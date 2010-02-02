package net.intensicode.util;

public class Position
    {
    public int x;

    public int y;



    public Position()
        {
        }

    public Position( int aX, int aY )
        {
        x = aX;
        y = aY;
        }

    public Position( final Position aPosition )
        {
        x = aPosition.x;
        y = aPosition.y;
        }

    public final void translate( final Position aPosition )
        {
        x += aPosition.x;
        y += aPosition.y;
        }

    public final void setTo( final Position aPosition )
        {
        x = aPosition.x;
        y = aPosition.y;
        }

    public final void scaleFixed( final int aValueFixed )
        {
        x = FixedMath.mul( x, aValueFixed );
        y = FixedMath.mul( y, aValueFixed );
        }

    public final int distanceToFixed( final Position aPositionFixed )
        {
        return FixedMath.length( x - aPositionFixed.x, y - aPositionFixed.y );
        }

    public final void normalizeFixed()
        {
        final int length = FixedMath.length( x, y );
        if ( length == 0 ) return;
        x = FixedMath.div( x, length );
        y = FixedMath.div( y, length );
        }

    public final boolean validDirection()
        {
        return x != 0 || y != 0;
        }

    // From Object

    public final boolean equals( final Object aObject )
        {
        if ( !( aObject instanceof Position ) ) return false;
        final Position that = (Position) aObject;
        return x == that.x && y == that.y;
        }

    //#if DEBUG
    public final String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( x );
        buffer.append( ',' );
        buffer.append( y );
        return buffer.toString();
        }
    //#endif
    }
