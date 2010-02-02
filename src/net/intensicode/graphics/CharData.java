package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Rectangle;

public final class CharData
    {
    public CharData( final ImageResource aCharSet, final int aX, final int aY, final int aCharWidth, final int aCharHeight )
        {
        myCharSet = aCharSet;
        myCharRect.x = aX;
        myCharRect.y = aY;
        myCharRect.width = aCharWidth;
        myCharRect.height = aCharHeight;
        }

    public final void blit( final DirectGraphics aGraphics, final int aX, final int aY )
        {
        aGraphics.drawImage( myCharSet, myCharRect, aX, aY );
        }

    public final void blit( final DirectGraphics aGraphics, final int aX, final int aY, final int aWidth )
        {
        final int originalWidth = myCharRect.width;
        myCharRect.width = aWidth;
        aGraphics.drawImage( myCharSet, myCharRect, aX, aY );
        myCharRect.width = originalWidth;
        }



    private final ImageResource myCharSet;

    private final Rectangle myCharRect = new Rectangle();
    }
