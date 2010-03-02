//#condition TOUCH

package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.screens.ScreenBase;
import net.intensicode.util.*;

public final class TouchSequenceHandler extends ScreenBase implements TouchEventListener
    {
    public static final int MAX_SEQUENCE_LENGTH = 16;

    public static final int GRID_SIZE = 3;

    public int[] cheatSequence = { GRID_SIZE * 2, GRID_SIZE + 2, 0, GRID_SIZE + 2, GRID_SIZE + 1, GRID_SIZE };

    public int[] debugSequence = { 0, GRID_SIZE + 2, GRID_SIZE * 2, 1, GRID_SIZE + 1, GRID_SIZE * 2 + 1 };


    public final boolean isSequenceMatched( final int[] aSequence )
        {
        for ( int idx = 0; idx < aSequence.length; idx++ )
            {
            final int sequenceIndex = myCellSequence.length - aSequence.length + idx;
            if ( myCellSequence[ sequenceIndex ] != aSequence[ idx ] ) return false;
            }
        return true;
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
        for ( int idx = 0; idx < myCellSequence.length; idx++ ) myCellSequence[ idx ] = UNUSED_SEQUENCE_INDEX;
        }

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        //#if DEBUG_TOUCH
        drawCellSequence();
        drawTouchEvents();
        //#endif
        }

    //#if DEBUG_TOUCH
    private void drawCellSequence()
        {
        final DirectGraphics graphics = graphics();
        graphics.setColorARGB32( CELL_SEQUENCE_COLOR_ARGB32 );
        for ( int idx = 0; idx < myCellSequence.length; idx++ )
            {
            if ( myCellSequence[ idx ] == UNUSED_SEQUENCE_INDEX ) continue;
            final int cellIndex = myCellSequence[ idx ];
            final int x = ( cellIndex % GRID_SIZE ) * myCellWidth;
            final int y = ( cellIndex / GRID_SIZE ) * myCellHeight;
            graphics.fillRect( x, y, myCellWidth, myCellHeight );
            graphics.fillRect( x, y, myCellWidth, myCellHeight * idx / myCellSequence.length );
            }
        }

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

    public synchronized final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        //#if DEBUG_TOUCH
        if ( myTouches.size > MAX_QUEUE_SIZE ) myTouches.remove( 0 );
        myTouches.add( new Position( aTouchEvent.getX(), aTouchEvent.getY() ) );
        //#endif

        final int cellX = aTouchEvent.getX() / myCellWidth;
        final int cellY = aTouchEvent.getY() / myCellHeight;
        final int cellIndex = cellX + cellY * GRID_SIZE;
        if ( cellIndex == myCellSequence[ myCellSequence.length - 1 ] ) return;

        appendToCellSequence( cellIndex );

        final boolean debugMatched = isSequenceMatched( debugSequence );
        if ( debugMatched ) system().context.onDebugTriggered();

        final boolean cheatMatched = isSequenceMatched( cheatSequence );
        if ( cheatMatched ) system().context.onCheatTriggered();
        }

    // Implementation

    private void appendToCellSequence( final int aCellIndex )
        {
        // manual copy because some J2ME System.arraycopy implementations are broken..
        for ( int idx = 0; idx < myCellSequence.length - 1; idx++ )
            {
            myCellSequence[ idx ] = myCellSequence[ idx + 1 ];
            }

        myCellSequence[ myCellSequence.length - 1 ] = aCellIndex;
        }


    private int myCellWidth;

    private int myCellHeight;

    //#if DEBUG_TOUCH
    private final DynamicArray myTouches = new DynamicArray();
    //#endif

    private final int[] myCellSequence = new int[MAX_SEQUENCE_LENGTH];


    private static final int MAX_QUEUE_SIZE = 100;

    private static final int UNUSED_SEQUENCE_INDEX = -1;

    private static final int CELL_SEQUENCE_COLOR_ARGB32 = 0x10008000;

    private static final int TOUCH_EVENTS_COLOR_ARGB32 = 0x10FFFFFF;
    }
