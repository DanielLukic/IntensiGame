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

    public static final String TAP = "TAP";


    public final Position lastEventPosition = new Position();

    public final DynamicArray gesture = new DynamicArray();

    public Rectangle optionalHotzone;


    public TouchGestures( final TouchGesturesConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    public final void reset()
        {
        gesture.clear();
        }

    public final void onDrawFrame( final DirectGraphics aGraphics )
        {
        //#if DEBUG_TOUCH
        aGraphics.setColorARGB32( 0x40FF00FF );
        for ( int idx = 0; idx < myStrokePath.size; idx++ )
            {
            final Position position = (Position) myStrokePath.get( idx );
            aGraphics.fillRect( position.x - 1, position.y - 1, 3, 3 );
            }
        //#endif
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        if ( optionalHotzone != null )
            {
            if ( !isInsideHotzone( aTouchEvent ) ) return;
            if ( isStartedInsideHotzone( aTouchEvent ) ) return;
            }

        if ( aTouchEvent.isPress() ) start( aTouchEvent );
        else if ( aTouchEvent.isSwipe() ) move( aTouchEvent );
        else if ( aTouchEvent.isRelease() ) end( aTouchEvent );
        }

    private boolean isInsideHotzone( final TouchEvent aTouchEvent )
        {
        return optionalHotzone.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        }

    private boolean isStartedInsideHotzone( final TouchEvent aTouchEvent )
        {
        return !aTouchEvent.isPress() && myStrokePath.empty();
        }

    // Implementation

    private void start( final TouchEvent aTouchEvent )
        {
        resetTemporaries();
        startStrokePath( aTouchEvent );
        startTimingBreak( aTouchEvent );
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
            if ( breakTimeThresholdReached( aTouchEvent ) )
                {
                addStroke( myStrokeStart, aTouchEvent );
                startStrokePath( aTouchEvent );
                startTimingBreak( aTouchEvent );
                return;
                }
            }
        else
            {
            startTimingBreak( aTouchEvent );
            }

        addToStrokePath( aTouchEvent );
        }

    private boolean isSamePosition( final Position aPosition, final TouchEvent aTouchEvent )
        {
        if ( Math.abs( aPosition.x - aTouchEvent.getX() ) > myConfiguration.samePositionThresholdInPixels )
            return false;
        if ( Math.abs( aPosition.y - aTouchEvent.getY() ) > myConfiguration.samePositionThresholdInPixels )
            return false;
        return true;
        }

    private boolean breakTimeThresholdReached( final TouchEvent aTouchEvent )
        {
        final long timestampDelta = aTouchEvent.timestamp() - myBreakTimingStart;
        return timestampDelta > myConfiguration.breakTimeThresholdInMillis;
        }

    private void startTimingBreak( final TouchEvent aTouchEvent )
        {
        myBreakTimingStart = aTouchEvent.timestamp();
        }

    private long myBreakTimingStart;

    private void end( final TouchEvent aTouchEvent )
        {
        addToStrokePath( aTouchEvent );
        addStroke( myStrokeStart, aTouchEvent );
        determineGesture();
        }

    private void addStroke( final Position aStartPosition, final TouchEvent aTouchEvent )
        {
        final int xDelta = aTouchEvent.getX() - aStartPosition.x;
        final int yDelta = aTouchEvent.getY() - aStartPosition.y;
        final String stroke = recognize( xDelta, yDelta );
        if ( stroke != NO_STROKE ) myStrokes.add( stroke );
        }

    private String recognize( int aDeltaX, int aDeltaY )
        {
        final float xScaled = Math.abs( aDeltaX ) * myConfiguration.directionIgnoreFactor;
        final float yScaled = Math.abs( aDeltaY ) * myConfiguration.directionIgnoreFactor;
        if ( Math.abs( aDeltaX ) > yScaled ) aDeltaY = 0;
        if ( Math.abs( aDeltaY ) > xScaled ) aDeltaX = 0;
        final int indexX = determineStrokeIndex( aDeltaX );
        final int indexY = determineStrokeIndex( aDeltaY );
        final int strokeIndex = indexY * 3 + indexX;
        return STROKES[ strokeIndex ];
        }

    private int determineStrokeIndex( final int aDelta )
        {
        final int delta = Math.abs( aDelta ) > myConfiguration.strokeThresholdInPixels ? aDelta : 0;
        if ( delta < 0 ) return 0;
        if ( delta > 0 ) return 2;
        return 1;
        }

    private void determineGesture()
        {
        gesture.clear();

        for ( int idx = 0; idx < myStrokes.size; idx++ )
            {
            gesture.add( myStrokes.objects[ idx ] );
            }

        if ( gesture.size == 0 ) gesture.add( TAP );

        //#if DEBUG_TOUCH
        //# Log.info( "GESTURE: {}", gesture );
        //#endif
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

    private void updateLastActionPosition( final TouchEvent aTouchEvent )
        {
        lastEventPosition.x = aTouchEvent.getX();
        lastEventPosition.y = aTouchEvent.getY();
        }


    private final TouchGesturesConfiguration myConfiguration;

    private final Position myStrokeStart = new Position();

    private final DynamicArray myStrokes = new DynamicArray();

    private final DynamicArray myStrokePath = new DynamicArray();

    private static final String[] STROKES =
            { NORTH_WEST, NORTH, NORTH_EAST,
              WEST, NO_STROKE, EAST,
              SOUTH_WEST, SOUTH, SOUTH_EAST };
    }
