package net.intensicode.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TODO: Describe this!
 */
public final class CharDataRGB
    {
    public final int width;

    public final int height;



    CharDataRGB( final int aCharWidth, final int aCharHeight )
        {
        width = aCharWidth;
        height = aCharHeight;
        myPixelData = new int[aCharWidth * aCharHeight];
        }

    CharDataRGB( final Image aCharBitmap, final int aX, final int aY, final int aCharWidth, final int aCharHeight )
        {
        this( aCharWidth, aCharHeight );

        // http://discussion.forum.nokia.com/forum/archive/index.php/t-43642.html

        //#if NOKIA
        //# final com.nokia.mid.ui.DirectGraphics gc = com.nokia.mid.ui.DirectUtils.getDirectGraphics( aCharBitmap.getGraphics() );
        //# gc.getPixels( myPixelData, 0, aCharWidth, aX, aY, aCharWidth, aCharHeight, com.nokia.mid.ui.DirectGraphics.TYPE_INT_8888_ARGB );
        //#else
        // This freezes on some S60 phones (6600, 6670 and probably some others). Known bug.
        aCharBitmap.getRGB( myPixelData, 0, aCharWidth, aX, aY, aCharWidth, aCharHeight );
        //#endif
        }

    public final int[] pixelData()
        {
        return myPixelData;
        }

    public final void blit( final Graphics aGC, final int aX, final int aY )
        {
        final int dx = aGC.getTranslateX();
        final int dy = aGC.getTranslateY();
        aGC.translate( -dx, -dy );
        aGC.drawRGB( myPixelData, 0, width, aX + dx, aY + dy, width, height, true );
        aGC.translate( dx, dy );
        }

    public final void blit( final Graphics aGC, final int aX, final int aY, final int aWidth )
        {
        final int dx = aGC.getTranslateX();
        final int dy = aGC.getTranslateY();
        aGC.translate( -dx, -dy );
        aGC.drawRGB( myPixelData, 0, width, aX + dx, aY + dy, aWidth, height, true );
        aGC.translate( dx, dy );
        }

    public final void blitScaled( final Graphics aGraphics, final int aX, final int aY, final int aWidth, final int aHeight )
        {
        updateBuffer( aWidth, aHeight );

        int idx = 0;
        for ( int y = 0; y < aHeight; y++ )
            {
            for ( int x = 0; x < aWidth; x++ )
                {
                final int xSrc = x * (width - 1) / (aWidth - 1);
                final int ySrc = y * (height - 1) / (aHeight - 1);
                myScaleBuffer[ idx++ ] = myPixelData[ xSrc + ySrc * width ];
                }
            }

        aGraphics.drawRGB( myScaleBuffer, 0, aWidth, aX, aY, aWidth, aHeight, true );
        }

    private static final void updateBuffer( final int aWidth, final int aHeight )
        {
        final int bufferSize = aWidth * aHeight;
        if ( myScaleBuffer == null ) myScaleBuffer = new int[bufferSize];
        if ( myScaleBuffer.length < bufferSize ) myScaleBuffer = new int[bufferSize];
        }



    private final int[] myPixelData;

    private static int[] myScaleBuffer;
    }