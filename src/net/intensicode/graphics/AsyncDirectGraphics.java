//#condition RENDER_ASYNC

package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.DynamicArray;
import net.intensicode.util.Rectangle;

public final class AsyncDirectGraphics extends DirectGraphics
    {
    public AsyncDirectGraphics( final AsyncRenderQueue aRenderQueue )
        {
        myRenderQueue = aRenderQueue;
        }

    public final void beginFrame() throws InterruptedException
        {
        myCurrentColorARGB32 = 0xabbadada;
        myCurrentFontResource = null;

        myCommandQueue = myRenderQueue.waitForCompletedQueue();
        freeQueuedObjects();
        }

    public final void endFrame()
        {
        myRenderQueue.postFilledQueue( myCommandQueue );
        }

    private void freeQueuedObjects()
        {
        myPooledCommands.addAll( myCommandQueue );
        myCommandQueue.clear();
        }

    public final int getColorRGB24()
        {
        return myColorARGB32 & 0x00ffffff;
        }

    public final int getColorARGB32()
        {
        return myColorARGB32;
        }

    public final void setColorRGB24( final int aRGB24 )
        {
        myColorARGB32 = 0xff000000 | ( aRGB24 & 0x00ffffff );
        }

    public final void setColorARGB32( final int aARGB32 )
        {
        myColorARGB32 = aARGB32;
        }

    private int myColorARGB32;

    public final void setFont( final FontResource aFont )
        {
        myFont = aFont;
        }

    private FontResource myFont;

    private void queue( final GraphicsCommand aCommand )
        {
        myCommandQueue.add( aCommand );
        }

    private long myCurrentColorARGB32;

    private void queueColorChangeIfNecessary( final int aColorARGB32 )
        {
        if ( myCurrentColorARGB32 == aColorARGB32 ) return;
        queue( command().changeColor( aColorARGB32 ) );
        myCurrentColorARGB32 = aColorARGB32;
        }

    private FontResource myCurrentFontResource;

    private void queueFontChangeIfNecessary( final FontResource aFontResource )
        {
        if ( myCurrentFontResource == aFontResource ) return;
        queue( command().changeFont( aFontResource ) );
        myCurrentFontResource = aFontResource;
        }

    public final void clearRGB24( final int aRGB24 )
        {
        queue( command().clear( 0xff000000 | ( aRGB24 & 0x00ffffff ) ) );
        }

    public final void clearARGB32( final int aARGB32 )
        {
        queue( command().clear( aARGB32 ) );
        }

    public final void drawLine( final int aX1, final int aY1, final int aX2, final int aY2 )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        queue( command().drawLine( aX1, aY1, aX2, aY2 ) );
        }

    public final void drawRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        queue( command().drawRect( aX, aY, aWidth, aHeight ) );
        }

    public final void drawRGB( final int[] aARGB32, final int aOffsetX, final int aScanlineSize, final int aX, final int aY, final int aWidth, final int aHeight, final boolean aUseAlpha )
        {
        queue( command().drawRGB( aARGB32, aOffsetX, aScanlineSize, aX, aY, aWidth, aHeight, aUseAlpha ) );
        }

    public final void fillRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        queue( command().fillRect( aX, aY, aWidth, aHeight ) );
        }

    public final void fillTriangle( final int aX1, final int aY1, final int aX2, final int aY2, final int aX3, final int aY3 )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        queue( command().fillTriangle( aX1, aY1, aX2, aY2, aX3, aY3 ) );
        }

    public final void blendImage( final ImageResource aImage, final int aX, final int aY, final int aAlpha256 )
        {
        queue( command().blendImage( aImage, aX, aY, aAlpha256 ) );
        }

    public final void blendImage( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY, final int aAlpha256 )
        {
        queue( command().blendImageRect( aImage, aSourceRect, aX, aY, aAlpha256 ) );
        }

    public final void drawImage( final ImageResource aImage, final int aX, final int aY )
        {
        queue( command().drawImage( aImage, aX, aY ) );
        }

    public final void drawImage( final ImageResource aImage, final int aX, final int aY, final int aAlignment )
        {
        queue( command().drawImage( aImage, aX, aY, aAlignment ) );
        }

    public final void drawImage( final ImageResource aImage, final Rectangle aSourceRect, final int aTargetX, final int aTargetY )
        {
        queue( command().drawImageRect( aImage, aSourceRect, aTargetX, aTargetY ) );
        }

    public final void drawSubstring( final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        queueFontChangeIfNecessary( myFont );
        queue( command().drawSubstring( aText, aStart, aEnd, aX, aY ) );
        }

    public final void drawChar( final char aCharCode, final int aX, final int aY )
        {
        queueFontChangeIfNecessary( myFont );
        queue( command().drawChar( aCharCode, aX, aY ) );
        }

    private GraphicsCommand command()
        {
        try
            {
            if ( myPooledCommands.size == 0 ) return new GraphicsCommand();
            return (GraphicsCommand) myPooledCommands.removeLast();
            }
        catch ( final Exception e )
            {
            throw new ChainedException( e );
            }
        }


    private DynamicArray myCommandQueue = new DynamicArray();

    private final DynamicArray myPooledCommands = new DynamicArray();

    private final AsyncRenderQueue myRenderQueue;
    }
