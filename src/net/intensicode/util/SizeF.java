package net.intensicode.util;

public final class SizeF
    {
    public float width;

    public float height;

    public SizeF()
        {
        }

    public SizeF( float aWidth, float aHeight )
        {
        width = aWidth;
        height = aHeight;
        }

    public final void setTo( final float aWidth, final float aHeight )
        {
        width = aWidth;
        height = aHeight;
        }

    public final void setTo( final SizeF aSize )
        {
        width = aSize.width;
        height = aSize.height;
        }
    }
