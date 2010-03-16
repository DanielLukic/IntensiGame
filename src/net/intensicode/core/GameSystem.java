package net.intensicode.core;

import net.intensicode.*;
import net.intensicode.configuration.*;
import net.intensicode.graphics.*;
import net.intensicode.screens.*;
import net.intensicode.util.*;

public abstract class GameSystem
    {
    public final PlatformContext platform;

    public final SystemContext context;

    public final GameTiming timing;

    public final ScreenStack stack;

    public final SkinManager skin;

    public AnalogController analog;

    public ResourcesManager resources;

    public DirectGraphics graphics;

    public StorageManager storage;

    //#if SENSORS
    public SensorsManager sensors;
    //#endif

    public DirectScreen screen;

    //#ifdef TOUCH
    public TouchHandler touch;
    //#endif

    public AudioManager audio;

    public GameEngine engine;

    public NetworkIO network;

    public KeysHandler keys;

    //#if DEBUG
    public DebugScreen debug;
    //#endif

    //#if CONSOLE

    public ConsoleOverlay console;
    //#endif

    //#if STATS

    public EngineStats stats;
    //#endif


    public GameSystem( final SystemContext aSystemContext, final PlatformContext aPlatformContext )
        {
        context = aSystemContext;
        platform = aPlatformContext;
        stack = new ScreenStack( this );
        skin = new SkinManager( this );
        timing = new GameTiming();
        }

    public static void showException( final Throwable aThrowable )
        {
        synchronized ( myQueuedThrowables )
            {
            myQueuedThrowables.add( aThrowable );
            }
        }

    public final void showCriticalError( final String aMessage, final Throwable aOptionalThrowable )
        {
        Log.error( "critical system error", aOptionalThrowable );
        updateAndShowErrorScreen( aMessage, aOptionalThrowable );
        myErrorScreen.critical = true;
        }

    public final void showError( final String aMessage, final Throwable aOptionalThrowable )
        {
        Log.error( "system error", aOptionalThrowable );
        updateAndShowErrorScreen( aMessage, aOptionalThrowable );
        }

    public final void shutdownAndExit()
        {
        context.terminateApplication();
        }

    public final void setSystemFont( final FontGenerator aFontGenerator )
        {
        myErrorScreen.changeFont( aFontGenerator );
        //#if DEBUG
        debug.font = aFontGenerator;
        //#endif
        //#if CONSOLE
        console.font = aFontGenerator;
        //#endif
        //#if STATS
        stats.font = aFontGenerator;
        //#endif
        }

    public final ConfigurationElementsTree getSystemValues()
        {
        final ConfigurationElementsTree system = new ConfigurationElementsTree( "Game System" );

        final ConfigurationElementsTree trackball = system.addSubTree( "Trackball" );
        trackball.addLeaf( new TrackballPreset( analog ) );
        trackball.addLeaf( new InitialTicksThreshold( analog ) );
        trackball.addLeaf( new MultiTicksThreshold( analog ) );
        trackball.addLeaf( new AdditionalMultiTicksThreshold( analog ) );
        trackball.addLeaf( new SilenceBeforeUpdateInMillis( analog ) );
        trackball.addLeaf( new MultiEventThresholdInMillis( analog ) );
        trackball.addLeaf( new ForcedSilenceBetweenEventsInMillis( analog ) );
        trackball.addLeaf( new DirectionIgnoreFactorFixed( analog ) );

        final ConfigurationElementsTree timing = system.addSubTree( "Timing" );
        timing.addLeaf( new TicksPerSecond( this.timing ) );
        timing.addLeaf( new MaxFramesPerSecond( this.timing ) );
        //#if TIMING
        timing.addLeaf( new DumpTiming() );
        //#endif

        //#if CONSOLE
        final ConfigurationElementsTree console = system.addSubTree( "Console" );
        console.addLeaf( new ShowHideConsole( this.console ) );
        console.addLeaf( new ConsoleEntryStayTime( this.console ) );
        //#endif

        system.addLeaf( new LoadConfiguration( context ) );
        system.addLeaf( new SaveConfiguration( context ) );

        return system;
        }

    // Internal API

    public final DynamicArray getInformationStrings()
        {
        if ( myInformationStrings.size == 0 ) fillInformationStrings();
        return myInformationStrings;
        }

    public final boolean isInitialized()
        {
        return myInitializedFlag;
        }

    public final void start()
        {
        if ( myRunningFlag ) return;

        engine.startThreaded();
        audio.resumePlayback();
        //#if SENSORS
        sensors.enable();
        //#endif

        myRunningFlag = true;
        }

    private boolean myRunningFlag;

    public final void stop()
        {
        if ( !myRunningFlag ) return;

        context.onPauseApplication();

        //#if SENSORS
        sensors.disable();
        //#endif
        audio.haltPlayback();
        engine.stopThreaded();

        myRunningFlag = false;
        }

    public final void destroy()
        {
        stop();

        context.onDestroyApplication();

        cleanUp();
        dumpAndDisposeTiming();
        }

    private void cleanUp()
        {
        BitmapFontGenerator.purgeCaches();
        BitmapFontGenerator.resources = null;

        skin.destroy();

        // I18n..
        // ConsoleOverlay has static methods..
        // EngineStats..
        // ..

        System.gc();
        }

    private void dumpAndDisposeTiming()
        {
        //#if TIMING
        final StringBuffer buffer = new StringBuffer();
        Timing.dumpInto( buffer );
        System.out.println( buffer );
        //#endif

        Timing.reset();

        System.gc();
        }

    final void onFramesDropped()
        {
        context.onFramesDropped();
        }

    final void doSystemTick()
        {
        Timing.start( "doSystemTick" );
        try
            {
            doSystemTickUnsafe();
            }
        catch ( final Exception e )
            {
            showError( "critical game system failure", e );
            }
        finally
            {
            Timing.end( "doSystemTick" );
            }
        }

    final void doControlTick()
        {
        Timing.start( "doControlTick" );
        try
            {
            doControlTickUnsafe();
            }
        catch ( final Exception e )
            {
            showError( "active handler control tick failure", e );
            }
        finally
            {
            Timing.end( "doControlTick" );
            }
        }

    final void doDrawFrame()
        {
        Timing.start( "doDrawFrame" );
        try
            {
            doDrawFrameUnsafe();
            }
        catch ( final Exception e )
            {
            showError( "active handler draw frame failure", e );
            }
        finally
            {
            Timing.end( "doDrawFrame" );
            }
        }

    // Protected Interface

    protected void doSystemTickUnsafe() throws Exception
        {
        if ( !myInitializedFlag ) initialize();

        //#if SENSORS
        sensors.onControlTick();
        //#endif
        analog.onControlTick();
        keys.onControlTick();

        if ( stack.empty() ) throw new IllegalStateException( "no screen on stack" );

        if ( !myQueuedThrowables.empty() ) showNextQueuedThrowable();
        }

    protected void doControlTickUnsafe() throws Exception
        {
        stack.onControlTick();

        // handle back key if application didn't..
        if ( keys.checkAndConsume( KeysHandler.BACK_KEY ) ) stack.popScreen();
        }

    protected void doDrawFrameUnsafe() throws Exception
        {
        try
            {
            screen.beginFrame();
            stack.onDrawFrame();
            }
        finally
            {
            screen.endFrame();
            }
        }

    protected abstract void throwWrappedExceptionToTellCallingSystemAboutBrokenGameSystem( final Exception aException );

    protected abstract void fillInformationStrings();

    // Implementation

    private void updateAndShowErrorScreen( final String aMessage, final Throwable aOptionalThrowable )
        {
        try
            {
            myErrorScreen.reset();
            myErrorScreen.message = aMessage;
            if ( aOptionalThrowable != null ) myErrorScreen.setCause( aOptionalThrowable );
            stack.pushOnce( myErrorScreen );
            }
        catch ( final Exception e )
            {
            //#if DEBUG
            Log.error( "showing error screen failed", e );
            //#endif
            throwWrappedExceptionToTellCallingSystemAboutBrokenGameSystem( e );
            }
        }

    private void initialize() throws Exception
        {
        //#if TOUCH
        stack.addGlobalHandler( touch );
        //#endif
        //#if DEBUG
        debug = new DebugScreen( resources.getSmallDefaultFont() );
        stack.addGlobalHandler( debug );
        //#endif
        //#if CONSOLE
        console = new ConsoleOverlay( resources.getSmallDefaultFont() );
        stack.addGlobalHandler( console );
        //#endif
        //#if STATS
        stats = new EngineStats( resources.getSmallDefaultFont() );
        stack.addGlobalHandler( stats );
        //#endif
        initializeErrorScreen();
        initializeMainController();

        myInitializedFlag = true;
        }

    private void initializeErrorScreen()
        {
        final FontGenerator font = resources.getSmallDefaultFont();
        myErrorScreen = new ErrorScreen( font );
        }

    private void initializeMainController() throws Exception
        {
        stack.pushScreen( context.createMainScreen() );
        }

    private void showNextQueuedThrowable()
        {
        synchronized ( myQueuedThrowables )
            {
            //#if DEBUG
            Assert.isFalse( "queued throwable available", myQueuedThrowables.empty() );
            //#endif

            final Throwable throwable = (Throwable) myQueuedThrowables.remove( 0 );
            showError( throwable.getMessage(), throwable );
            }
        }


    private boolean myInitializedFlag;

    private ErrorScreen myErrorScreen;

    protected final DynamicArray myInformationStrings = new DynamicArray();

    private static final DynamicArray myQueuedThrowables = new DynamicArray();
    }
