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
    }
