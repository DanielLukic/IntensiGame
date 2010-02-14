package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;
import net.intensicode.util.*;

public abstract class FontGenerator
    {
    public static final int HCENTER = 1;

    public static final int VCENTER = 2;

    public static final int LEFT = 4;

    public static final int RIGHT = 8;

    public static final int TOP = 16;

    public static final int BOTTOM = 32;

    public static final int TOP_LEFT = TOP | LEFT;

    public static final int CENTER = HCENTER | VCENTER;


    public static Position getAlignedPosition( final Position aPosition, final int aWidth, final int aHeight, final int aAlignment )
        {
        myBlitPos.x = aPosition.x;
        myBlitPos.y = aPosition.y;

        if ( ( aAlignment & HCENTER ) != 0 ) myBlitPos.x -= aWidth / 2;
        else if ( ( aAlignment & RIGHT ) != 0 ) myBlitPos.x -= aWidth;
        if ( ( aAlignment & VCENTER ) != 0 ) myBlitPos.y -= aHeight / 2;
        else if ( ( aAlignment & BOTTOM ) != 0 ) myBlitPos.y -= aHeight;

        return myBlitPos;
        }

    public static int getNumberOfDigits( final int aNumber )
        {
        int digits = 1;
        int value = aNumber;
        while ( value > 9 )
            {
            digits++;
            value /= 10;
            }
        return digits;
        }

    public final int stringWidth( final String aString )
        {
        return substringWidth( aString, 0, aString.length() );
        }

    public final void blitString( final DirectGraphics aGraphics, final String aText, final Position aPosition, final int aAlignment )
        {
        final int textLength = aText.length();
        final Position alignedPosition = getAlignedPosition( aPosition, stringWidth( aText ), charHeight(), aAlignment );
        blitString( aGraphics, aText, 0, textLength, alignedPosition.x, alignedPosition.y );
        }

    public final void blitText( final DirectGraphics aGraphics, final String aText, final Rectangle aTextRect )
        {
        final int textLength = aText.length();

        int linePos = aTextRect.y;
        int start = 0;
        int end = start;
        do
            {
            final int nextSpaceOffset = aText.indexOf( " ", end + 1 );
            if ( nextSpaceOffset == -1 )
                {
                final int newWidth = substringWidth( aText, start, textLength - start );
                if ( newWidth <= aTextRect.width ) end = textLength;
                if ( start == end ) end = textLength;
                }
            else
                {
                final int newWidth = substringWidth( aText, start, nextSpaceOffset - start );
                if ( newWidth <= aTextRect.width )
                    {
                    end = nextSpaceOffset;
                    continue;
                    }
                }

            blitString( aGraphics, aText, start, end, aTextRect.x, linePos );
            linePos += charHeight();

            while ( end < textLength && aText.charAt( end ) == ' ' ) end++;

            start = end;
            }
        while ( end < textLength );
        }

    public final void blitNumber( final DirectGraphics aGraphics, final Position aPosition, final int aNumber, final int aAlignment )
        {
        final int digitWidth = maxDigitCharWidth();
        final int digits = getNumberOfDigits( aNumber );
        final Position alignedPosition = getAlignedPosition( aPosition, digits * digitWidth, charHeight(), aAlignment );

        int value = Math.abs( aNumber );
        int x = alignedPosition.x + digitWidth * ( digits - 1 );
        for ( int idx = 0; idx < digits; idx++ )
            {
            final int digit = value % 10;
            blitChar( aGraphics, x, alignedPosition.y, '0' + digit );
            x -= digitWidth;
            value /= 10;
            }
        if ( aNumber < 0 ) blitChar( aGraphics, x, alignedPosition.y, '-' );
        }

    public final void blitNumber( final DirectGraphics aGraphics, final Position aPosition, final int aNumber, final int aAlignment, final int aNumberOfDigits )
        {
        final int digitWidth = maxDigitCharWidth();
        final int digits = Math.max( getNumberOfDigits( aNumber ), aNumberOfDigits );
        final Position alignedPosition = getAlignedPosition( aPosition, digits * digitWidth, charHeight(), aAlignment );

        int value = Math.abs( aNumber );
        int x = alignedPosition.x + digitWidth * ( digits - 1 );
        for ( int idx = 0; idx < digits; idx++ )
            {
            final int digit = value % 10;
            blitChar( aGraphics, x, alignedPosition.y, '0' + digit );
            x -= digitWidth;
            value /= 10;
            }
        if ( aNumber < 0 ) blitChar( aGraphics, x, alignedPosition.y, '-' );
        }

    // Abstract Interface

    public abstract int charHeight();

    public abstract int charWidth( char aCharCode );

    public abstract int substringWidth( String aString, int aOffset, int aLength );

    public abstract void blitChar( DirectGraphics aGraphics, int aX, int aY, int aAsciiCode );

    public abstract void blitString( DirectGraphics aGraphics, String aText, int aStart, int aEnd, int aX, int aY );

    public abstract void blendChar( DirectGraphics aGraphics, int aX, int aY, char aAsciiCode, int aAlpha8 );

    // Protected Interface

    protected abstract int maxCharWidth();

    protected abstract int maxDigitCharWidth();

    private static final Position myBlitPos = new Position();
    }
