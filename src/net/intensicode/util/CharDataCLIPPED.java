package net.intensicode.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TODO: Describe this!
 */
public final class CharDataCLIPPED
    {
    public final int width;

    public final int height;



    CharDataCLIPPED( final Image aCharSet, final int aX, final int aY, final int aCharWidth, final int aCharHeight )
        {
        width = aCharWidth;
        height = aCharHeight;

        myPixelData = new int[width * height];

        //#if NOKIA
        //# myCharImage = com.nokia.mid.ui.DirectUtils.createImage( width, height, 0 );
        //# myOffsetX = 0;
        //# myOffsetY = 0;
        //#
        //# final Graphics graphics = myCharImage.getGraphics();
        //# // graphics.setColor( 0 );
        //# // graphics.fillRect( 0, 0, aCharWidth, aCharHeight );
        //# graphics.drawImage( aCharSet, -aX, -aY, Graphics.TOP | Graphics.LEFT );
        //#
        //# final com.nokia.mid.ui.DirectGraphics gc = com.nokia.mid.ui.DirectUtils.getDirectGraphics( graphics );
        //# gc.getPixels( myPixelData, 0, aCharWidth, 0, 0, aCharWidth, aCharHeight, com.nokia.mid.ui.DirectGraphics.TYPE_INT_8888_ARGB );
        //#else
        myCharImage = aCharSet;
        myOffsetX = aX;
        myOffsetY = aY;

        aCharSet.getRGB( myPixelData, 0, width, aX, aY, width, height );
        //#endif
        }

    public final int[] pixelData()
        {
        return myPixelData;
        }

    public final void blit( final Graphics aGC, final int aX, final int aY )
        {
        final int x = aGC.getClipX();
        final int y = aGC.getClipY();
        final int width = aGC.getClipWidth();
        final int height = aGC.getClipHeight();
        aGC.setClip( aX, aY, this.width, this.height );
        aGC.drawImage( myCharImage, aX - myOffsetX, aY - myOffsetY, Graphics.TOP | Graphics.LEFT );
        aGC.setClip( x, y, width, height );
        }

    public final void blit( final Graphics aGC, final int aX, final int aY, final int aWidth )
        {
        final int x = aGC.getClipX();
        final int y = aGC.getClipY();
        final int width = aGC.getClipWidth();
        final int height = aGC.getClipHeight();
        aGC.setClip( aX, aY, aWidth, this.height );
        aGC.drawImage( myCharImage, aX - myOffsetX, aY - myOffsetY, Graphics.TOP | Graphics.LEFT );
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
                final int xSrc = x * ( width - 1 ) / ( aWidth - 1 );
                final int ySrc = y * ( height - 1 ) / ( aHeight - 1 );
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



    private final int myOffsetX;

    private final int myOffsetY;

    private final Image myCharImage;

    private final int[] myPixelData;

    private static int[] myScaleBuffer;
    }