package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Log;
import net.intensicode.util.Rectangle;

public final class GraphicsCommand
    {
    public static final int BEGIN_FRAME = 14;

    public static final int END_FRAME = 15;

    public static final int NUMBER_OF_IDS = 16;

    public int id;

    public GraphicsCommand blendImage( final ImageResource aImage, final int aX, final int aY, final int aAlpha256 )
        {
        id = BLEND_IMAGE;
        myImage = aImage;
        myX = aX;
        myY = aY;
        myAlpha256 = aAlpha256;
        return this;
        }

    public final GraphicsCommand blendImageRect( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY, final int aAlpha256 )
        {
        id = BLEND_IMAGE_RECT;
        myImage = aImage;
        mySourceRect = aSourceRect;
        myX = aX;
        myY = aY;
        myAlpha256 = aAlpha256;
        return this;
        }

    public final GraphicsCommand changeColor( final int aColorARGB32 )
        {
        id = CHANGE_COLOR;
        myColorARGB32 = aColorARGB32;
        return this;
        }

    public final GraphicsCommand changeFont( final FontResource aFontResource )
        {
        id = CHANGE_FONT;
        myFontResource = aFontResource;
        return this;
        }

    public final GraphicsCommand clear( final int aColorARGB32 )
        {
        id = CLEAR;
        myColorARGB32 = aColorARGB32;
        return this;
        }

    public final GraphicsCommand drawChar( final char aCharCode, final int aX, final int aY )
        {
        id = DRAW_CHAR;
        myCharCode = aCharCode;
        myX = aX;
        myY = aY;
        return this;
        }

    public final GraphicsCommand drawImage( final ImageResource aImage, final int aX, final int aY )
        {
        return drawImage( aImage, aX, aY, DirectGraphics.ALIGN_TOP_LEFT );
        }

    public final GraphicsCommand drawImage( final ImageResource aImage, final int aX, final int aY, final int aAlignment )
        {
        id = DRAW_IMAGE;
        myImage = aImage;
        myX = aX;
        myY = aY;
        myAlignment = aAlignment;
        return this;
        }

    public final GraphicsCommand drawImageRect( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY )
        {
        id = DRAW_IMAGE_RECT;
        myImage = aImage;
        mySourceRect = aSourceRect;
        myX = aX;
        myY = aY;
        return this;
        }

    public final GraphicsCommand drawLine( final int aX1, final int aY1, final int aX2, final int aY2 )
        {
        id = DRAW_LINE;
        myX1 = aX1;
        myY1 = aY1;
        myX2 = aX2;
        myY2 = aY2;
        return this;
        }

    public final GraphicsCommand drawRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        id = DRAW_RECT;
        myX = aX;
        myY = aY;
        myWidth = aWidth;
        myHeight = aHeight;
        return this;
        }

    public final GraphicsCommand drawRGB( final int[] aARGB32, final int aOffsetX, final int aScanlineSize, final int aX, final int aY, final int aWidth, final int aHeight, final boolean aUseAlpha )
        {
        id = DRAW_RGB;
        myARGB32 = aARGB32;
        myOffsetX = aOffsetX;
        myScanlineSize = aScanlineSize;
        myX = aX;
        myY = aY;
        myWidth = aWidth;
        myHeight = aHeight;
        myUseAlpha = aUseAlpha;
        return this;
        }

    public final GraphicsCommand drawSubstring( final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        id = DRAW_SUBSTRING;
        myText = aText;
        myStart = aStart;
        myEnd = aEnd;
        myX = aX;
        myY = aY;
        return this;
        }

    public final GraphicsCommand fillRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        id = FILL_RECT;
        myX = aX;
        myY = aY;
        myWidth = aWidth;
        myHeight = aHeight;
        return this;
        }

    public final GraphicsCommand fillTriangle( final int aX1, final int aY1, final int aX2, final int aY2, final int aX3, final int aY3 )
        {
        id = FILL_TRIANGLE;
        myX1 = aX1;
        myY1 = aY1;
        myX2 = aX2;
        myY2 = aY2;
        myX3 = aX3;
        myY3 = aY3;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        switch ( id )
            {
            case BLEND_IMAGE:
                blendImage( aGraphics );
                break;
            case BLEND_IMAGE_RECT:
                blendImageRect( aGraphics );
                break;
            case CHANGE_COLOR:
                changeColor( aGraphics );
                break;
            case CHANGE_FONT:
                changeFont( aGraphics );
                break;
            case CLEAR:
                clear( aGraphics );
                break;
            case DRAW_CHAR:
                drawChar( aGraphics );
                break;
            case DRAW_IMAGE:
                drawImage( aGraphics );
                break;
            case DRAW_IMAGE_RECT:
                drawImageRect( aGraphics );
                break;
            case DRAW_LINE:
                drawLine( aGraphics );
                break;
            case DRAW_RECT:
                drawRect( aGraphics );
                break;
            case DRAW_RGB:
                drawRGB( aGraphics );
                break;
            case DRAW_SUBSTRING:
                drawSubstring( aGraphics );
                break;
            case FILL_RECT:
                fillRect( aGraphics );
                break;
            case FILL_TRIANGLE:
                fillTriangle( aGraphics );
                break;
            default:
                Log.debug( "unknown GraphicsCommand id: {}", id );
                break;
            }
        }

    private void blendImage( final DirectGraphics aGraphics )
        {
        aGraphics.blendImage( myImage, myX, myY, myAlpha256 );
        }

    private void blendImageRect( final DirectGraphics aGraphics )
        {
        aGraphics.blendImage( myImage, mySourceRect, myX, myY, myAlpha256 );
        }

    private void changeColor( final DirectGraphics aGraphics )
        {
        aGraphics.setColorARGB32( myColorARGB32 );
        }

    private void changeFont( final DirectGraphics aGraphics )
        {
        aGraphics.setFont( myFontResource );
        }

    private void clear( final DirectGraphics aGraphics )
        {
        aGraphics.clearARGB32( myColorARGB32 );
        }

    private void drawChar( final DirectGraphics aGraphics )
        {
        aGraphics.drawChar( myCharCode, myX, myY );
        }

    private void drawImage( final DirectGraphics aGraphics )
        {
        aGraphics.drawImage( myImage, myX, myY, myAlignment );
        }

    private void drawImageRect( final DirectGraphics aGraphics )
        {
        aGraphics.drawImage( myImage, mySourceRect, myX, myY );
        }

    private void drawLine( final DirectGraphics aGraphics )
        {
        aGraphics.drawLine( myX1, myY1, myX2, myY2 );
        }

    private void drawRect( final DirectGraphics aGraphics )
        {
        aGraphics.drawRect( myX, myY, myWidth, myHeight );
        }

    private void drawRGB( final DirectGraphics aGraphics )
        {
        aGraphics.drawRGB( myARGB32, myOffsetX, myScanlineSize, myX, myY, myWidth, myHeight, myUseAlpha );
        }

    private void drawSubstring( final DirectGraphics aGraphics )
        {
        aGraphics.drawSubstring( myText, myStart, myEnd, myX, myY );
        }

    private void fillRect( final DirectGraphics aGraphics )
        {
        aGraphics.fillRect( myX, myY, myWidth, myHeight );
        }

    private void fillTriangle( final DirectGraphics aGraphics )
        {
        aGraphics.fillTriangle( myX1, myY1, myX2, myY2, myX3, myY3 );
        }


    private ImageResource myImage;

    private int myX;

    private int myY;

    private int myAlpha256;

    private Rectangle mySourceRect;

    private int myColorARGB32;

    private FontResource myFontResource;

    private char myCharCode;

    private int myAlignment;

    private int myX1;

    private int myY1;

    private int myX2;

    private int myY2;

    private int myWidth;

    private int myHeight;

    private int[] myARGB32;

    private int myOffsetX;

    private int myScanlineSize;

    private boolean myUseAlpha;

    private String myText;

    private int myStart;

    private int myEnd;

    private int myX3;

    private int myY3;

    private static final int BLEND_IMAGE = 0;

    private static final int BLEND_IMAGE_RECT = 1;

    private static final int CHANGE_COLOR = 2;

    private static final int CHANGE_FONT = 3;

    private static final int CLEAR = 4;

    private static final int DRAW_CHAR = 5;

    private static final int DRAW_IMAGE = 6;

    private static final int DRAW_IMAGE_RECT = 7;

    private static final int DRAW_LINE = 8;

    private static final int DRAW_RECT = 9;

    private static final int DRAW_RGB = 10;

    private static final int DRAW_SUBSTRING = 11;

    private static final int FILL_RECT = 12;

    private static final int FILL_TRIANGLE = 13;
    }
