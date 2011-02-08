package net.intensicode.core;

import net.intensicode.*;
import net.intensicode.configuration.*;
import net.intensicode.configuration.timing.*;
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

    public ResourcesManager resources;

    public DirectGraphics graphics;

    public StorageManager storage;

    //#if SENSORS

    public SensorsManager sensors;

    //#endif

    public DirectScreen screen;

    //#ifdef TRACKBALL

    public net.intensicode.trackball.TrackballHandler trackball;

    //#endif

    //#ifdef TOUCH

    public net.intensicode.touch.TouchHandler touch;

    //#endif

    public AudioManager audio;

    public GameEngine engine;

    public NetworkIO network;

    //#if ONLINE

    public OnlineAPI online;

    //#endif

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

    public FontGenerator systemFont;


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
        Log.error( "critical system error: {}", aMessage, aOptionalThrowable );
        platform.showCriticalError( aMessage, aOptionalThrowable );
        }

    public final void showError( final String aMessage, final Throwable aOptionalThrowable )
        {
        Log.error( "system error: {}", aMessage, aOptionalThrowable );
        platform.showError( aMessage, aOptionalThrowable );
        }

    public final void shutdownAndExit()
        {
        context.terminateApplication();
        }

    public final void setSystemFont( final FontGenerator aFontGenerator )
        {
        systemFont = aFontGenerator;

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

        try
            {
            //#if !RELEASE

            final ConfigurationElementsTree timing = system.addSubTree( "Timing" );
            timing.addLeaf( new TicksPerSecond( this.timing ) );
            timing.addLeaf( new MaxFramesPerSecond( this.timing ) );
            //#if TIMING
            timing.addLeaf( new DumpTiming() );
            //#endif

            //#if CONSOLE
            final ConfigurationElementsTree console = system.addSubTree( "Console" );
            console.addLeaf( new net.intensicode.configuration.console.ShowHideConsole( this.console ) );
            console.addLeaf( new net.intensicode.configuration.console.ConsoleEntryStayTime( this.console ) );
            //#endif

            system.addLeaf( new LoadConfiguration( context ) );
            system.addLeaf( new SaveConfiguration( context ) );
            system.addLeaf( new DeleteConfiguration( context ) );

            //#endif
            }
        catch ( final Exception e )
            {
            showError( "failed preparing system values for configuration menu. ignored.", e );
            }

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

        myInitializedFlag = false;

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
        try
            {
            doSystemTickUnsafe();
            }
        catch ( final Exception e )
            {
            showError( "critical game system failure", e );
            }
        }

    final void doControlTick()
        {
        try
            {
            doControlTickUnsafe();
            }
        catch ( final Exception e )
            {
            showError( "active handler control tick failure", e );
            }
        }

    final void doDrawFrame()
        {
        try
            {
            doDrawFrameUnsafe();
            }
        catch ( final Exception e )
            {
            showError( "active handler draw frame failure", e );
            }
        }

    // Protected Interface

    protected void doSystemTickUnsafe() throws Exception
        {
        if ( !myInitializedFlag ) initialize();

        //#if SENSORS
        sensors.onControlTick();
        //#endif
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

    private void initialize() throws Exception
        {
        //#if TOUCH
        stack.addGlobalHandler( touch );
        //#endif
        //#if TRACKBALL
        stack.addGlobalHandler( trackball );
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

        systemFont = resources.getSmallDefaultFont();

        initializeMainController();

        myInitializedFlag = true;
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

    protected final DynamicArray myInformationStrings = new DynamicArray();

    private static final DynamicArray myQueuedThrowables = new DynamicArray();
    }
