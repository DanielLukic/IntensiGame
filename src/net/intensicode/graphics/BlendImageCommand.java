package net.intensicode.graphics;

import net.intensicode.core.*;

public final class BlendImageCommand implements GraphicsCommand
    {
    public GraphicsCommand set( final ImageResource aImage, final int aX, final int aY, final int aAlpha256 )
        {
        myImage = aImage;
        myX = aX;
        myY = aY;
        myAlpha256 = aAlpha256;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.blendImage( myImage, myX, myY, myAlpha256 );
        }

    private ImageResource myImage;

    private int myX;

    private int myY;

    private int myAlpha256;
    }
