package net.intensicode.core;

import net.intensicode.util.Rectangle;

public final class NullDirectGraphics extends DirectGraphics
    {
    public static final DirectGraphics NULL = new NullDirectGraphics();

    private NullDirectGraphics()
        {
        }

    public int getColorRGB24()
        {
        return 0;
        }

    public int getColorARGB32()
        {
        return 0;
        }

    public void setColorRGB24( final int aRGB24 )
        {
        }

    public void setColorARGB32( final int aARGB32 )
        {
        }

    public void setFont( final FontResource aFont )
        {
        }

    public void clearRGB24( final int aRGB24 )
        {
        }

    public void clearARGB32( final int aARGB32 )
        {
        }

    public void drawLine( final int aX1, final int aY1, final int aX2, final int aY2 )
        {
        }

    public void drawRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        }

    public void drawRGB( final int[] aARGB32, final int aOffsetX, final int aScanlineSize, final int aX, final int aY, final int aWidth, final int aHeight, final boolean aUseAlpha )
        {
        }

    public void fillRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        }

    public void fillTriangle( final int aX1, final int aY1, final int aX2, final int aY2, final int aX3, final int aY3 )
        {
        }

    public void blendImage( final ImageResource aImage, final int aX, final int aY, final int aAlpha256 )
        {
        }

    public void blendImage( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY, final int aAlpha256 )
        {
        }

    public void drawImage( final ImageResource aImage, final int aX, final int aY )
        {
        }

    public void drawImage( final ImageResource aImage, final int aX, final int aY, final int aAlignment )
        {
        }

    public void drawImage( final ImageResource aImage, final Rectangle aSourceRect, final int aTargetX, final int aTargetY )
        {
        }

    public void drawSubstring( final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        }

    public void drawChar( final char aCharCode, final int aX, final int aY )
        {
        }

    public void beginFrame()
        {
        }

    public void endFrame()
        {
        }
    }
