package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;

import javax.microedition.lcdui.Graphics;



public final class ClearScreen extends AbstractScreen
{
    public ClearScreen( final int aARGB32 )
    {
        myARGB32 = aARGB32;
    }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine )
    {
    }

    public final void onDrawFrame( final DirectScreen aScreen )
    {
        final Graphics gc = aScreen.graphics();
        gc.setColor( myARGB32 );
        gc.fillRect( 0, 0, aScreen.width(), aScreen.height() );
    }

    private final int myARGB32;
}