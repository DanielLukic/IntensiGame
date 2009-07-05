/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.path;

import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public final class DirectedPosition
{
    public final Position positionFixed = new Position();

    public final Position directionFixed = new Position();



    public DirectedPosition( final Position aPositionFixed )
    {
        positionFixed.setTo( aPositionFixed );
    }

    public DirectedPosition( final Position aPositionFixed, final Position aDirectionFixed )
    {
        this( aPositionFixed );
        directionFixed.setTo( aDirectionFixed );
    }
}
