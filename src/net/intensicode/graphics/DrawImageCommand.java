package net.intensicode.graphics;

import net.intensicode.core.*;

public final class DrawImageCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final ImageResource aImage, final int aX, final int aY )
        {
        return set( aImage, aX, aY, DirectGraphics.ALIGN_TOP_LEFT );
        }

    public final GraphicsCommand set( final ImageResource aImage, final int aX, final int aY, final int aAlignment )
        {
        myImage = aImage;
        myX = aX;
        myY = aY;
        myAlignment = aAlignment;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.drawImage( myImage, myX, myY, myAlignment );
        }

    private ImageResource myImage;

    private int myX;

    private int myY;

    private int myAlignment;
    }
