package net.intensicode.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TODO: Describe this!
 */
public final class CharData
    {
    public final int width;

    public final int height;



    CharData( final Image aCharSet, final int aX, final int aY, final int aCharWidth, final int aCharHeight )
        {
        width = aCharWidth;
        height = aCharHeight;

        myCharImage = Image.createImage( aCharSet, aX, aY, width, height, 0 );
        }

    public final void blit( final Graphics aGC, final int aX, final int aY )
        {
        aGC.drawImage( myCharImage, aX, aY, Graphics.TOP | Graphics.LEFT );
        }

    public final void blit( final Graphics aGC, final int aX, final int aY, final int aWidth )
        {
        final int x = aGC.getClipX();
        final int y = aGC.getClipY();
        final int width = aGC.getClipWidth();
        final int height = aGC.getClipHeight();
        aGC.setClip( aX, aY, aWidth, this.height );
        aGC.drawImage( myCharImage, aX, aY, Graphics.TOP | Graphics.LEFT );
        aGC.setClip( x, y, width, height );
        }



    private final Image myCharImage;
    }