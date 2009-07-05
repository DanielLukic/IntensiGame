//#condition CONSOLE

package net.intensicode.core;

import net.intensicode.util.DynamicArray;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;

import javax.microedition.lcdui.Graphics;

public final class ConsoleOverlay extends AbstractScreen
    {
    public static boolean show = true;



    public static final void addMessage( final String aMessage )
        {
        theMessages.insert( 0, new ConsoleEntry( aMessage ) );
        }

    public ConsoleOverlay( final FontGen aFontGen )
        {
        myFontGen = aFontGen;
        }

    // From AbstractScreen

    public final void onInitOnce( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        }

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        for ( int idx = theMessages.size - 1; idx >= 0; idx-- )
            {
            final ConsoleEntry entry = (ConsoleEntry) theMessages.get( idx );
            if ( --entry.tickCounter == 0 ) theMessages.remove( idx );
            }
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        if ( show == false ) return;

        final int width = aScreen.width();
        final int height = aScreen.height();
        final int charHeight = myFontGen.charHeight();

        final int linesOnScreen = height / charHeight;
        while ( theMessages.size > linesOnScreen ) theMessages.remove( 0 );

        final Graphics gc = aScreen.graphics();
        gc.setColor( 0 );

        for ( int idx = 0; idx < theMessages.size; idx++ )
            {
            myBlitPos.y = height - ( idx + 1 ) * charHeight;
            for ( int y = myBlitPos.y; y < myBlitPos.y + charHeight; y += 2 )
                {
                gc.drawLine( 0, y, width, y );
                }
            final ConsoleEntry entry = (ConsoleEntry) theMessages.get( idx );
            myFontGen.blitString( gc, entry.message, myBlitPos, FontGen.TOP_LEFT );
            }
        }



    private final FontGen myFontGen;

    private final Position myBlitPos = new Position();

    private static final DynamicArray theMessages = new DynamicArray();



    public static final class ConsoleEntry
        {
        public int tickCounter;

        public final String message;

        public ConsoleEntry( final String aMessage )
            {
            message = aMessage;
            tickCounter = Engine.ticksPerSecond * 5;
            }
        }
    }