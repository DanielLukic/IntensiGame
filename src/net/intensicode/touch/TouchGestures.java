//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.PlatformContext;
import net.intensicode.core.DirectGraphics;
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

    public static final String[] STROKES =
            { NORTH_WEST, NORTH, NORTH_EAST,
              WEST, NO_STROKE, EAST,
              SOUTH_WEST, SOUTH, SOUTH_EAST };


    public final PositionF lastEventPosition = new PositionF();

    public final DynamicArray gesture = new DynamicArray();

    public Rectangle optionalHotzone;


    public TouchGestures( final TouchGesturesConfiguration aConfiguration, final PlatformContext aPlatformContext )
        {
        myConfiguration = aConfiguration;
        myPlatformContext = aPlatformContext;
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
            final PositionF position = (PositionF) myStrokePath.get( idx );
            aGraphics.fillRect( (int) ( position.x - 1 ), (int) ( position.y - 1 ), 3, 3 );
            }
        //#endif
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        setCurrentEvent( aTouchEvent );

        if ( optionalHotzone != null )
            {
            if ( !isInsideHotzone() ) return;
            if ( isNotStartedInsideHotzone() ) return;
            }

        if ( aTouchEvent.isPress() ) start();
        else if ( aTouchEvent.isSwipe() ) move();
        else if ( aTouchEvent.isRelease() ) end();
        }

    private void setCurrentEvent( final TouchEvent aTouchEvent )
        {
        myTouchEvent = aTouchEvent;
        myTouchX = aTouchEvent.getX() * myConfiguration.devicePixelFactorX;
        myTouchY = aTouchEvent.getY() * myConfiguration.devicePixelFactorY;
        }

    private boolean isInsideHotzone()
        {
        return optionalHotzone.contains( myTouchEvent.getX(), myTouchEvent.getY() );
        }

    private boolean isNotStartedInsideHotzone()
        {
        return !myTouchEvent.isPress() && myStrokePath.empty();
        }

    // Implementation

    private void start()
        {
        resetTemporaries();
        startStrokePath();
        startTimingBreak();
        }

    private void resetTemporaries()
        {
        myStrokePath.clear();
        myStrokes.clear();
        }

    private void move()
        {
        if ( isSamePosition( lastEventPosition ) )
            {
            if ( breakTimeThresholdReached() )
                {
                addStroke( myStrokeStart );
                startStrokePath();
                startTimingBreak();
                return;
                }
            }
        else
            {
            startTimingBreak();
            }

        addToStrokePath();
        }

    private boolean isSamePosition( final PositionF aPosition )
        {
        if ( Math.abs( aPosition.x - myTouchX ) > myConfiguration.samePositionThresholdInPixels ) return false;
        if ( Math.abs( aPosition.y - myTouchY ) > myConfiguration.samePositionThresholdInPixels ) return false;
        return true;
        }

    private boolean breakTimeThresholdReached()
        {
        final long timestampDelta = myTouchEvent.timestamp() - myBreakTimingStart;
        return timestampDelta > myConfiguration.breakTimeThresholdInMillis;
        }

    private void startTimingBreak()
        {
        myBreakTimingStart = myTouchEvent.timestamp();
        }

    private void end()
        {
        addToStrokePath();
        addStroke( myStrokeStart );
        determineGesture();
        }

    private void addStroke( final PositionF aStartPosition )
        {
        final float xDelta = myTouchX - aStartPosition.x;
        final float yDelta = myTouchY - aStartPosition.y;
        final String stroke = recognize( xDelta, yDelta );
        if ( stroke != NO_STROKE ) myStrokes.add( stroke );
        }

    private String recognize( float aDeltaX, float aDeltaY )
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

    private int determineStrokeIndex( final float aDelta )
        {
        final float delta = Math.abs( aDelta ) > myConfiguration.strokeThresholdInPixels ? aDelta : 0;
        if ( delta < 0 ) return 0;
        if ( delta > 0 ) return 2;
        return 1;
        }

    private void determineGesture()
        {
        gesture.clear();

        final long now = myPlatformContext.compatibleTimeInMillis();
        if ( now - myStrokeStartTimestamp < myConfiguration.gestureStartThresholdInMillis ) return;

        for ( int idx = 0; idx < myStrokes.size; idx++ )
            {
            gesture.add( myStrokes.objects[ idx ] );
            }

        if ( gesture.size == 0 ) gesture.add( TAP );

        //#if DEBUG_TOUCH
        //# Log.info( "GESTURE: {}", gesture );
        //#endif
        }

    private void startStrokePath()
        {
        setStrokeStart();
        addToStrokePath();
        myStrokeStartTimestamp = myTouchEvent.timestamp();
        }

    private void setStrokeStart()
        {
        myStrokeStart.x = myTouchX;
        myStrokeStart.y = myTouchY;
        }

    private void addToStrokePath()
        {
        updateLastActionPosition();

        // TODO: Use object pool here..
        myStrokePath.add( new PositionF( myTouchX, myTouchY ) );
        }

    private void updateLastActionPosition()
        {
        lastEventPosition.x = myTouchX;
        lastEventPosition.y = myTouchY;
        }


    private float myTouchX;

    private float myTouchY;

    private long myBreakTimingStart;

    private TouchEvent myTouchEvent;

    private long myStrokeStartTimestamp;

    private final PlatformContext myPlatformContext;

    private final TouchGesturesConfiguration myConfiguration;

    private final PositionF myStrokeStart = new PositionF();

    private final DynamicArray myStrokes = new DynamicArray();

    private final DynamicArray myStrokePath = new DynamicArray();
    }
