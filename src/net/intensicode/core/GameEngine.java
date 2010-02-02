package net.intensicode.core;

import net.intensicode.util.Assert;

public abstract class GameEngine implements Runnable
    {
    public int slowDownInTicks;

    public boolean singleStep;

    public boolean paused;


    public GameEngine( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    /**
     * Call this to start the engine in threaded mode. The engine will then issue control ticks and draw frames
     * continously. It expects a GameView with proper beginFrame and endFrame implementation.
     */
    public final void showNotify()
        {
        if ( !engineThreadCreated() ) createEngineThread();
        startEngineThread();
        }

    /**
     * Call this to stop the engine in threaded mode after it has been started by a call to showNotify.
     */
    public final void hideNotify()
        {
        stopAndReleaseEngineThread();
        }

    /**
     * Call this to render a single frame after issuing control ticks for the time passed since the previous call to
     * this function.
     */
    public void runSingleLoop()
        {
        doControlTicks();
        doDrawFrame();
        updateTimingStatsIfPossible();
        }

    // From Runnable

    public final void run()
        {
        try
            {
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
        myThread = new Thread( this );
        }

    protected final void startEngineThread()
        {
        //#if DEBUG
        Assert.isNotNull( "engine thread must exist", myThread );
        //#endif
        if ( !myThread.isAlive() ) myThread.start();
        }

    protected final void stopAndReleaseEngineThread()
        {
        if ( myThread == null ) return;

        myShouldBailOutFlag = true;

        final Thread thread = myThread;
        myThread = null;

        //#if ANDROID
        thread.interrupt();
        //#endif

        try
            {
            thread.join();
            }
        catch ( final InterruptedException e )
            {
            // We can only assume everything is killed here..
            }

        myShouldBailOutFlag = false;
        }

    // Implementation

    private void runInterruptible() throws InterruptedException
        {
        timing().reset();
        while ( !myShouldBailOutFlag )
            {
            runLoopOrYieldIfTooSlow();
            }
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
        doDrawFrame();
        waitMinimumFrameTime();
        updateTimingStatsIfPossible();
        }

    private void doDrawFrame()
        {
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
        doSystemControlTick();
        if ( paused ) return;

        myGameSystem.doControlTick();

        if ( !singleStep ) return;
        paused = true;
        singleStep = false;
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
            Thread.sleep( frameWaitTime / 3 );
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
    }
