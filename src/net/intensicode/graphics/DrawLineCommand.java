package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class DrawLineCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final int aX1, final int aY1, final int aX2, final int aY2 )
        {
        myX1 = aX1;
        myY1 = aY1;
        myX2 = aX2;
        myY2 = aY2;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.drawLine( myX1, myY1, myX2, myY2 );
        }

    private int myX1;

    private int myY1;

    private int myX2;

    private int myY2;
    }
