package net.intensicode.util;

import net.intensicode.core.Color;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;



public final class SystemFontGen extends FontGen
{
    public SystemFontGen( final Font aSystemFont )
    {
        mySystemFont = aSystemFont;

        int maxDigitWidth = 0;
        for ( char idx = '0'; idx < '9'; idx++ )
        {
            maxDigitWidth = Math.max( maxDigitWidth, mySystemFont.charWidth( idx ) );
        }

        myMaxDigitWidth = maxDigitWidth;
    }

    // From FontGen

    public final int charHeight()
    {
        return mySystemFont.getHeight();
    }

    public final int charWidth( final char aCharCode )
    {
        return mySystemFont.charWidth( aCharCode );
    }

    public final int substringWidth( final String aString, final int aOffset, final int aLength )
    {
        return mySystemFont.substringWidth( aString, aOffset, aLength );
    }

    public void blitString( final Graphics aGC, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
    {
        aGC.setColor( Color.WHITE );
        aGC.setFont( mySystemFont );
        aGC.drawSubstring( aText, aStart, aEnd - aStart, aX, aY, GRAPHICS_TOP_LEFT );
    }

    public final void blitChar( final Graphics aGC, final int aX, final int aY, final int aAsciiCode )
    {
        aGC.setColor( Color.WHITE );
        aGC.setFont( mySystemFont );
        aGC.drawChar( ( char ) aAsciiCode, aX, aY, GRAPHICS_TOP_LEFT );
    }

    // Protected Interface

    protected final int maxCharWidth()
    {
        return myMaxDigitWidth;
    }

    protected final int maxDigitCharWidth()
    {
        return myMaxDigitWidth;
    }



    private final Font mySystemFont;

    private final int myMaxDigitWidth;

    private static final int GRAPHICS_TOP_LEFT = Graphics.TOP | Graphics.LEFT;
}
