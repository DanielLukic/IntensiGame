package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Rectangle;

public final class BlendImageRectCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final ImageResource aImage, final Rectangle aSourceRect, final int aX, final int aY, final int aAlpha256 )
        {
        myImage = aImage;
        mySourceRect = aSourceRect;
        myX = aX;
        myY = aY;
        myAlpha256 = aAlpha256;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.blendImage( myImage, mySourceRect, myX, myY, myAlpha256 );
        }

    private ImageResource myImage;

    private Rectangle mySourceRect;

    private int myX;

    private int myY;

    private int myAlpha256;
    }
