package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class DrawCharCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final char aCharCode, final int aX, final int aY )
        {
        myCharCode = aCharCode;
        myX = aX;
        myY = aY;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.drawChar( myCharCode, myX, myY );
        }

    private char myCharCode;

    private int myX;

    private int myY;
    }
