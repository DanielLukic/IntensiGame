package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Log;
import net.intensicode.util.Rectangle;

public final class GraphicsCommand
    {
    public static final int BLEND_IMAGE = 0;

    public static final int BLEND_IMAGE_RECT = 1;

    public static final int CHANGE_COLOR = 2;

    public static final int CHANGE_FONT = 3;

    public static final int CLEAR = 4;

    public static final int DRAW_CHAR = 5;

    public static final int DRAW_IMAGE = 6;

    public static final int DRAW_IMAGE_RECT = 7;

    public static final int DRAW_LINE = 8;

    public static final int DRAW_RECT = 9;

    public static final int DRAW_RGB = 10;

    public static final int DRAW_SUBSTRING = 11;

    public static final int FILL_RECT = 12;

    public static final int FILL_TRIANGLE = 13;

    public static final int INITIALIZE = 14;

    public static final int BEGIN_FRAME = 15;

    public static final int END_FRAME = 16;

    public static final int DESTROY = 17;

    public static final int NUMBER_OF_IDS = 18;

    public int id;

    public final GraphicsCommand initialize()
        {
        id = INITIALIZE;
        return this;
        }

    public final GraphicsCommand beginFrame()
        {
        id = BEGIN_FRAME;
        return this;
        }

    public final GraphicsCommand endFrame()
        {
        id = END_FRAME;
        return this;
        }

    public final GraphicsCommand destroy()
        {
        id = DESTROY;
        return this;
        }

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

    public final void execute( final DirectGraphics aGraphics ) throws Exception
        {
        switch ( id )
            {
            case INITIALIZE:
                Log.info( "EXECUTING INITIALIZE COMMAND" );
                aGraphics.initialize();
                break;
            case BEGIN_FRAME:
                aGraphics.beginFrame();
                break;
            case END_FRAME:
                aGraphics.endFrame();
                break;
            case DESTROY:
                Log.info( "EXECUTING CLEANUP COMMAND" );
                aGraphics.cleanup();
                break;
            case BLEND_IMAGE:
                aGraphics.blendImage( myImage, myX, myY, myAlpha256 );
                break;
            case BLEND_IMAGE_RECT:
                aGraphics.blendImage( myImage, mySourceRect, myX, myY, myAlpha256 );
                break;
            case CHANGE_COLOR:
                aGraphics.setColorARGB32( myColorARGB32 );
                break;
            case CHANGE_FONT:
                aGraphics.setFont( myFontResource );
                break;
            case CLEAR:
                aGraphics.clearARGB32( myColorARGB32 );
                break;
            case DRAW_CHAR:
                aGraphics.drawChar( myCharCode, myX, myY );
                break;
            case DRAW_IMAGE:
                aGraphics.drawImage( myImage, myX, myY, myAlignment );
                break;
            case DRAW_IMAGE_RECT:
                aGraphics.drawImage( myImage, mySourceRect, myX, myY );
                break;
            case DRAW_LINE:
                aGraphics.drawLine( myX1, myY1, myX2, myY2 );
                break;
            case DRAW_RECT:
                aGraphics.drawRect( myX, myY, myWidth, myHeight );
                break;
            case DRAW_RGB:
                aGraphics.drawRGB( myARGB32, myOffsetX, myScanlineSize, myX, myY, myWidth, myHeight, myUseAlpha );
                break;
            case DRAW_SUBSTRING:
                aGraphics.drawSubstring( myText, myStart, myEnd, myX, myY );
                break;
            case FILL_RECT:
                aGraphics.fillRect( myX, myY, myWidth, myHeight );
                break;
            case FILL_TRIANGLE:
                aGraphics.fillTriangle( myX1, myY1, myX2, myY2, myX3, myY3 );
                break;
            default:
                Log.debug( "unknown GraphicsCommand id: {}", id );
                break;
            }
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
    }
