package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class DrawSubstringCommand implements GraphicsCommand
    {
    public final GraphicsCommand set( final String aText, final int aStart, final int aEnd, final int aX, final int aY )
        {
        myText = aText;
        myStart = aStart;
        myEnd = aEnd;
        myX = aX;
        myY = aY;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.drawSubstring( myText, myStart, myEnd, myX, myY );
        }

    private String myText;

    private int myStart;

    private int myEnd;

    private int myX;

    private int myY;
    }
