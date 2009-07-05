/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.util.Hashtable;



public final class BitmapFontGen extends FontGen
    {
    public static boolean buffered = false;



    public static final void purgeCaches()
        {
        if ( myBlittedStrings.size() == 0 ) return;

        //#if DEBUG
        Log.debug( "Purging {} cached text strings", myBlittedStrings.size() );
        //#endif

        myBlittedStrings.clear();
        }

    public BitmapFontGen( final CharGen aCharGen )
        {
        myCharGen = aCharGen;
        myCharHeight = myCharGen.charHeight;

        if ( aCharGen.charWidth > 127 ) throw new RuntimeException( "nyi" );

        final byte width = (byte) aCharGen.charWidth;
        final int numberOfChars = aCharGen.charsPerRow * aCharGen.charsPerColumn;
        final byte[] charWidths = new byte[numberOfChars];
        for ( int idx = 0; idx < numberOfChars; idx++ )
            {
            charWidths[ idx ] = width;
            }
        myCharWidths = charWidths;
        }

    public BitmapFontGen( final CharGen aCharGen, final byte[] aWidths )
        {
        myCharGen = aCharGen;
        myCharWidths = aWidths;
        myCharHeight = myCharGen.charHeight;
        }

    // From FontGen

    public final int charHeight()
        {
        return myCharHeight;
        }

    public final int charWidth( final char aCharCode )
        {
        if ( aCharCode < MIN_ASCII_CODE || aCharCode - MIN_ASCII_CODE > myCharWidths.length )
            return 0;
        return myCharWidths[ aCharCode - MIN_ASCII_CODE ];
        }

    public final int substringWidth( final String aString, final int aOffset, final int aLength )
        {
        int length = 0;
        for ( int idx = aOffset; idx < aOffset + aLength; idx++ )
            {
            length += charWidth( aString.charAt( idx ) );
            }
        return length;
        }

    public final void blitChar( final Graphics aGC, final int aX, final int aY, final int aAsciiCode )
        {
        //#if NOKIA
        //# final int width = myCharWidths[ aAsciiCode - MIN_ASCII_CODE ];
        //# myCharGen.blit( aGC, aX, aY, aAsciiCode - MIN_ASCII_CODE, width );
        //#else
        myCharGen.blit( aGC, aX, aY, aAsciiCode - MIN_ASCII_CODE );
        //#endif
        }

    public final void blitString( final Graphics aGC, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        if ( aText.length() == 0 ) return;

        if ( buffered )
            {
            final String part = getPart( aText, aStart, aEnd );

            if ( myBlittedStrings.containsKey( part ) == false )
                {
                final Image buffer = createBufferedString( part );
                myBlittedStrings.put( part, buffer );
                }

            final Image buffer = (Image) myBlittedStrings.get( part );
            aGC.drawImage( buffer, aX, aY, TOP_LEFT );
            }
        else
            {
            myBlittedStrings.clear();

            int x = aX;
            for ( int idx = aStart; idx < aEnd; idx++ )
                {
                final char code = aText.charAt( idx );
                blitChar( aGC, x, aY, code );
                x += charWidth( code );
                }
            }
        }

    // Protected Interface

    protected final int maxCharWidth()
        {
        return myCharGen.charWidth;
        }

    protected final int maxDigitCharWidth()
        {
        return myCharGen.charWidth;
        }

    // Implementation

    private final String getPart( final String aText, final int aStart, final int aEnd )
        {
        if ( aStart == 0 && aEnd == aText.length() ) return aText;
        return aText.substring( aStart, aEnd );
        }

    private final Image createBufferedString( final String aPart )
        {
        //#if DEBUG
        Log.debug( "Buffering {}", aPart );
        //#endif

        final int partLength = aPart.length();

        // Fix for #84: Using MIN_BUFFER_WIDTH to avoid empty/black image instead of small character.
        // So far this happens only in the RunME version!?

        //#if RUNME
        final int bufferWidth = Math.max( MIN_BUFFER_WIDTH, stringWidth( aPart ) );
        //#else
        //# final int bufferWidth = stringWidth( aPart );
        //#endif

        final Image buffer = Image.createImage( bufferWidth, myCharHeight );
        final Graphics gc = buffer.getGraphics();
        gc.setColor( 0 );
        gc.fillRect( 0, 0, buffer.getWidth(), buffer.getHeight() );

        int x = 0;
        for ( int idx = 0; idx < partLength; idx++ )
            {
            final char code = aPart.charAt( idx );
            blitChar( gc, x, 0, code );
            x += charWidth( code );
            }

        return buffer;
        }



    private final int myCharHeight;

    private final CharGen myCharGen;

    private final byte[] myCharWidths;

    private static final Hashtable myBlittedStrings = new Hashtable();


    private static final int MIN_ASCII_CODE = 32;

    private static final int MIN_BUFFER_WIDTH = 16;
    }
