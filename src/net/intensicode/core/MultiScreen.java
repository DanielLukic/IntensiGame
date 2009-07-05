/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import java.util.Vector;



/**
 * TODO: Describe this!
 */
public class MultiScreen extends AbstractScreen
    {
    public MultiScreen()
        {
        }

    public int numberOfScreens()
        {
        return myScreens.size();
        }

    public void insertScreen( final AbstractScreen aScreen ) throws Exception
        {
        myScreens.insertElementAt( new MultiScreenHandle( aScreen ), 0 );

        if ( isInitialized() ) aScreen.onInit( engine(), screen() );
        }

    public void addScreen( final AbstractScreen aScreen ) throws Exception
        {
        myScreens.addElement( new MultiScreenHandle( aScreen ) );

        if ( isInitialized() )
            {
            aScreen.onInit( engine(), screen() );
            }
        }

    public void removeScreen( final AbstractScreen aScreen )
        {
        final MultiScreenHandle handle = findHandle( aScreen );
        while ( myScreens.removeElement( handle ) ) ;
        }

    public final void removeAllScreens()
        {
        myScreens.removeAllElements();
        }

    public final boolean isVisible( final AbstractScreen aScreen )
        {
        return findHandle( aScreen ).visible;
        }

    public final void setVisibility( final AbstractScreen aScreen, final boolean aVisibility )
        {
        final MultiScreenHandle handle = findHandle( aScreen );
        handle.visible = aVisibility;
        }

    // From AbstractScreen

    public void onInit( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        super.onInit( aEngine, aScreen );

        for ( int idx = 0; idx < myScreens.size(); idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            handle.screen.onInit( aEngine, aScreen );
            }
        }

    public void onControlTick( final Engine aEngine ) throws Exception
        {
        for ( int idx = 0; idx < myScreens.size(); idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.visible )
                {
                handle.screen.onControlTick( aEngine );
                }
            }
        }

    public void onDrawFrame( final DirectScreen aScreen )
        {
        for ( int idx = 0; idx < myScreens.size(); idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.visible )
                {
                handle.screen.onDrawFrame( aScreen );
                }
            }
        }

    // Protected Interface

    protected final MultiScreenHandle screenHandle( final int aIdx )
        {
        return (MultiScreenHandle) myScreens.elementAt( aIdx );
        }

    protected final MultiScreenHandle findHandle( final AbstractScreen aScreen )
        {
        for ( int idx = 0; idx < myScreens.size(); idx++ )
            {
            final MultiScreenHandle handle = screenHandle( idx );
            if ( handle.screen == aScreen ) return handle;
            }
        throw new IllegalArgumentException();
        }


    private final Vector myScreens = new Vector();
    }
