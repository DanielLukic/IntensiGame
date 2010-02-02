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

    // Protected Interface

    protected final int maxCharWidth()
        {
        return myMaxDigitWidth;
        }

    protected final int maxDigitCharWidth()
        {
        return myMaxDigitWidth;
        }



    private final int myMaxDigitWidth;

    private final FontResource mySystemFont;
    }
