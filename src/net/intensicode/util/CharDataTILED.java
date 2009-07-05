package net.intensicode.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TODO: Describe this!
 */
public final class CharDataTILED
    {
    public final int width;

    public final int height;



    CharDataTILED( final Image aCharBitmap, final int aX, final int aY, final int aCharWidth, final int aCharHeight )
        {
        width = aCharWidth;
        height = aCharHeight;
        myPixelData = new int[width * height];

        //#if NOKIA

        //# myCharBitmap = Image.createImage( aCharWidth, aCharHeight );
        //#
        //# final Graphics graphics = myCharBitmap.getGraphics();
        //# graphics.setColor( 0 );
        //# graphics.fillRect( 0, 0, aCharWidth, aCharHeight );
        //# graphics.drawImage( aCharBitmap, -aX, -aY, Graphics.TOP | Graphics.LEFT );
        //#
        //# final com.nokia.mid.ui.DirectGraphics gc = com.nokia.mid.ui.DirectUtils.getDirectGraphics( graphics );
        //# gc.getPixels( myPixelData, 0, aCharWidth, 0, 0, aCharWidth, aCharHeight, com.nokia.mid.ui.DirectGraphics.TYPE_INT_8888_ARGB );

        //#else

        // This freezes on some S60 phones (6600, 6670 and probably some others). Known bug:
        // http://discussion.forum.nokia.com/forum/archive/index.php/t-43642.html

        // On good phones, we take the original RGB data with alpha channel directly:
        aCharBitmap.getRGB( myPixelData, 0, aCharWidth, aX, aY, aCharWidth, aCharHeight );
        myCharBitmap = Image.createRGBImage( myPixelData, width, height, true );

        // This does not crash - but also does not work :) - on modern Nokia phones.
        // On these models the background will be white or black - but not transparent.
        // Could be this: http://discussion.forum.nokia.com/forum/showthread.php?t=100828

        //#endif
        }

    public final int[] pixelData()
        {
        return myPixelData;
        }

    public final void blit( final Graphics aGC, final int aX, final int aY )
        {
        aGC.drawImage( myCharBitmap, aX, aY, Graphics.TOP | Graphics.LEFT );
        }

    public final void blit( final Graphics aGC, final int aX, final int aY, final int aWidth )
        {
        final int x = aGC.getClipX();
        final int y = aGC.getClipY();
        final int width = aGC.getClipWidth();
        final int height = aGC.getClipHeight();
        aGC.setClip( aX, aY, aWidth, this.height );
        aGC.drawImage( myCharBitmap, aX, aY, Graphics.TOP | Graphics.LEFT );
        aGC.setClip( x, y, width, height );
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

    private final Image myCharBitmap;

    private static int[] myScaleBuffer;
    }