package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;

import javax.microedition.lcdui.Graphics;



/**
 * TODO: Describe this!
 */
public final class ColorScreen extends AbstractScreen
{
    public int color_rgb24;



    public ColorScreen()
    {
        this( 0x000000 );
    }

    public ColorScreen( final int aRGB24 )
    {
        color_rgb24 = aRGB24;
    }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine ) throws Exception
    {
    }

    public final void onDrawFrame( final DirectScreen aScreen )
    {
        final Graphics graphics = aScreen.graphics();
        graphics.setColor( color_rgb24 );
        graphics.fillRect( 0, 0, aScreen.width(), aScreen.height() );
    }
}
