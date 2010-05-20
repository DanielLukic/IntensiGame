package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Log;

import java.util.*;

public final class BitmapFontGenerator extends FontGenerator
    {
    public static ResourcesManager resources;

    public static boolean buffered = false;

    public static int maxBufferWidth = 256;

    public static int maxBufferHeight = 64;


    public static void apply( final Configuration aConfiguration )
        {
        buffered = aConfiguration.readBoolean( "BitmapFontGenerator.buffered", buffered );
        maxBufferWidth = aConfiguration.readInt( "BitmapFontGenerator.maxBufferWidth", maxBufferWidth );
        maxBufferHeight = aConfiguration.readInt( "BitmapFontGenerator.maxBufferHeight", maxBufferHeight );
        }

    public static void purgeCaches()
        {
        if ( myBlittedStrings.size() == 0 ) return;

        //#if DEBUG
        Log.debug( "Purging {} cached text strings", myBlittedStrings.size() );
        //#endif

        final Enumeration bufferImages = myBlittedStrings.elements();
        while ( bufferImages.hasMoreElements() )
            {
            final ImageResource bufferImage = (ImageResource) bufferImages.nextElement();
            bufferImage.purge();
            }
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

    public final int maxCharWidth()
        {
        return myCharGen.charWidth;
        }

    public final int maxDigitCharWidth()
        {
        return myCharGen.charWidth;
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

    public final void blendChar( final DirectGraphics aGraphics, final int aX, final int aY, final char aAsciiCode, final int aAlpha8 )
        {
        final int index = aAsciiCode - MIN_ASCII_CODE;
        if ( index < 0 || index >= myCharWidths.length ) return;

        final int width = myCharWidths[ index ];
        myCharGen.blend( aGraphics, aX, aY, index, width, aAlpha8 );
        }

    public final void blitString( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        if ( aText == null || aText.length() == 0 ) return;

        if ( buffered && isBufferingAllowedFor( aText, aStart, aEnd ) )
            {
            blitStringBuffered( aGraphics, aText, aStart, aEnd, aX, aY );
            }
        else
            {
            blitStringUnbuffered( aGraphics, aText, aStart, aEnd, aX, aY );
            }

        if ( !buffered ) purgeCaches();
        }

    public final void blendString( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY, final int aAlpha256 )
        {
        if ( aText == null || aText.length() == 0 ) return;

        int x = aX;
        for ( int idx = aStart; idx < aEnd; idx++ )
            {
            final char code = aText.charAt( idx );
            blendChar( aGraphics, x, aY, code, aAlpha256 );
            x += charWidth( code );
            }
        }

    // Implementation

    private boolean isBufferingAllowedFor( final String aText, final int aStart, final int aEnd )
        {
        if ( resources == null ) return false;

        final int width = substringWidth( aText, aStart, aEnd - aStart );
        if ( width > maxBufferWidth ) return false;

        final int height = charHeight();
        if ( height > maxBufferHeight ) return false;

        final int maxSize = resources.maxImageResourceSize();
        return width <= maxSize && height <= maxSize;
        }

    private void blitStringBuffered( final DirectGraphics aGraphics, final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        // TODO: Can the object allocation in #getPart be avoided somehow? It is required as the hash-key here..
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
        if ( aPart == null ) return getSharedEmptyImage();

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

        if ( bufferWidth == 0 ) return getSharedEmptyImage();

        final ImageResource buffer = resources.createImageResource( bufferWidth, myCharHeight );
        final DirectGraphics gc = buffer.getGraphics();
        //#if J2ME
        // J2ME will create images with white background. We don't want this here!
        //#if DROIDSHOCK
        gc.clearRGB24( 0x95c03c );
        //#else
        //# gc.clearRGB24( 0 );
        //#endif
        //#endif

        final int partLength = aPart.length();
        blitStringUnbuffered( gc, aPart, 0, partLength, 0, 0 );

        return buffer;
        }

    private ImageResource getSharedEmptyImage()
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
