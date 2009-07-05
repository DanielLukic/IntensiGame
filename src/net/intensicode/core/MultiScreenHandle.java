/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

/**
 * TODO: Describe this!
 */
final class MultiScreenHandle
{
    final AbstractScreen screen;

    boolean visible = true;



    MultiScreenHandle( final AbstractScreen aScreen )
    {
        screen = aScreen;
    }

    // From Object

    public final boolean equals( final Object aObject )
    {
        if ( super.equals( aObject ) ) return true;
        return screen.equals( aObject );
    }
}
