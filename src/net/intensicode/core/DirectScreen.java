/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import net.intensicode.util.ImageUtils;
import net.intensicode.util.Size;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;



/**
 * TODO: Describe this!
 */
//#if MIDP2
public final class DirectScreen extends GameCanvas
//#else
//# public final class DirectScreen extends Canvas
//#endif
    {
    public static int scaleToFitPixelMargin = 8;

    public static boolean scaleToFit = false;

    boolean engineShouldPause = false;

    boolean visible = false;



    DirectScreen( final Keys aKeys, final Size aTargetSize )
        {
        //#if MIDP2
        super( false );
        setFullScreenMode( true );
        //#endif

        myKeys = aKeys;
        myTargetSize = aTargetSize;
        }

    final void clearAndUpdate()
        {
        final Graphics gc = graphics();
        clearGC( gc, getWidth(), getHeight() );
        update();
        }

    final void start()
        {
        myBufferGC = null;

        //scaleToFit = shouldScaleToFit();

        clearGC( graphics(), width(), height() );
        }

    final void update()
        {
        if ( visible == false ) return;

        if ( myBufferGC != null )
            {
            if ( scaleToFit && myOffscreenBuffer != null )
                {
                ImageUtils.scaleInto( myBufferGC, getWidth(), getHeight(), myOffscreenBuffer );
                }
            flushGraphics();
            }

        if ( width() == getWidth() && height() == getHeight() ) scaleToFit = false;
        }

    // From DirectScreen

    public final boolean shouldScaleToFit()
        {
        final int xLimit = getWidth() + scaleToFitPixelMargin;
        final int yLimit = getHeight() + scaleToFitPixelMargin;
        return width() > xLimit || height() > yLimit;
        }

    public final int getTargetWidth()
        {
        return myTargetSize.width;
        }

    public final int getTargetHeight()
        {
        return myTargetSize.height;
        }

    public final int width()
        {
        if ( myTargetSize.width == 0 ) return getWidth();
        return myTargetSize.width;
        }

    public final int height()
        {
        if ( myTargetSize.width == 0 ) return getHeight();
        return myTargetSize.height;
        }

    public final Graphics graphics()
        {
        final int realWidth = getWidth();
        final int realHeight = getHeight();
        final int width = width();
        final int height = height();

        // We need the real GC anyway. So let's make sure we have it..
        if ( myBufferGC == null )
            {
            myBufferGC = getGraphics();
            clearGC( myBufferGC, realWidth, realHeight );
            }

        if ( scaleToFit )
            {
            if ( myOffscreenBuffer == null || myOffscreenGC == null )
                {
                myOffscreenBuffer = Image.createImage( width, height );
                myOffscreenGC = myOffscreenBuffer.getGraphics();

                clearGC( myOffscreenGC, width, height );
                resetGC( myBufferGC, 0, 0, realWidth, realHeight );
                clearGC( myBufferGC, realWidth, realHeight );
                }

            // If we use scaleToFit, the offscreen buffer GC is the right one to return here:
            return myOffscreenGC;
            }
        else
            {
            if ( myOffscreenBuffer != null )
                {
                // We need to clear the real buffer..
                clearGC( myBufferGC, realWidth, realHeight );

                //// We need to reset the real buffer..
                //final int xOffset = (realWidth - width) / 2;
                //final int yOffset = (realHeight - height) / 2;
                //resetGC( myBufferGC, xOffset, yOffset, width, height );
                }

            myOffscreenBuffer = null;
            myOffscreenGC = null;
            }

        final int xOffset = ( realWidth - width ) / 2; // align center
        final int yOffset = ( realHeight - height ) / 2; // align bottom
        resetGC( myBufferGC, xOffset, yOffset, width, height );

        return myBufferGC;
        }

    // From Canvas

    protected final void hideNotify()
        {
        super.hideNotify();
        visible = false;
        engineShouldPause = true;
        }

    protected final void showNotify()
        {
        super.showNotify();
        visible = true;
        }

    protected void sizeChanged( final int aWidth, final int aHeight )
        {
        super.sizeChanged( aWidth, aHeight );
        start();
        update();
        }

    public final void keyPressed( final int aCode )
        {
        final int gameAction = getGameAction( aCode );
        myKeys.keyPressed( aCode, gameAction );
        }

    //#if NO_KEY_REPEAT

    public final void keyRepeated( final int i )
        {
        }

    //#endif

    public final void keyReleased( final int aCode )
        {
        final int gameAction = getGameAction( aCode );
        myKeys.keyReleased( aCode, gameAction );
        }

    // Implementation

    private static final void resetGC( final Graphics aGC, final int aOffsetX, final int aOffsetY, final int aWidth, final int aHeight )
        {
        aGC.translate( -aGC.getTranslateX(), -aGC.getTranslateY() );
        aGC.translate( aOffsetX, aOffsetY );
        aGC.setClip( 0, 0, aWidth, aHeight );
        }

    private static final void clearGC( final Graphics aGC, final int aWidth, final int aHeight )
        {
        aGC.translate( -aGC.getTranslateX(), -aGC.getTranslateY() );
        aGC.setColor( 0 );
        aGC.fillRect( 0, 0, aWidth, aHeight );
        }



    private Graphics myBufferGC;

    private Graphics myOffscreenGC;

    private Image myOffscreenBuffer;

    private final Keys myKeys;

    private final Size myTargetSize;
    }
