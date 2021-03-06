package net.intensicode.core;

import net.intensicode.util.Assert;
import net.intensicode.util.Log;

public abstract class GameEngine implements Runnable
    {
    public int slowDownInTicks;

    public boolean singleStep;

    public boolean paused;

    //#if ORIENTATION_DYNAMIC

    public boolean orientationChanged;

    //#endif


    public GameEngine( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    // Internal API

    /**
     * Call this to start the engine in threaded mode. The engine will then issue control ticks and draw frames
     * continously. It expects a GameView with proper beginFrame and endFrame implementation.
     */
    public synchronized final void startThreaded()
        {
        if ( !engineThreadCreated() ) createEngineThread();
        startEngineThread();
        }

    /**
     * Call this to stop the engine in threaded mode after it has been started by a call to startThreaded.
     */
    public synchronized final void stopThreaded()
        {
        stopAndReleaseEngineThread();
        }

    // From Runnable

    public final void run()
        {
        waitSomeTimeForViewToStabilize();
        try
            {
            myGameSystem.screen.initialize();
            runInterruptible();
            }
        catch ( final InterruptedException e )
            {
            // Simply bail out here..
            }
        catch ( final Throwable t )
            {
            myGameSystem.showCriticalError( "critical game engine failure", t );
            }
        myGameSystem.screen.cleanup();
        }

    private void waitSomeTimeForViewToStabilize()
        {
        try
            {
            // Encountered on the Nexus One: When in landscape mode, sometimes the portrait screen size is reported
            // early when creating the view/surface. This lasts only for a few milliseconds. See the other part of this
            // fix in AndroidGameView#onSurfaceChanged.
            Thread.sleep( VIEW_STABILIZE_TIME_IN_MILLIS );
            }
        catch ( final InterruptedException e )
            {
            Log.error( "interrupted while waiting for view to stabilize", null );
            }
        }

    // Protected Interface

    protected final boolean engineThreadCreated()
        {
        return myThread != null;
        }

    protected final void createEngineThread()
        {
        //#if DEBUG
        Assert.isNull( "engine thread must not exist", myThread );
        //#endif
        myThread = new Thread( this, "GameEngine" );
        }

    protected final void startEngineThread()
        {
        //#if DEBUG
        Assert.isNotNull( "engine thread must exist", myThread );
        //#endif
        try
            {
            if ( !myThread.isAlive() ) myThread.start();
            }
        catch ( final IllegalThreadStateException e )
            {
            Log.error( "ignored", e );
            }
        }

    protected final void stopAndReleaseEngineThread()
        {
        if ( myThread == null ) return;

        myShouldBailOutFlag = true;

        final Thread thread = myThread;
        myThread = null;

        if ( thread == Thread.currentThread() ) return;

        try
            {
            final int waitTries = 5;
            for ( int idx = 0; idx < waitTries; idx++ )
                {
                if ( !thread.isAlive() ) break;
                Log.debug( "waiting for engine thread to terminate.." );
                Thread.sleep( 250 );
                if ( idx > waitTries / 2 ) thread.interrupt();
                }
            Log.debug( "engine thread terminated" );
            }
        catch ( final InterruptedException e )
            {
            // We can only assume everything is killed here..
            }
        }

    // Implementation

    private void runInterruptible() throws InterruptedException
        {
        timing().reset();
        while ( !myShouldBailOutFlag )
            {
            runLoopOrYieldIfTooSlow();
            }
        myShouldBailOutFlag = false;
        }

    private void runLoopOrYieldIfTooSlow() throws InterruptedException
        {
        if ( timing().tooMuchTimeHasPassed() )
            {
            timing().reset();
            myGameSystem.onFramesDropped();
            }
        else
            {
            runOneLoop();
            }
        }

    private void runOneLoop() throws InterruptedException
        {
        doControlTicks();
        if ( myGameSystem.isInitialized() ) doDrawFrame();
        waitMinimumFrameTime();
        updateTimingStatsIfPossible();
        }

    private void doDrawFrame() throws InterruptedException
        {
        notifyOrientationChangedIfNecessary();
        timing().notifyStartOfFrame();
        myGameSystem.doDrawFrame();
        }

    private void doControlTicks()
        {
        final int numberOfTicks = timing().numberOfPendingTicks();
        for ( int idx = 0; idx < numberOfTicks; idx++ )
            {
            handleControlTickWithSlowDownAndRepetition();
            }
        }

    private void handleControlTickWithSlowDownAndRepetition()
        {
        timing().notifyStartOfTick();
        if ( slowDownInTicks > 0 )
            {
            doSlowDownTick();
            }
        else if ( slowDownInTicks < 0 )
            {
            doRepeatedTick( Math.abs( slowDownInTicks ) );
            }
        else
            {
            doControlTick();
            }
        }

    private void doSlowDownTick()
        {
        if ( myRemainingSlowDownTicks < slowDownInTicks )
            {
            myRemainingSlowDownTicks++;
            doSystemControlTick();
            }
        else
            {
            myRemainingSlowDownTicks = 0;
            doControlTick();
            }
        }

    private void doRepeatedTick( final int aNumberOfRepetitions )
        {
        for ( int repeat = 0; repeat < aNumberOfRepetitions; repeat++ )
            {
            doControlTick();
            }
        }

    private void doControlTick()
        {
        notifyOrientationChangedIfNecessary();

        doSystemControlTick();
        if ( paused ) return;

        myGameSystem.doControlTick();

        if ( !singleStep ) return;
        paused = true;
        singleStep = false;
        }

    private void notifyOrientationChangedIfNecessary()
        {
        //#if ORIENTATION_DYNAMIC
        if ( !orientationChanged ) return;
        orientationChanged = false;
        myGameSystem.context.onOrientationChanged();
        //#endif
        }

    private void doSystemControlTick()
        {
        myGameSystem.doSystemTick();
        }

    private void waitMinimumFrameTime() throws InterruptedException
        {
        while ( true )
            {
            final long frameWaitTime = timing().frameWaitTime();
            if ( frameWaitTime <= 0 ) break;
            Thread.sleep( frameWaitTime * 2 / 3 );
            }
        }

    private void updateTimingStatsIfPossible()
        {
        if ( timing().enoughTimeHasPassedForUpdatingStats() ) timing().updateStats();
        }

    private GameTiming timing()
        {
        return myGameSystem.timing;
        }


    private Thread myThread;

    private boolean myShouldBailOutFlag;

    private int myRemainingSlowDownTicks;

    protected final GameSystem myGameSystem;

    private static final int VIEW_STABILIZE_TIME_IN_MILLIS = 100;
    }
