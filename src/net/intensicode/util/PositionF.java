package net.intensicode.util;

public class PositionF
    {
    public float x;

    public float y;


    public PositionF()
        {
        }

    public PositionF( final float aX, final float aY )
        {
        x = aX;
        y = aY;
        }

    public PositionF( final PositionF aPosition )
        {
        x = aPosition.x;
        y = aPosition.y;
        }

    public final void translate( final PositionF aPosition )
        {
        x += aPosition.x;
        y += aPosition.y;
        }

    public final void setTo( final PositionF aPosition )
        {
        x = aPosition.x;
        y = aPosition.y;
        }

    public final void scale( final float aValue )
        {
        x *= aValue;
        y *= aValue;
        }

    public final float distanceTo( final PositionF aPositionFixed )
        {
        return MathExtended.length( x - aPositionFixed.x, y - aPositionFixed.y );
        }

    public final void normalize()
        {
        final float length = MathExtended.length( x, y );
        if ( length == 0 ) return;
        x /= length;
        y /= length;
        }

    public final boolean validDirection()
        {
        return x != 0 || y != 0;
        }

    // From Object

    public final boolean equals( final Object aObject )
        {
        if ( !( aObject instanceof PositionF ) ) return false;
        final PositionF that = (PositionF) aObject;
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
