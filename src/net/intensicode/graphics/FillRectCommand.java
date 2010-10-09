package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class FillRectCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        myX = aX;
        myY = aY;
        myWidth = aWidth;
        myHeight = aHeight;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.fillRect( myX, myY, myWidth, myHeight );
        }

    private int myX;

    private int myY;

    private int myWidth;

    private int myHeight;
    }
