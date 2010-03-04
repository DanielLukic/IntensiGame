//#condition TOUCH

package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.screens.ScreenBase;
import net.intensicode.util.*;

public final class ControlSequenceHandler extends ScreenBase
//#if TOUCH
        implements TouchEventListener
//#endif
    {
    public static final int MAX_SEQUENCE_LENGTH = 16;

    public static final int GRID_SIZE = 3;

    //#if CHEAT
    public int[] cheatSequence = { GRID_SIZE * 2, GRID_SIZE + 2, 0, GRID_SIZE + 2, GRID_SIZE + 1, GRID_SIZE };
    //#endif

    //#if DEBUG

    public int[] debugSequence = { 0, GRID_SIZE + 2, GRID_SIZE * 2, 1, GRID_SIZE + 1, GRID_SIZE * 2 + 1 };
    //#endif

    //#if TIMING

    public int[] timingSequence = { 0, 1, 2, 3, 1 };
    //#endif


    public final boolean isSequenceMatched( final int[] aSequence )
        {
        for ( int idx = 0; idx < aSequence.length; idx++ )
            {
            final int sequenceIndex = myCellSequence.length - aSequence.length + idx;
            if ( myCellSequence[ sequenceIndex ] != aSequence[ idx ] ) return false;
            }
        reset();
        return true;
        }

    public final void reset()
        {
        for ( int idx = 0; idx < myCellSequence.length; idx++ ) myCellSequence[ idx ] = UNUSED_SEQUENCE_INDEX;
        }

    // From ScreenBase

    public final void onInitOnce() throws Exception
        {
        touch().addListener( this );
        }

    public void onInitEverytime() throws Exception
        {
        myCellWidth = screen().width() / GRID_SIZE;
        myCellHeight = screen().height() / GRID_SIZE;
        reset();
        }

    public final void onControlTick() throws Exception
        {
        processLastKeyCode();

        //#if CHEAT
        final boolean cheatMatched = isSequenceMatched( cheatSequence );
        if ( cheatMatched ) system().context.onCheatTriggered();
        //#endif

        //#if DEBUG
        final boolean debugMatched = isSequenceMatched( debugSequence );
        if ( debugMatched ) system().context.onDebugTriggered();
        //#endif

        //#if TIMING
        final boolean timingMatched = isSequenceMatched( timingSequence );
        if ( timingMatched )
            {
            final StringBuffer buffer = new StringBuffer();
            Timing.dumpInto( buffer );
            System.out.println( buffer );
            }
        //#endif
        }

    private void processLastKeyCode()
        {
        final int code = keys().lastCode;
        if ( code == myLastCodeSeen ) return;

        final KeysConfiguration configuration = keys().platformKeysConfiguration;
        if ( code == configuration.keyNum1 ) appendToCellSequence( 0 );
        if ( code == configuration.keyNum2 ) appendToCellSequence( 1 );
        if ( code == configuration.keyNum3 ) appendToCellSequence( 2 );
        if ( code == configuration.keyNum4 ) appendToCellSequence( 3 );
        if ( code == configuration.keyNum5 ) appendToCellSequence( 4 );
        if ( code == configuration.keyNum6 ) appendToCellSequence( 5 );
        if ( code == configuration.keyNum7 ) appendToCellSequence( 6 );
        if ( code == configuration.keyNum8 ) appendToCellSequence( 7 );
        if ( code == configuration.keyNum9 ) appendToCellSequence( 8 );

        myLastCodeSeen = code;
        }

    public final void onDrawFrame()
        {
        //#if TOUCH && DEBUG_TOUCH
        drawTouchEvents();
        //#endif
        }

    //#if TOUCH && DEBUG_TOUCH

    private void drawTouchEvents()
        {
        final DirectGraphics graphics = graphics();
        graphics.setColorARGB32( TOUCH_EVENTS_COLOR_ARGB32 );
        for ( int idx = 0; idx < myTouches.size; idx++ )
            {
            final Position position = (Position) myTouches.get( idx );
            graphics.fillRect( position.x - 1, position.y - 1, 3, 3 );
            }
        }

    //#endif

    // From TouchEventListener

    //#if TOUCH

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        //#if DEBUG_TOUCH
        if ( myTouches.size > MAX_QUEUE_SIZE ) myTouches.remove( 0 );
        myTouches.add( new Position( aTouchEvent.getX(), aTouchEvent.getY() ) );
        //#endif

        final int cellX = aTouchEvent.getX() / myCellWidth;
        final int cellY = aTouchEvent.getY() / myCellHeight;
        final int cellIndex = cellX + cellY * GRID_SIZE;
        appendToCellSequence( cellIndex );
        }

    //#endif

    // Implementation

    private void appendToCellSequence( final int aCellIndex )
        {
        if ( aCellIndex == myCellSequence[ myCellSequence.length - 1 ] ) return;

        // manual copy because some J2ME System.arraycopy implementations are broken..
        for ( int idx = 0; idx < myCellSequence.length - 1; idx++ )
            {
            myCellSequence[ idx ] = myCellSequence[ idx + 1 ];
            }

        myCellSequence[ myCellSequence.length - 1 ] = aCellIndex;
        }


    private int myCellWidth;

    private int myCellHeight;

    private int myLastCodeSeen;

    //#if TOUCH && DEBUG_TOUCH
    private final DynamicArray myTouches = new DynamicArray();
    //#endif

    private final int[] myCellSequence = new int[MAX_SEQUENCE_LENGTH];


    private static final int MAX_QUEUE_SIZE = 16;

    private static final int UNUSED_SEQUENCE_INDEX = -1;

    private static final int TOUCH_EVENTS_COLOR_ARGB32 = 0x10FFFFFF;
    }
