package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Rectangle;

public final class DrawImageRectCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY )
        {
        myImage = aImage;
        mySourceRect = aSourceRect;
        myX = aX;
        myY = aY;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.drawImage( myImage, mySourceRect, myX, myY );
        }

    private ImageResource myImage;

    private Rectangle mySourceRect;

    private int myX;

    private int myY;
    }
