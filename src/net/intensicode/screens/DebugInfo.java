package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public final class DebugInfo
    {
    public boolean active;

    public int x;

    public int y;

    public int number;

    public int numberAlign = FontGenerator.CENTER;

    public boolean showNumber;

    public String text;

    public int textAlign = FontGenerator.CENTER;

    public boolean showText;


    public final DebugInfo init( final int aX, final int aY, final int aNumber )
        {
        active = true;
        x = aX;
        y = aY;
        number = aNumber;
        showNumber = true;
        myActiveTicks = 1;
        return this;
        }

    public final DebugInfo init( final int aX, final int aY, final String aText )
        {
        active = true;
        x = aX;
        y = aY;
        text = aText;
        showText = true;
        myActiveTicks = 1;
        return this;
        }

    public final void setActiveTicks( final int aTicks )
        {
        myActiveTicks = aTicks;
        }

    public final void reset()
        {
        active = false;
        myActiveTicks = 0;
        showNumber = showText = false;
        }

    public final void onControlTick()
        {
        if ( !active ) return;

        if ( myActiveTicks > 0 ) myActiveTicks--;
        else reset();
        }

    public final void onDrawFrame( final DirectGraphics aGraphics, final FontGenerator aFont )
        {
        if ( !active ) return;

        theBlitPos.x = x;
        theBlitPos.y = y;

        if ( showNumber ) aFont.blitNumber( aGraphics, theBlitPos, number, numberAlign );
        if ( showText ) aFont.blitString( aGraphics, text, theBlitPos, textAlign );
        }


    private int myActiveTicks;

    private static final Position theBlitPos = new Position();
    }
