package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class FillTriangleCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final int aX1, final int aY1, final int aX2, final int aY2, final int aX3, final int aY3 )
        {
        myX1 = aX1;
        myY1 = aY1;
        myX2 = aX2;
        myY2 = aY2;
        myX3 = aX3;
        myY3 = aY3;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.fillTriangle( myX1, myY1, myX2, myY2, myX3, myY3 );
        }

    private int myX1;

    private int myY1;

    private int myX2;

    private int myY2;

    private int myX3;

    private int myY3;
    }
