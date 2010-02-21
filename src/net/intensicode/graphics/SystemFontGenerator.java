package net.intensicode.graphics;

import net.intensicode.core.*;


public final class SystemFontGenerator extends FontGenerator
    {
    public SystemFontGenerator( final FontResource aSystemFont )
        {
        mySystemFont = aSystemFont;

        int maxDigitWidth = 0;
        for ( char idx = '0'; idx < '9'; idx++ )
            {
            maxDigitWidth = Math.max( maxDigitWidth, mySystemFont.charWidth( idx ) );
            }
        myMaxDigitWidth = maxDigitWidth;

        int maxCharWidth = 0;
        for ( char idx = 32; idx < 128; idx++ )
            {
            maxCharWidth = Math.max( maxCharWidth, mySystemFont.charWidth( idx ) );
            }
        myMaxCharWidth = maxCharWidth;
        }

    // From FontGenerator

    public final int charHeight()
        {
        return mySystemFont.getHeight();
        }

    public final int charWidth( final char aCharCode )
        {
        return mySystemFont.charWidth( aCharCode );
        }

    public final int maxCharWidth()
        {
        return myMaxCharWidth;
        }

    public final int maxDigitCharWidth()
        {
        return myMaxDigitWidth;
        }

    public final int substringWidth( final String aString, final int aOffset, final int aLength )
        {
        return mySystemFont.substringWidth( aString, aOffset, aLength );
        }

    public void blitString( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        aGraphics.setFont( mySystemFont );
        aGraphics.drawSubstring( aText, aStart, aEnd, aX, aY );
        }

    public final void blitChar( final DirectGraphics aGraphics, final int aX, final int aY, final int aAsciiCode )
        {
        aGraphics.setFont( mySystemFont );
        aGraphics.drawChar( (char) aAsciiCode, aX, aY );
        }

    public final void blendChar( final DirectGraphics aGraphics, final int aX, final int aY, final char aAsciiCode, final int aAlpha8 )
        {
        final int rgb24 = aGraphics.getColorRGB24();
        aGraphics.setColorARGB32( aAlpha8 << 24 | rgb24 );
        aGraphics.drawChar( (char) aAsciiCode, aX, aY );
        }


    private final int myMaxCharWidth;

    private final int myMaxDigitWidth;

    private final FontResource mySystemFont;
    }
