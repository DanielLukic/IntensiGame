//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchGestures implements TouchEventListener
    {
    public static final String NORTH_WEST = "NORTH_WEST";

    public static final String NORTH = "NORTH";

    public static final String NORTH_EAST = "NORTH_EAST";

    public static final String WEST = "WEST";

    public static final String NO_STROKE = "NO STROKE";

    public static final String EAST = "EAST";

    public static final String SOUTH_WEST = "SOUTH_WEST";

    public static final String SOUTH = "SOUTH";

    public static final String SOUTH_EAST = "SOUTH_EAST";

    public static final String WAIT = "WAIT";


    public final Position lastEventPosition = new Position();

    public final DynamicArray gesture = new DynamicArray();


    public boolean combineGestures = true;

    public int combinedGestureThresholdInMillis = 1000 / 6;

    public int breakTimeThresholdInMillis = 1000 / 12;

    public int samePositionThresholdInPixels = 10;

    public int strokeThresholdInPixels = 5;

    public int directionIgnoreFactor = 2;


    public final boolean couldBeCombinedGesture()
        {
        if ( !combineGestures ) return false;
        return System.currentTimeMillis() - myLastGestureTimestamp < combinedGestureThresholdInMillis;
        }

    public final void reset()
        {
        gesture.clear();
        }

    public final void onDrawFrame( final DirectGraphics aGraphics )
        {
        aGraphics.setColorARGB32( 0x40FF00FF );
        for ( int idx = 0; idx < myStrokePath.size; idx++ )
            {
            final Position position = (Position) myStrokePath.get( idx );
            aGraphics.fillRect( position.x - 1, position.y - 1, 3, 3 );
            }
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        if ( aTouchEvent.isPress() ) start( aTouchEvent );
        else if ( aTouchEvent.isSwipe() ) move( aTouchEvent );
        else if ( aTouchEvent.isRelease() ) end( aTouchEvent );
        }

    // Implementation

    private void start( final TouchEvent aTouchEvent )
        {
        Log.debug( "starting new gesture" );

        if ( !couldBeCombinedGesture() ) resetTemporaries();
        else Log.debug( "COMBINED" );

        startStrokePath( aTouchEvent );
        }

    private void resetTemporaries()
        {
        myStrokePath.clear();
        myStrokes.clear();
        }

    private void move( final TouchEvent aTouchEvent )
        {
        if ( isSamePosition( lastEventPosition, aTouchEvent ) )
            {
            Log.debug( "same position.." );
            if ( breakTimeThresholdReached( aTouchEvent ) )
                {
                Log.debug( "breaking stroke" );
                addStroke( myStrokeStart, aTouchEvent );
                startStrokePath( aTouchEvent );
                startTimingBreak( aTouchEvent );
                return;
                }
            }
        else
            {
            Log.debug( "position changed.." );
            startTimingBreak( aTouchEvent );
            }

        Log.debug( "continuing current stroke" );
        addToStrokePath( aTouchEvent );
        }

    private boolean breakTimeThresholdReached( final TouchEvent aTouchEvent )
        {
        return aTouchEvent.timestamp() - myBreakTimingStart > breakTimeThresholdInMillis;
        }

    private void startTimingBreak( final TouchEvent aTouchEvent )
        {
        myBreakTimingStart = aTouchEvent.timestamp();
        }

    private long myBreakTimingStart;

    private void end( final TouchEvent aTouchEvent )
        {
        Log.debug( "ending gesture" );
        addToStrokePath( aTouchEvent );

        addStroke( myStrokeStart, aTouchEvent );

        determineGesture();
        }

    private String recognize( int aDeltaX, int aDeltaY )
        {
        if ( Math.abs( aDeltaX ) > Math.abs( aDeltaY ) * directionIgnoreFactor ) aDeltaY = 0;
        if ( Math.abs( aDeltaY ) > Math.abs( aDeltaX ) * directionIgnoreFactor ) aDeltaX = 0;
        final int indexX = determineStrokeIndex( aDeltaX );
        final int indexY = determineStrokeIndex( aDeltaY );
        final int strokeIndex = indexY * 3 + indexX;
        return STROKES[ strokeIndex ];
        }

    private int determineStrokeIndex( final int aDelta )
        {
        final int delta = Math.abs( aDelta ) > strokeThresholdInPixels ? aDelta : 0;
        if ( delta < 0 ) return 0;
        if ( delta > 0 ) return 2;
        return 1;
        }

    private void addStroke( final Position aStartPosition, final TouchEvent aTouchEvent )
        {
        final int xDelta = aTouchEvent.getX() - aStartPosition.x;
        final int yDelta = aTouchEvent.getY() - aStartPosition.y;
        final String stroke = recognize( xDelta, yDelta );
        if ( stroke != NO_STROKE )
            {
            Log.debug( "adding stroke {}", stroke );
            myStrokes.add( stroke );
            }

        myLastGestureTimestamp = System.currentTimeMillis();
        }

    private void determineGesture()
        {
        gesture.clear();

        for ( int idx = 0; idx < myStrokes.size; idx++ )
            {
            gesture.add( myStrokes.objects[ idx ] );
            }

        Log.debug( "new gesture: {}", gesture );
        }

    private void startStrokePath( final TouchEvent aTouchEvent )
        {
        setStrokeStart( aTouchEvent );
        addToStrokePath( aTouchEvent );
        }

    private void setStrokeStart( final TouchEvent aTouchEvent )
        {
        myStrokeStart.x = aTouchEvent.getX();
        myStrokeStart.y = aTouchEvent.getY();
        }

    private void addToStrokePath( final TouchEvent aTouchEvent )
        {
        updateLastActionPosition( aTouchEvent );
        myStrokePath.add( new Position( aTouchEvent.getX(), aTouchEvent.getY() ) );
        }

    private boolean isSamePosition( final Position aPosition, final TouchEvent aTouchEvent )
        {
        if ( Math.abs( aPosition.x - aTouchEvent.getX() ) > samePositionThresholdInPixels ) return false;
        if ( Math.abs( aPosition.y - aTouchEvent.getY() ) > samePositionThresholdInPixels ) return false;
        return true;
        }

    private void updateLastActionPosition( final TouchEvent aTouchEvent )
        {
        lastEventPosition.x = aTouchEvent.getX();
        lastEventPosition.y = aTouchEvent.getY();
        }


    private long myLastGestureTimestamp;

    private final Position myStrokeStart = new Position();

    private final DynamicArray myStrokes = new DynamicArray();

    private final DynamicArray myStrokePath = new DynamicArray();

    private static final String[] STROKES =
            { NORTH_WEST, NORTH, NORTH_EAST,
              WEST, NO_STROKE, EAST,
              SOUTH_WEST, SOUTH, SOUTH_EAST };
    }