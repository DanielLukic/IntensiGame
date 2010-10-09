package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.DynamicArray;
import net.intensicode.util.Rectangle;

import java.util.Hashtable;

public final class AsyncDirectGraphics extends DirectGraphics
    {
    public AsyncDirectGraphics( final DynamicArray aRenderQueue )
        {
        myRenderQueue = aRenderQueue;

        myBlendImageCommand = registerCommand( "net.intensicode.graphics.BlendImageCommand" );
        myBlendImageRectCommand = registerCommand( "net.intensicode.graphics.BlendImageRectCommand" );
        myChangeColorCommand = registerCommand( "net.intensicode.graphics.ChangeColorCommand" );
        myChangeFontCommand = registerCommand( "net.intensicode.graphics.ChangeFontCommand" );
        myClearCommand = registerCommand( "net.intensicode.graphics.ClearCommand" );
        myDrawCharCommand = registerCommand( "net.intensicode.graphics.DrawCharCommand" );
        myDrawImageCommand = registerCommand( "net.intensicode.graphics.DrawImageCommand" );
        myDrawImageRectCommand = registerCommand( "net.intensicode.graphics.DrawImageRectCommand" );
        myDrawLineCommand = registerCommand( "net.intensicode.graphics.DrawLineCommand" );
        myDrawRectCommand = registerCommand( "net.intensicode.graphics.DrawRectCommand" );
        myDrawRgbCommand = registerCommand( "net.intensicode.graphics.DrawRgbCommand" );
        myDrawSubstringCommand = registerCommand( "net.intensicode.graphics.DrawSubstringCommand" );
        myFillRectCommand = registerCommand( "net.intensicode.graphics.FillRectCommand" );
        myFillTriangleCommand = registerCommand( "net.intensicode.graphics.FillTriangleCommand" );
        }

    private Class registerCommand( final String aClassName )
        {
        try
            {
            final Class loadedClass = Class.forName( aClassName );
            myPoolsById.put( loadedClass, new DynamicArray() );
            return loadedClass;
            }
        catch ( final Exception e )
            {
            throw new ChainedException( e );
            }
        }

    public final void beginFrame()
        {
        myCurrentColorARGB32 = 0xabbadada;
        myCurrentFontResource = null;

        try
            {
            waitForRenderQueueToBeEmpty();
            fillRenderQueue();
            switchQueues();
            freeQueuedObjects();
            }
        catch ( final InterruptedException e )
            {
            // Simply bail out here..
            }
        }

    public final void endFrame()
        {
        }

    private void waitForRenderQueueToBeEmpty() throws InterruptedException
        {
        synchronized ( myRenderQueue )
            {
            while ( !myRenderQueue.empty() ) myRenderQueue.wait();
            }
        }

    private void fillRenderQueue()
        {
        synchronized ( myRenderQueue )
            {
            myRenderQueue.addAll( myPrimaryQueue );
            myRenderQueue.notify();
            }
        }

    private void switchQueues()
        {
        final DynamicArray swappedQueue = myPrimaryQueue;
        myPrimaryQueue = mySecondaryQueue;
        mySecondaryQueue = swappedQueue;
        }

    private void freeQueuedObjects()
        {
        for ( int idx = 0; idx < myPrimaryQueue.size; idx++ )
            {
            final Object command = myPrimaryQueue.get( idx );
            final DynamicArray pool = (DynamicArray) myPoolsById.get( command.getClass() );
            pool.add( command );
            }
        myPrimaryQueue.clear();
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
        myPrimaryQueue.add( aCommand );
        }

    private long myCurrentColorARGB32;

    private void queueColorChangeIfNecessary( final int aColorARGB32 )
        {
        if ( myCurrentColorARGB32 == aColorARGB32 ) return;
        final ChangeColorCommand command = (ChangeColorCommand) getOrCreatePooledCommand( myChangeColorCommand );
        queue( command.set( aColorARGB32 ) );
        myCurrentColorARGB32 = aColorARGB32;
        }

    private FontResource myCurrentFontResource;

    private void queueFontChangeIfNecessary( final FontResource aFontResource )
        {
        if ( myCurrentFontResource == aFontResource ) return;
        final ChangeFontCommand command = (ChangeFontCommand) getOrCreatePooledCommand( myChangeFontCommand );
        queue( command.set( aFontResource ) );
        myCurrentFontResource = aFontResource;
        }

    public final void clearRGB24( final int aRGB24 )
        {
        final ClearCommand command = (ClearCommand) getOrCreatePooledCommand( myClearCommand );
        queue( command.set( 0xff000000 | ( aRGB24 & 0x00ffffff ) ) );
        }

    public final void clearARGB32( final int aARGB32 )
        {
        final ClearCommand command = (ClearCommand) getOrCreatePooledCommand( myClearCommand );
        queue( command.set( aARGB32 ) );
        }

    public final void drawLine( final int aX1, final int aY1, final int aX2, final int aY2 )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        final DrawLineCommand command = (DrawLineCommand) getOrCreatePooledCommand( myDrawLineCommand );
        queue( command.set( aX1, aY1, aX2, aY2 ) );
        }

    public final void drawRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        final DrawRectCommand command = (DrawRectCommand) getOrCreatePooledCommand( myDrawRectCommand );
        queue( command.set( aX, aY, aWidth, aHeight ) );
        }

    public final void drawRGB( final int[] aARGB32, final int aOffsetX, final int aScanlineSize, final int aX, final int aY, final int aWidth, final int aHeight, final boolean aUseAlpha )
        {
        final DrawRgbCommand command = (DrawRgbCommand) getOrCreatePooledCommand( myDrawRgbCommand );
        queue( command.set( aARGB32, aOffsetX, aScanlineSize, aX, aY, aWidth, aHeight, aUseAlpha ) );
        }

    public final void fillRect( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        final FillRectCommand command = (FillRectCommand) getOrCreatePooledCommand( myFillRectCommand );
        queue( command.set( aX, aY, aWidth, aHeight ) );
        }

    public final void fillTriangle( final int aX1, final int aY1, final int aX2, final int aY2, final int aX3, final int aY3 )
        {
        queueColorChangeIfNecessary( myColorARGB32 );
        final FillTriangleCommand command = (FillTriangleCommand) getOrCreatePooledCommand( myFillTriangleCommand );
        queue( command.set( aX1, aY1, aX2, aY2, aX3, aY3 ) );
        }

    public final void blendImage( final ImageResource aImage, final int aX, final int aY, final int aAlpha256 )
        {
        final BlendImageCommand command = (BlendImageCommand) getOrCreatePooledCommand( myBlendImageCommand );
        queue( command.set( aImage, aX, aY, aAlpha256 ) );
        }

    public final void blendImage( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY, final int aAlpha256 )
        {
        final BlendImageRectCommand command = (BlendImageRectCommand) getOrCreatePooledCommand( myBlendImageRectCommand );
        queue( command.set( aImage, aSourceRect, aX, aY, aAlpha256 ) );
        }

    public final void drawImage( final ImageResource aImage, final int aX, final int aY )
        {
        final DrawImageCommand command = (DrawImageCommand) getOrCreatePooledCommand( myDrawImageCommand );
        queue( command.set( aImage, aX, aY ) );
        }

    public final void drawImage( final ImageResource aImage, final int aX, final int aY, final int aAlignment )
        {
        final DrawImageCommand command = (DrawImageCommand) getOrCreatePooledCommand( myDrawImageCommand );
        queue( command.set( aImage, aX, aY, aAlignment ) );
        }

    public final void drawImage( final ImageResource aImage, final Rectangle aSourceRect, final int aTargetX, final int aTargetY )
        {
        final DrawImageRectCommand command = (DrawImageRectCommand) getOrCreatePooledCommand( myDrawImageRectCommand );
        queue( command.set( aImage, aSourceRect, aTargetX, aTargetY ) );
        }

    public final void drawSubstring( final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        queueFontChangeIfNecessary( myFont );
        final DrawSubstringCommand command = (DrawSubstringCommand) getOrCreatePooledCommand( myDrawSubstringCommand );
        queue( command.set( aText, aStart, aEnd, aX, aY ) );
        }

    public final void drawChar( final char aCharCode, final int aX, final int aY )
        {
        queueFontChangeIfNecessary( myFont );
        final DrawCharCommand command = (DrawCharCommand) getOrCreatePooledCommand( myDrawCharCommand );
        queue( command.set( aCharCode, aX, aY ) );
        }

    private Object getOrCreatePooledCommand( final Class aCommandClass )
        {
        try
            {
            final DynamicArray poolById = (DynamicArray) myPoolsById.get( aCommandClass );
            if ( poolById.size == 0 ) return aCommandClass.newInstance();
            return poolById.removeLast();
            }
        catch ( final Exception e )
            {
            throw new ChainedException( e );
            }
        }

    private Class myBlendImageCommand;

    private Class myBlendImageRectCommand;

    private Class myChangeColorCommand;

    private Class myChangeFontCommand;

    private Class myClearCommand;

    private Class myDrawCharCommand;

    private Class myDrawImageCommand;

    private Class myDrawImageRectCommand;

    private Class myDrawLineCommand;

    private Class myDrawRectCommand;

    private Class myDrawRgbCommand;

    private Class myDrawSubstringCommand;

    private Class myFillRectCommand;

    private Class myFillTriangleCommand;

    private DynamicArray myPrimaryQueue = new DynamicArray();

    private DynamicArray mySecondaryQueue = new DynamicArray();

    private final Hashtable myPoolsById = new Hashtable();

    private final DynamicArray myRenderQueue;
    }
