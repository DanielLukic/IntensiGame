package net.intensicode.core;

import net.intensicode.graphics.BitmapFontGenerator;
import net.intensicode.screens.*;
import net.intensicode.util.*;

public final class ScreenStack
    {
    public ScreenStack( final GameSystem aSystem )
        {
        myGameSystem = aSystem;
        }

    //#if ORIENTATION_DYNAMIC

    public void onOrientationChanged()
        {
        for ( int idx = 0; idx < myScreenStack.size; idx++ )
            {
            try
                {
                final ScreenBase screen = (ScreenBase) myScreenStack.get( idx );
                screen.onOrientationChanged();
                }
            catch ( final Exception e )
                {
                myGameSystem.showError( null, e );
                }
            }
        }

    //#endif

    public final void addGlobalHandler( final ScreenBase aGlobalHandler ) throws Exception
        {
        myGlobalHandler.onInit( myGameSystem );
        myGlobalHandler.addScreen( aGlobalHandler );
        aGlobalHandler.onInit( myGameSystem );
        }

    public final boolean empty()
        {
        return numberOfStackedScreens() == 0;
        }

    public final int numberOfStackedScreens()
        {
        return myScreenStack.size;
        }

    public final ScreenBase activeScreen()
        {
        if ( myScreenStack.empty() ) return myNullScreen;
        return (ScreenBase) myScreenStack.last();
        }

    public final void onDrawFrame()
        {
        final ScreenBase activeScreen = activeScreen();
        activeScreen.onDrawFrame();

        // Drawing global handlers after the active screen:
        myGlobalHandler.onDrawFrame();
        }

    public final void onControlTick() throws Exception
        {
        // Ticking global handlers before the active screen:
        myGlobalHandler.onControlTick();

        final ScreenBase activeScreen = activeScreen();
        activeScreen.onControlTick();
        }

    public final void pushOnce( final ScreenBase aScreen ) throws Exception
        {
        //#if DEBUG
        Assert.isNotNull( "pushed screen null", aScreen );
        //#endif

        remove( aScreen );
        pushScreen( aScreen );
        }

    public final void pushScreen( final ScreenBase aScreen ) throws Exception
        {
        //#if DEBUG
        Assert.isNotNull( "pushed screen null", aScreen );
        //#endif

        resetKeys();

        if ( !myScreenStack.empty() ) activeScreen().onPop();

        myScreenStack.add( aScreen );
        aScreen.onInit( myGameSystem );
        aScreen.onTop();

        //#if ANAL
        myGameSystem.hooks.trackPageView( aScreen.getClass().getName() );
        //#endif

        //#if DEBUG
        Log.debug( "New active handler: {}", activeScreen() );
        //#endif

        // TODO: Move this to a registered 'handler'?
        BitmapFontGenerator.purgeCaches();
        //#if TOUCH
        myGameSystem.touch.purgePendingEvents();
        //#endif
        }

    public final void popScreen( final ScreenBase aScreen ) throws Exception
        {
        final ScreenBase activeHandler = activeScreen();
        if ( activeHandler == aScreen ) popScreen();
        else throw new IllegalArgumentException();
        }

    public final void popScreen() throws Exception
        {
        //#if DEBUG
        Assert.isFalse( "no screen on stack", myScreenStack.empty() );
        //#endif

        resetKeys();

        activeScreen().onPop();
        myScreenStack.removeLast();

        final ScreenBase active = activeScreen();
        if ( active != null ) active.onInit( myGameSystem );
        if ( active != null ) active.onTop();

        //#if ANAL
        if ( active != null ) myGameSystem.hooks.trackPageView( active.getClass().getName() );
        //#endif

        //#if DEBUG
        Log.debug( "New active handler: {}", active );
        //#endif

        // TODO: Move this to a registered 'handler'?
        BitmapFontGenerator.purgeCaches();
        //#if TOUCH
        myGameSystem.touch.purgePendingEvents();
        //#endif
        }

    public final void remove( final ScreenBase aScreen )
        {
        myScreenStack.removeAll( aScreen );
        }

    // Implementation

    private void resetKeys()
        {
        myGameSystem.keys.reset( myGameSystem.timing.ticksPerSecond );
        }


    private final GameSystem myGameSystem;

    private final DynamicArray myScreenStack = new DynamicArray();

    private final MultiScreen myGlobalHandler = new MultiScreen();

    private final ScreenBase myNullScreen = new MultiScreen();
    }
