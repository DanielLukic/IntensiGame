package net.intensicode.path;

import net.intensicode.util.*;


public final class DirectedPosition
{
    public final PositionF position = new PositionF();

    public final PositionF direction = new PositionF();



    public DirectedPosition( final PositionF aPosition )
    {
        position.setTo( aPosition );
    }

    public DirectedPosition( final PositionF aPosition, final PositionF aDirection )
    {
        this( aPosition );
        direction.setTo( aDirection );
    }
}
