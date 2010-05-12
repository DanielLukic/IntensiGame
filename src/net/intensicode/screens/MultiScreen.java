package net.intensicode.screens;

import net.intensicode.core.GameSystem;
import net.intensicode.util.DynamicArray;

public class MultiScreen extends ScreenBase
    {
    public MultiScreen()
        {
        }

    public int numberOfScreens()
        {
        return myScreens.size;
        }

    public void insertScreen( final ScreenBase aScreen ) throws Exception
        {
        myScreens.insert( 0, new MultiScreenHandle( aScreen ) );
        if ( isInitialized() ) aScreen.onInit( system() );
        }

    public void addScreen( final ScreenBase aScreen ) throws Exception
        {
        myScreens.add( new MultiScreenHandle( aScreen ) );
        if ( isInitialized() ) aScreen.onInit( system() );
        }

    public void removeScreen( final ScreenBase aScreen )
        {
        final MultiScreenHandle handle = findHandle( aScreen );
        myScreens.removeAll( handle );
        }

    public final void removeAllScreens()
        {
        myScreens.clear();
        }

    public final boolean isVisible( final ScreenBase aScreen )
        {
        return findHandle( aScreen ).visible;
        }

    public final void setVisibility( final ScreenBase aScreen, final boolean aVisibility )
        {
        final MultiScreenHandle handle = findHandle( aScreen );
        handle.visible = aVisibility;
        }

    // From ScreenBase

    public void onTop()
        {
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            handle.screen.onTop();
            }
        }

    public void onPop()
        {
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            handle.screen.onPop();
            }
        }

    //#if ORIENTATION_DYNAMIC

    public void onOrientationChanged() throws Exception
        {
        onInitEverytime();
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            handle.screen.onOrientationChanged();
            }
        }

    //#endif

    public void onInit( final GameSystem aGameSystem ) throws Exception
        {
        super.onInit( aGameSystem );

        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            handle.screen.onInit( aGameSystem );
            }
        }

    public void onControlTick() throws Exception
        {
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.visible ) handle.screen.onControlTick();
            }
        }

    public void onDrawFrame()
        {
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.visible ) handle.screen.onDrawFrame();
            }
        }

    // Protected Interface

    protected final MultiScreenHandle screenHandle( final int aIdx )
        {
        return (MultiScreenHandle) myScreens.get( aIdx );
        }

    protected final MultiScreenHandle findHandle( final ScreenBase aScreen )
        {
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.screen == aScreen ) return handle;
            }
        throw new IllegalArgumentException();
        }

    protected final boolean hasScreen( final ScreenBase aScreen )
        {
        for ( int idx = 0; idx < myScreens.size; idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.screen == aScreen ) return true;
            }
        return false;
        }

    private final DynamicArray myScreens = new DynamicArray();
    }
