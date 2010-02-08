package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Log;

import java.util.Hashtable;

public final class BitmapFontGenerator extends FontGenerator
    {
    public static ResourcesManager resources;

    public static boolean buffered;


    public static void purgeCaches()
        {
        if ( myBlittedStrings.size() == 0 ) return;

        //#if DEBUG
        Log.debug( "Purging {} cached text strings", myBlittedStrings.size() );
        //#endif

        myBlittedStrings.clear();
        }

    public BitmapFontGenerator( final CharGenerator aCharGen )
        {
        myCharGen = aCharGen;
        myCharHeight = myCharGen.charHeight;

        // Because of the byte-sized width array:
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

    public BitmapFontGenerator( final CharGenerator aCharGen, final byte[] aWidths )
        {
        myCharGen = aCharGen;
        myCharWidths = aWidths;
        myCharHeight = myCharGen.charHeight;
        }

    // From FontGenerator

    public final int charHeight()
        {
        return myCharHeight;
        }

    public final int charWidth( final char aCharCode )
        {
        if ( aCharCode < MIN_ASCII_CODE || aCharCode - MIN_ASCII_CODE > myCharWidths.length ) return 0;
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

    public final void blitChar( final DirectGraphics aGraphics, final int aX, final int aY, final int aAsciiCode )
        {
        final int index = aAsciiCode - MIN_ASCII_CODE;
        if ( index < 0 || index >= myCharWidths.length ) return;

        final int width = myCharWidths[ index ];
        myCharGen.blit( aGraphics, aX, aY, index, width );
        }

    public final void blitString( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        if ( aText.length() == 0 ) return;

        if ( buffered )
            {
            blitStringBuffered( aGraphics, aText, aStart, aEnd, aX, aY );
            }
        else
            {
            blitStringUnbuffered( aGraphics, aText, aStart, aEnd, aX, aY );
            }

        if ( !buffered ) myBlittedStrings.clear();
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

    private void blitStringBuffered( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        // TODO: Can the object allocation in #getPart be avoided somehow?
        final String part = getPart( aText, aStart, aEnd );

        if ( !myBlittedStrings.containsKey( part ) )
            {
            final ImageResource buffer = createBufferedString( part );
            myBlittedStrings.put( part, buffer );
            }

        final ImageResource buffer = (ImageResource) myBlittedStrings.get( part );
        aGraphics.drawImage( buffer, aX, aY );
        }

    private void blitStringUnbuffered( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        int x = aX;
        for ( int idx = aStart; idx < aEnd; idx++ )
            {
            final char code = aText.charAt( idx );
            blitChar( aGraphics, x, aY, code );
            x += charWidth( code );
            }
        }

    private String getPart( final String aText, final int aStart, final int aEnd )
        {
        if ( aStart == 0 && aEnd == aText.length() ) return aText;
        return aText.substring( aStart, aEnd );
        }

    private ImageResource createBufferedString( final String aPart )
        {
        if ( aPart == null ) return getEmptyImage();

        //#if DEBUG
        Log.debug( "Buffering {}", aPart );
        //#endif

        // Fix for #84: Using MIN_BUFFER_WIDTH to avoid empty/black image instead of small character.
        // So far this happens only in the RunME version!?

        //#if RUNME
        final int bufferWidth = Math.max( MIN_BUFFER_WIDTH, stringWidth( aPart ) );
        //#else
        //# final int bufferWidth = stringWidth( aPart );
        //#endif

        final ImageResource buffer = resources.createImageResource( bufferWidth, myCharHeight );
        final DirectGraphics gc = buffer.getGraphics();
        gc.clearRGB24( 0 );

        final int partLength = aPart.length();
        blitStringUnbuffered( gc, aPart, 0, partLength, 0, 0 );

        return buffer;
        }

    private ImageResource getEmptyImage()
        {
        if ( myEmptyImage == null ) myEmptyImage = resources.createImageResource( 4, 4 );
        return myEmptyImage;
        }


    private final int myCharHeight;

    private final byte[] myCharWidths;

    private final CharGenerator myCharGen;

    private static ImageResource myEmptyImage;

    private static final Hashtable myBlittedStrings = new Hashtable();


    private static final int MIN_ASCII_CODE = 32;

    private static final int MIN_BUFFER_WIDTH = 16;
    }
