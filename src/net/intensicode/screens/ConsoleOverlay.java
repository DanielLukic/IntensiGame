//#condition CONSOLE

package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class ConsoleOverlay extends ScreenBase
    {
    public static boolean show = true;



    public static void addMessage( final String aMessage )
        {
        theMessages.insert( 0, new ConsoleEntry( aMessage, TICKS_NOT_SET_YET ) );
        }

    public ConsoleOverlay( final FontGenerator aFontGen )
        {
        myFontGen = aFontGen;
        }

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        for ( int idx = theMessages.size - 1; idx >= 0; idx-- )
            {
            final ConsoleEntry entry = (ConsoleEntry) theMessages.get( idx );
            if ( entry.tickCounter == TICKS_NOT_SET_YET )
                {
                entry.tickCounter = system().timing.ticksPerSecond * ENTRY_DISPLAY_TIME_IN_SECONDS;
                }
            if ( --entry.tickCounter == 0 )
                {
                theMessages.remove( idx );
                }
            }
        }

    public final void onDrawFrame()
        {
        if ( !show ) return;

        final int width = screen().width();
        final int height = screen().height();
        final int charHeight = myFontGen.charHeight();

        final int linesOnScreen = height / charHeight;
        while ( theMessages.size > linesOnScreen ) theMessages.remove( 0 );

        graphics().setColorRGB24( 0 );

        for ( int idx = 0; idx < theMessages.size; idx++ )
            {
            myBlitPos.y = height - ( idx + 1 ) * charHeight;
            for ( int y = myBlitPos.y; y < myBlitPos.y + charHeight; y += 2 )
                {
                graphics().drawLine( 0, y, width, y );
                }
            final ConsoleEntry entry = (ConsoleEntry) theMessages.get( idx );
            myFontGen.blitString( graphics(), entry.message, myBlitPos, FontGenerator.TOP_LEFT );
            }
        }



    private final FontGenerator myFontGen;

    private final Position myBlitPos = new Position();

    private static final DynamicArray theMessages = new DynamicArray();

    private static final int TICKS_NOT_SET_YET = -1;

    private static final int ENTRY_DISPLAY_TIME_IN_SECONDS = 5;



    public static final class ConsoleEntry
        {
        public int tickCounter;

        public final String message;

        public ConsoleEntry( final String aMessage, final int aTicksBeforeRemoval )
            {
            message = aMessage;
            tickCounter = aTicksBeforeRemoval;
            }
        }
    }
