package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class DrawRgbCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final int[] aARGB32, final int aOffsetX, final int aScanlineSize, final int aX, final int aY, final int aWidth, final int aHeight, final boolean aUseAlpha )
        {
        myARGB32 = aARGB32;
        myOffsetX = aOffsetX;
        myScanlineSize = aScanlineSize;
        myX = aX;
        myY = aY;
        myWidth = aWidth;
        myHeight = aHeight;
        myUseAlpha = aUseAlpha;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.drawRGB( myARGB32, myOffsetX, myScanlineSize, myX, myY, myWidth, myHeight, myUseAlpha );
        }

    private int[] myARGB32;

    private int myOffsetX;

    private int myScanlineSize;

    private int myX;

    private int myY;

    private int myWidth;

    private int myHeight;

    private boolean myUseAlpha;
    }
