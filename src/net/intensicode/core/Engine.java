/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import net.intensicode.util.*;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import java.util.Vector;



/**
 * TODO: Describe this!
 */
public final class Engine implements Runnable
    {
    //#if DEBUG
    public static int slowDownInTicks;
    //#endif

    public static boolean pause;

    public static boolean singleStep;


    public static int limitFps = 50;

    public static int ticksPerSecond = 50;

    public static int limitTpsPerLoop = -1;// (ticksPerSecond/limitFps+1)*2;

    public static boolean showStats;

    public final SystemContext systemContext;

    public final ResourceLoader loader;

    public final DirectScreen screen;

    public final NetworkIO nio;

    public final Sound sound;

    public final Keys keys;


    public boolean applicationShouldPause;

    public int realTps = -1;

    public int realFps = -1;



    public Engine( final SystemContext aSystemContext, final Size aTargetScreenSize )
        {
        systemContext = aSystemContext;

        loader = aSystemContext.getResourceLoader();
        sound = new Sound( this );
        keys = new Keys();
        nio = new NetworkIO();

        screen = new DirectScreen( keys, aTargetScreenSize );

        final Font systemFont = Font.getFont( Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL );
        myEngineFont = new SystemFontGen( systemFont );
        myErrorScreen = new ErrorScreen( myEngineFont );
        }

    public final void addGlobalHandler( final AbstractScreen aGlobalHandler ) throws Exception
        {
        myGlobalHandler.addScreen( aGlobalHandler );
        }

    public final int numberOfStackedScreens()
        {
        return myScreenStack.size();
        }

    public final AbstractScreen activeScreenBase()
        {
        if ( myScreenStack.size() == 0 ) return null;
        return (AbstractScreen) myScreenStack.lastElement();
        }

    public final void start() throws Exception
        {
        keys.initialize( screen );

        systemContext.getDisplay().setCurrent( screen );
        screen.visible = true;

        if ( myThread != null ) return;

        screen.start();
        sound.start();
        nio.start();

        myThread = new Thread( this );
        myThread.setPriority( Thread.MIN_PRIORITY );
        myThread.start();

        myGlobalHandler.onInit( this, screen );
        }

    public final void stop()
        {
        systemContext.getDisplay().setCurrent( null );
        screen.visible = false;

        if ( myThread == null ) return;
        myThread = null;

        nio.stop();
        sound.stop();
        }

    public static void showError( final String aMessage, final Throwable aThrowable )
        {
        myErrors.add( aMessage );
        myExceptions.add( aThrowable );
        }

    public static void showException( final Throwable aThrowable )
        {
        showError( "System Error", aThrowable );
        }

    public final void pushOnce( final AbstractScreen aScreen ) throws Exception
        {
        if ( aScreen == null ) throw new IllegalArgumentException();
        while ( myScreenStack.removeElement( aScreen ) ) ;
        pushScreen( aScreen );
        }

    public final void pushScreen( final AbstractScreen aScreen ) throws Exception
        {
        if ( aScreen == null ) throw new IllegalArgumentException();
        keys.reset( ticksPerSecond );

        if ( myScreenStack.size() > 0 ) activeScreenBase().onPop( this );

        myScreenStack.addElement( aScreen );
        aScreen.onInit( this, screen );

        //#if DEBUG
        Log.debug( "New active handler: {}", activeScreenBase() );
        //#endif

        BitmapFontGen.purgeCaches();
        }

    public final void popScreen( final AbstractScreen aScreen ) throws Exception
        {
        final AbstractScreen activeHandler = activeScreenBase();
        if ( activeHandler == aScreen ) popScreen();
        }

    public final void popScreen() throws Exception
        {
        if ( myScreenStack.size() <= 1 ) throw new IllegalStateException();
        keys.reset( ticksPerSecond );

        activeScreenBase().onPop( this );
        myScreenStack.removeElementAt( myScreenStack.size() - 1 );

        final AbstractScreen active = activeScreenBase();
        if ( active != null ) active.onInit( this, screen );

        //#if DEBUG
        Log.debug( "New active handler: {}", active );
        //#endif

        BitmapFontGen.purgeCaches();
        }

    public final void remove( final AbstractScreen aScreen )
        {
        while ( myScreenStack.removeElement( aScreen ) ) ;
        }

    public final void shutdownAndExit()
        {
        systemContext.exit();
        }

    // From Runnable

    public final void run()
        {
        myLastTimingStart = myLastFrameTime = myLastTickTime = now();
        myRemainingTickTime = myFrameCount = 0;
        realFps = 0;

        screen.clearAndUpdate();

        while ( myThread != null )
            {
            try
                {
                if ( screen.engineShouldPause )
                    {
                    // Unfortunately phones don't really like this.. So I leave the
                    // applicationShouldPause flag handling, but remove all the good stuff.

                    ////#if DEBUG
                    //Log.debug( "Engine pausing MIDlet" );
                    ////#endif
                    //systemContext.pause();

                    screen.engineShouldPause = false;
                    applicationShouldPause = true;

                    //// Give the application a chance to react:
                    //doControlTick();
                    //
                    //continue;
                    }

                final long deltaTime = now() - myLastFrameTime;
                if ( deltaTime > MAX_TIME_DELTA || screen.visible == false )
                    {
                    Thread.yield();
                    myLastTimingStart = myLastFrameTime = myLastTickTime = now();
                    continue;
                    }

                if ( myScreenStack.size() == 0 )
                    {
                    if ( myMainController == null )
                        {
                        myMainController = systemContext.initMainController();
                        }
                    pushOnce( myMainController );
                    continue;
                    }

                if ( myExceptions.size > 0 && activeScreenBase() != myErrorScreen )
                    {
                    final String error = (String) myErrors.remove( myErrors.size - 1 );
                    final Throwable throwable = (Throwable) myExceptions.remove( myExceptions.size - 1 );
                    showErrorScreen( error, throwable );
                    }

                runOneLoop();
                }
            catch ( final Throwable t )
                {
                // System fucked up:
                if ( activeScreenBase() == myErrorScreen )
                    {
                    t.printStackTrace();
                    shutdownAndExit();
                    }
                showErrorScreen( "Critical failure", t );
                }
            }
        }

    // Implementation

    private final void showErrorScreen( final String aMessage, final Throwable t )
        {
        //#if DEBUG
        System.out.println( aMessage );
        //#endif

        myErrorScreen.reset();
        myErrorScreen.setMessage( aMessage );
        if ( t != null )
            {
            myErrorScreen.addCause( t );
            t.printStackTrace();
            }

        try
            {
            pushOnce( myErrorScreen );
            }
        catch ( final Exception e )
            {
            System.out.println( e );
            shutdownAndExit();
            }
        }

    private final void runOneLoop() throws Exception
        {
        if ( keys.checkAndConsume( Keys.PAUSE_KEY ) ) pause = !pause;

        doDrawFrame();
        doControlTicks();
        waitMinimumFrameTime();

        final long framesTime = now() - myLastTimingStart;
        if ( framesTime > 1000 )
            {
            realFps = (int) ( myFrameCount * 1000 / framesTime );
            realTps = (int) ( myTickCount * 1000 / framesTime );
            myFrameCount = 0;
            myTickCount = 0;
            myLastTimingStart += framesTime;
            }
        }

    private final void waitMinimumFrameTime()
        {
        if ( limitFps <= 0 ) return;

        while ( limitFps > 0 )
            {
            final long frameTime = now() - myLastFrameTime;
            final long expectedTime = 1000 / limitFps;
            final long delta = expectedTime - frameTime;
            if ( delta < 1 ) break;
            try
                {
                Thread.sleep( delta / 4 );
                }
            catch ( InterruptedException e )
                {
                }
            }
        }

    private final void doControlTicks() throws Exception
        {
        final long now = now();
        if ( now - myLastTickTime > MAX_TIME_DELTA )
            {
            myLastTickTime = now;
            myRemainingTickTime = 0;
            }

        final long timeForTicks = ( now - myLastTickTime ) + myRemainingTickTime;
        myLastTickTime = now;

        final int requiredTpsPerLoop = ( ticksPerSecond / limitFps + 1 ) * 2;
        if ( limitTpsPerLoop == -1 || limitTpsPerLoop < requiredTpsPerLoop )
            {
            limitTpsPerLoop = ( ticksPerSecond / limitFps + 1 ) * 2;
            }

        final long realNumberOfTicks = timeForTicks * ticksPerSecond / 1000;
        final long numberOfTicks = Math.min( limitTpsPerLoop, realNumberOfTicks );
        myRemainingTickTime = timeForTicks - numberOfTicks * 1000 / ticksPerSecond;

        for ( int idx = 0; idx < numberOfTicks; idx++ )
            {
            doSlowDownTicks();
            }

        myTickCount += numberOfTicks;
        }

    private final void doSlowDownTicks() throws Exception
        {
        //#if DEBUG
        if ( slowDownInTicks > 0 )
            {
            if ( myRemainingSlowDownTicks < slowDownInTicks )
                {
                myRemainingSlowDownTicks++;
                keys.onControlTick();
                }
            else
                {
                myRemainingSlowDownTicks = 0;
                doControlTick();
                }
            }
        else if ( slowDownInTicks < 0 )
            {
            for ( int repeat = 0; repeat < Math.abs( slowDownInTicks ); repeat++ )
                {
                doControlTick();
                }
            }
        else
            //#endif
            {
            doControlTick();
            }
        }

    private final void doControlTick() throws Exception
        {
        sound.onControlTick();
        keys.onControlTick();

        if ( pause ) return;

        //#if DEBUG
        final int keycode = keys.lastInvalidCode;
        if ( keycode != 0 )
            {
            keys.lastInvalidCode = 0;
            showErrorScreen( "Unknown keycode: " + keycode, null );
            }
        //#endif

        try
            {
            final AbstractScreen activeScreen = activeScreenBase();
            if ( activeScreen != null ) activeScreen.onControlTick( this );
            if ( myGlobalHandler != null ) myGlobalHandler.onControlTick( this );
            }
        catch ( final Exception e )
            {
            showErrorScreen( "Active handler failed", e );
            }

        if ( singleStep )
            {
            pause = true;
            singleStep = false;
            }
        }

    private final void doDrawFrame()
        {
        myLastFrameTime = now();
        myFrameCount++;

        final AbstractScreen activeHandler = activeScreenBase();
        final Graphics gc = screen.graphics();
        if ( gc != null )
            {
            activeHandler.onDrawFrame( screen );

            if ( myGlobalHandler != null ) myGlobalHandler.onDrawFrame( screen );

            if ( showStats )
                {
                final Runtime runtime = Runtime.getRuntime();
                final long total = runtime.totalMemory();
                final long free = runtime.freeMemory();
                final int percentFree = (int) ( free * 100 / total );
                final int freeMemInKB = (int) ( runtime.totalMemory() / 1024 );

                myBlitPos.x = screen.width();
                myBlitPos.y = myEngineFont.charHeight() * 0;
                myEngineFont.blitNumber( gc, myBlitPos, realFps, ALIGN_TOP_RIGHT );

                myBlitPos.y = myEngineFont.charHeight() * 1;
                myEngineFont.blitNumber( gc, myBlitPos, realTps, ALIGN_TOP_RIGHT );

                myBlitPos.y = myEngineFont.charHeight() * 2;
                myEngineFont.blitNumber( gc, myBlitPos, percentFree, ALIGN_TOP_RIGHT );

                myBlitPos.y = myEngineFont.charHeight() * 3;
                myEngineFont.blitNumber( gc, myBlitPos, freeMemInKB, ALIGN_TOP_RIGHT );
                }

            if ( pause )
                {
                myBlitPos.x = screen.width() / 2;
                myBlitPos.y = screen.height() / 2;
                myEngineFont.blitString( gc, "PAUSE", myBlitPos, ALIGN_CENTER );
                }

            screen.update();
            }
        }

    private static final long now()
        {
        return System.currentTimeMillis();
        }



    private Thread myThread;

    private int myTickCount;

    private int myFrameCount;

    private long myLastTickTime;

    private long myLastFrameTime;

    private long myLastTimingStart;

    private long myRemainingTickTime;

    //#if DEBUG
    private int myRemainingSlowDownTicks;
    //#endif


    private AbstractScreen myMainController;


    private final FontGen myEngineFont;

    private final ErrorScreen myErrorScreen;

    private final Vector myScreenStack = new Vector();

    private final Position myBlitPos = new Position();

    private final MultiScreen myGlobalHandler = new MultiScreen();


    private static final DynamicArray myErrors = new DynamicArray();

    private static final DynamicArray myExceptions = new DynamicArray();

    private static final int MAX_TIME_DELTA = 250;// minimum FPS == 4

    private static final int ALIGN_TOP_RIGHT = FontGen.TOP | FontGen.RIGHT;

    private static final int ALIGN_CENTER = FontGen.CENTER;
    }
