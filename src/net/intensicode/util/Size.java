package net.intensicode.util;

public final class Size
    {
    public int width;

    public int height;

    public Size()
        {
        }

    public Size( int aWidth, int aHeight )
        {
        width = aWidth;
        height = aHeight;
        }

    public final void setTo( final int aWidth, final int aHeight )
        {
        width = aWidth;
        height = aHeight;
        }

    public final void setTo( final Size aSize )
        {
        width = aSize.width;
        height = aSize.height;
        }

    // From Object

    public final boolean equals( final Object aObject )
        {
        if ( !( aObject instanceof Size ) ) return false;
        final Size that = (Size) aObject;
        return this.width == that.width && this.height == that.height;
        }

    public final String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( width );
        buffer.append( 'x' );
        buffer.append( height );
        return buffer.toString();
        }
    }
