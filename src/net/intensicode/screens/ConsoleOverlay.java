package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class ConsoleOverlay extends ScreenBase
    {
    //#if CONSOLE

    //#if FALSE
    public static boolean show = true;
    //#else
    //# public static boolean show = false;
    //#endif

    public int defaultStayTimeInTicks = TICKS_NOT_SET_YET;

    public int recommendedMaxStayTimeInTicks = TICKS_NOT_SET_YET;

    public FontGenerator font;


    public static void addMessage( final String aMessage )
        {
        theMessages.add( new ConsoleOverlayEntry( aMessage, TICKS_NOT_SET_YET ) );
        }

    public ConsoleOverlay( final FontGenerator aFontGen )
        {
        font = aFontGen;
        }

    // From ScreenBase

    public void onInitEverytime() throws Exception
        {
        if ( defaultStayTimeInTicks != TICKS_NOT_SET_YET ) return;
        defaultStayTimeInTicks = timing().ticksPerSecond * DEFAULT_STAY_TIME_IN_SECONDS;
        recommendedMaxStayTimeInTicks = timing().ticksPerSecond * MAX_STAY_TIME_IN_SECONDS;
        }

    public final void onControlTick() throws Exception
        {
        for ( int idx = 0; idx < theMessages.size; idx++ )
            {
            final ConsoleOverlayEntry entry = (ConsoleOverlayEntry) theMessages.get( idx );
            if ( entry.tickCounter == TICKS_NOT_SET_YET ) entry.tickCounter = defaultStayTimeInTicks;
            if ( entry.active ) entry.onControlTick();
            else myInactiveMessages.add( entry );
            }

        for ( int idx = 0; idx < myInactiveMessages.size; idx++ )
            {
            theMessages.removeAll( myInactiveMessages.get( idx ) );
            }
        myInactiveMessages.clear();
        }

    public final void onDrawFrame()
        {
        if ( !show ) return;

        final int width = screen().width();
        final int height = screen().height();
        final int charHeight = font.charHeight();

        final int linesOnScreen = height / charHeight;
        while ( theMessages.size > linesOnScreen ) theMessages.remove( 0 );

        graphics().setColorARGB32( 0x80000000 );

        for ( int idx = 0; idx < theMessages.size; idx++ )
            {
            myBlitPos.y = height - ( idx + 1 ) * charHeight;

            graphics().fillRect( 0, myBlitPos.y, width, charHeight );

            final ConsoleOverlayEntry entry = (ConsoleOverlayEntry) theMessages.get( theMessages.size - 1 - idx );
            font.blitString( graphics(), entry.message, myBlitPos, FontGenerator.TOP_LEFT );
            }
        }


    private final Position myBlitPos = new Position();

    private final DynamicArray myInactiveMessages = new DynamicArray();

    private static final DynamicArray theMessages = new DynamicArray();

    private static final int TICKS_NOT_SET_YET = -1;

    private static final int MAX_STAY_TIME_IN_SECONDS = 5;

    private static final int DEFAULT_STAY_TIME_IN_SECONDS = 5;

    //#else
    //# public final void onControlTick() throws Exception {};
    //# public final void onDrawFrame() {};
    //#endif
    }
