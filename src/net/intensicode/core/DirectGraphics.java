package net.intensicode.core;

import net.intensicode.util.*;

public abstract class DirectGraphics
    {
    public static final int FULLY_TRANSPARENT = 0;

    public static final int FULLY_OPAQUE = 255;

    public static final int ALIGN_DEFAULT = 0;

    public static final int ALIGN_TOP_LEFT = ALIGN_DEFAULT;

    public static final int ALIGN_TOP = ALIGN_DEFAULT;

    public static final int ALIGN_LEFT = ALIGN_DEFAULT;

    public static final int ALIGN_BOTTOM = 1;

    public static final int ALIGN_RIGHT = 2;

    public static final int ALIGN_HCENTER = 4;

    public static final int ALIGN_VCENTER = 8;

    public static final int ALIGN_CENTER = ALIGN_HCENTER | ALIGN_VCENTER;

    public static Position getAlignedPosition( final int aX, final int aY, final int aWidth, final int aHeight, final int aAlignment )
        {
        theAlignedXY.x = aX;
        theAlignedXY.y = aY;

        if ( ( aAlignment & ALIGN_HCENTER ) != 0 ) theAlignedXY.x -= aWidth / 2;
        else if ( ( aAlignment & ALIGN_RIGHT ) != 0 ) theAlignedXY.x -= aWidth;
        if ( ( aAlignment & ALIGN_VCENTER ) != 0 ) theAlignedXY.y -= aHeight / 2;
        else if ( ( aAlignment & ALIGN_BOTTOM ) != 0 ) theAlignedXY.y -= aHeight;

        return theAlignedXY;
        }

    public abstract int getColorRGB24();

    public abstract int getColorARGB32();

    public abstract void setColorRGB24( int aRGB24 );

    public abstract void setColorARGB32( int aARGB32 );

    public abstract void setFont( FontResource aFont );

    public abstract void clearRGB24( int aRGB24 );

    public abstract void clearARGB32( int aARGB32 );

    public abstract void drawLine( int aX1, int aY1, int aX2, int aY2 );

    public abstract void drawRect( int aX, int aY, int aWidth, int aHeight );

    public abstract void drawRGB( int[] aARGB32, int aOffsetX, int aScanlineSize, int aX, int aY, int aWidth, int aHeight, boolean aUseAlpha );

    public abstract void fillRect( int aX, int aY, int aWidth, int aHeight );

    public abstract void fillTriangle( int aX1, int aY1, int aX2, int aY2, int aX3, int aY3 );

    public abstract void blendImage( ImageResource aImage, int aX, int aY, int aAlpha256 );

    public abstract void blendImage( ImageResource aImage, Rectangle aSourceRect, int aX, int aY, int aAlpha256 );

    public abstract void drawImage( ImageResource aImage, int aX, int aY );

    public abstract void drawImage( ImageResource aImage, int aX, int aY, int aAlignment );

    public abstract void drawImage( ImageResource aImage, Rectangle aSourceRect, int aTargetX, int aTargetY );

    public abstract void drawSubstring( String aText, int aStart, int aEnd, int aX, int aY );

    public abstract void drawChar( char aCharCode, int aX, int aY );

    private static final Position theAlignedXY = new Position();
    }
