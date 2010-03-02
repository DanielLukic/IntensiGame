package net.intensicode.core;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.screens.*;
import net.intensicode.util.*;

public abstract class GameSystem
    {
    public final SystemContext context;

    public final GameTiming timing;

    public final ScreenStack stack;

    public final SkinManager skin;

    public ResourcesManager resources;

    public DirectGraphics graphics;

    public StorageManager storage;

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


    public GameSystem( final SystemContext aSystemContext )
        {
        context = aSystemContext;
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
        debug.changeFont( aFontGenerator );
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
        engine.startThreaded();
        audio.resumePlayback();
        }

    public final void stop()
        {
        context.onApplicationShouldPause( this );

        audio.haltPlayback();
        engine.stopThreaded();
        }

    final void onFramesDropped()
        {
        context.onFramesDropped( this );
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

        //#if TOUCH
        touch.onControlTick();
        //#endif
        keys.onControlTick();

        if ( stack.empty() ) throw new IllegalStateException( "no screen on stack" );

        if ( !myQueuedThrowables.empty() ) showNextQueuedThrowable();
        }

    protected void doControlTickUnsafe() throws Exception
        {
        stack.onControlTick( this );
        }

    protected void doDrawFrameUnsafe() throws Exception
        {
        try
            {
            screen.beginFrame();
            stack.onDrawFrame( this );
            //#if TOUCH
            touch.onDrawFrame();
            //#endif
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
            if ( stack.activeScreen() == myErrorScreen )
                {
                throw new RuntimeException( "error screen is causing errors :)" );
                }
            myErrorScreen.reset();
            myErrorScreen.message = aMessage;
            if ( aOptionalThrowable != null ) myErrorScreen.setCause( aOptionalThrowable );
            stack.pushOnce( myErrorScreen );
            }
        catch ( final Exception e )
            {
            throwWrappedExceptionToTellCallingSystemAboutBrokenGameSystem( e );
            }
        }

    private void initialize() throws Exception
        {
        //#if DEBUG
        debug = new DebugScreen( resources.getSmallDefaultFont() );
        stack.addGlobalHandler( debug );
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
        stack.pushScreen( context.createMainScreen( this ) );
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
