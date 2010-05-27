package net.intensicode.graphics;

import net.intensicode.core.*;


public final class CharGenerator
    {
    public static final CharGenerator NULL = new CharGenerator( NullImageResource.NULL, 1, 1 );

    public final int charWidth;

    public final int charHeight;

    public final int charsPerRow;

    public final int charsPerColumn;


    public static CharGenerator fromLayout( final ImageResource aCharBitmap, final int aCharsPerRow, final int aCharsPerColumn )
        {
        final int charWidth = aCharBitmap.getWidth() / aCharsPerRow;
        final int charHeight = aCharBitmap.getHeight() / aCharsPerColumn;
        return new CharGenerator( aCharBitmap, charWidth, charHeight );
        }

    public static CharGenerator fromSize( final ImageResource aCharBitmap, final int aCharWidth, final int aCharHeight )
        {
        return new CharGenerator( aCharBitmap, aCharWidth, aCharHeight );
        }

    public final int getNumberOfFrames()
        {
        return charsPerRow * charsPerColumn;
        }

    public final CharData getCharData( final int aCharCode )
        {
        if ( aCharCode < 0 || aCharCode >= myDataCache.length ) return getCharData( 0 );

        if ( myDataCache[ aCharCode ] == null )
            {
            final int column = aCharCode % charsPerRow;
            final int row = aCharCode / charsPerRow;

            final int x = column * charWidth;
            final int y = row * charHeight;
            myDataCache[ aCharCode ] = new CharData( myBitmap, x, y, charWidth, charHeight );
            }

        return myDataCache[ aCharCode ];
        }

    public final void blit( final DirectGraphics aGraphics, final int aX, final int aY, final int aCharCode )
        {
        final CharData data = getCharData( aCharCode );
        if ( data == null ) return;
        data.blit( aGraphics, aX, aY );
        }

    public final void blit( final DirectGraphics aGraphics, final int aX, final int aY, final int aCharCode, final int aWidth )
        {
        if ( aCharCode == 0 ) return;

        final CharData data = getCharData( aCharCode );
        if ( data == null ) return;
        data.blit( aGraphics, aX, aY, aWidth );
        }

    public final void blend( final DirectGraphics aGraphics, final int aX, final int aY, final int aCharCode, final int aWidth, final int aAlpha8 )
        {
        if ( aCharCode == 0 ) return;

        final CharData data = getCharData( aCharCode );
        if ( data == null ) return;
        data.blend( aGraphics, aX, aY, aWidth, aAlpha8 );
        }


    // Implementation

    private CharGenerator( final ImageResource aCharBitmap, final int aCharWidth, final int aCharHeight )
        {
        final int bitmapWidth = aCharBitmap.getWidth();
        final int bitmapHeight = aCharBitmap.getHeight();
        final int charsInRow = bitmapWidth / aCharWidth;
        final int charsInColumn = bitmapHeight / aCharHeight;

        //#if DEBUG
        if ( charsInRow * aCharWidth != bitmapWidth || charsInColumn * aCharHeight != bitmapHeight )
            {
            throw new IllegalArgumentException( "Char size mismatch" );
            }
        //#endif

        myBitmap = aCharBitmap;
        charWidth = aCharWidth;
        charHeight = aCharHeight;
        myDataCache = new CharData[charsInRow * charsInColumn];

        charsPerRow = charsInRow;
        charsPerColumn = charsInColumn;
        }


    private final CharData[] myDataCache;

    private final ImageResource myBitmap;
    }
