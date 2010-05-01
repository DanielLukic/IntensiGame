//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.*;
import net.intensicode.core.DirectGraphics;

public final class TouchControls implements TouchEventListener
    {
    public TouchControls( final TouchControlsConfiguration aConfiguration )
        {
        mySharedState.configuration = aConfiguration;

        mySharedState.idleState = new StateIdle().share( mySharedState );
        mySharedState.startingState = new StateStarting().share( mySharedState );
        mySharedState.draggingState = new StateDragging().share( mySharedState );
        mySharedState.gestureState = new StateGesture().share( mySharedState );

        mySharedState.idleState.transition();
        }

    public final boolean hasGesture()
        {
        return mySharedState.recognizedGesture != null;
        }

    public final String getAndResetGesture()
        {
        final String gesture = mySharedState.recognizedGesture;
        mySharedState.recognizedGesture = null;
        return gesture;
        }

    public final boolean hasDragSteps()
        {
        return mySharedState.dragSteps.validDirection();
        }

    public final Position getAndResetDragSteps()
        {
        myDragSteps.setTo( mySharedState.dragSteps );
        mySharedState.dragSteps.setTo( 0, 0 );
        return myDragSteps;
        }

    public final void setOptionalHotzoneByReference( final Rectangle aHotzone )
        {
        mySharedState.optionalHotzone = aHotzone;
        }

    //#if DEBUG_TOUCH

    public final void onDrawFrame( final DirectGraphics aGraphics )
        {
        mySharedState.onDrawFrame( aGraphics );
        }

    //#endif

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        mySharedState.updateTouchEvent( aTouchEvent );
        mySharedState.currentState.processTouchEvent();
        }


    private final Position myDragSteps = new Position();

    private final SharedState mySharedState = new SharedState();


    private static final class SharedState
        {
        public TouchControlsConfiguration configuration;

        public Rectangle optionalHotzone;

        public State idleState;

        public State startingState;

        public State draggingState;

        public State gestureState;

        public State currentState;


        public TouchEvent touchEvent;

        public final Position deltaFromStart = new Position();

        public long deltaFromStartTimestamp;

        public final Position deltaFromPrevious = new Position();

        public long deltaFromPreviousTimestamp;

        public float speedX;

        public float speedY;

        public long startTimestamp;

        public final Position startPosition = new Position();

        public long latestTimestamp;

        public final Position latestPosition = new Position();


        public String recognizedGesture;

        public final Position dragSteps = new Position();


        public final void updateTouchEvent( final TouchEvent aTouchEvent )
            {
            touchEvent = aTouchEvent;

            if ( touchEvent.isPress() )
                {
                startTimestamp = touchEvent.timestamp();
                startPosition.setTo( touchEvent.getX(), touchEvent.getY() );

                latestTimestamp = touchEvent.timestamp();
                latestPosition.setTo( touchEvent.getX(), touchEvent.getY() );
                }

            deltaFromStartTimestamp = touchEvent.timestamp() - startTimestamp;
            deltaFromStart.setTo( touchEvent.getX() - startPosition.x, touchEvent.getY() - startPosition.y );

            deltaFromPreviousTimestamp = touchEvent.timestamp() - latestTimestamp;
            deltaFromPrevious.setTo( touchEvent.getX() - latestPosition.x, touchEvent.getY() - latestPosition.y );

            latestTimestamp = touchEvent.timestamp();
            latestPosition.setTo( touchEvent.getX(), touchEvent.getY() );

            speedX = deltaFromPrevious.x / (float) deltaFromPreviousTimestamp;
            speedY = deltaFromPrevious.y / (float) deltaFromPreviousTimestamp;

            Log.info( "SPEED {}/{}", speedX, speedY );
            }

        //#if DEBUG_TOUCH

        public final void onDrawFrame( final DirectGraphics aGraphics )
            {
            final Rectangle hotzone = optionalHotzone;
            if ( hotzone != null )
                {
                aGraphics.setColorARGB32( 0x8000FF00 );
                aGraphics.drawRect( hotzone.x, hotzone.y, hotzone.width, hotzone.height );
                }
            if ( touchEvent != null )
                {
                aGraphics.setColorARGB32( 0x80FFFF00 );
                final int x = touchEvent.getX() - 2;
                final int y = touchEvent.getY() - 2;
                aGraphics.drawRect( x, y, 5, 5 );
                }
            if ( currentState != null )
                {
                currentState.onDrawFrame( aGraphics );
                }
            }

        //#endif
        }


    private static abstract class State
        {
        public SharedState shared;

        //#if DEBUG_TOUCH

        public void onDrawFrame( final DirectGraphics aGraphics )
            {
            }

        //#endif

        public final State share( final SharedState aSharedState )
            {
            shared = aSharedState;
            return this;
            }

        public final void transition()
            {
            if ( shared.currentState != null ) shared.currentState.leave();
            shared.currentState = this;
            shared.currentState.enter();
            }

        public void enter()
            {
            }

        public void leave()
            {
            }

        public abstract void processTouchEvent();


        protected final boolean gestureConditionMet()
            {
            final long deltaInMillis = shared.touchEvent.timestamp() - shared.startTimestamp;
            if ( deltaInMillis < shared.configuration.gestureMinThresholdInMillis ) return false;
            if ( deltaInMillis > shared.configuration.gestureMaxThresholdInMillis ) return false;
            return shared.touchEvent.isRelease();
            }
        }


    private static final class StateIdle extends State
        {
        public final void processTouchEvent()
            {
            if ( shared.touchEvent.isRelease() )
                {
                Log.info( "" );
                Log.info( "RELEASED IN IDLE" );
                Log.info( "" );
                return;
                }
            if ( isInsideHotzone() && shared.touchEvent.isSwipe() )
                {
                if ( shared.configuration.swipeStartsAction ) shared.startingState.transition();
                }
            if ( !shared.touchEvent.isPress() ) return;
            if ( !isInsideHotzone() ) return;
            shared.startingState.transition();
            }

        private boolean isInsideHotzone()
            {
            final TouchEvent event = shared.touchEvent;
            final Rectangle hotzone = shared.optionalHotzone;
            return hotzone == null || hotzone.contains( event.getX(), event.getY() );
            }
        }


    private final class StateStarting extends State
        {
        //#if DEBUG_TOUCH

        public void onDrawFrame( final DirectGraphics aGraphics )
            {
            final Position start = shared.startPosition;
            final Size deadZoneSize = shared.configuration.deadZoneSize;
            final int x = start.x - deadZoneSize.width / 2;
            final int y = start.y - deadZoneSize.height / 2;
            aGraphics.setColorARGB32( 0x80FF0000 );
            aGraphics.drawRect( x, y, deadZoneSize.width, deadZoneSize.height );
            }

        //#endif

        public final void enter()
            {
            final TouchEvent event = shared.touchEvent;
            shared.startTimestamp = event.timestamp();
            shared.startPosition.setTo( event.getX(), event.getY() );
            }

        public final void processTouchEvent()
            {
            if ( isStillInDeadZone() ) return;
            if ( gestureConditionMet() ) shared.gestureState.transition();
            if ( dragThresholdReached() ) shared.draggingState.transition();
            if ( shared.touchEvent.isRelease() )
                {
                Log.info( "" );
                Log.info( "RELEASED IN STARTING" );
                Log.info( "TIMESTAMP DELTA: {}", shared.touchEvent.timestamp() - shared.startTimestamp );
                Log.info( "X DELTA: {}", shared.touchEvent.getX() - shared.startPosition.x );
                Log.info( "Y DELTA: {}", shared.touchEvent.getY() - shared.startPosition.y );
                Log.info( "" );
                shared.idleState.transition();
                }
            }

        private boolean isStillInDeadZone()
            {
            final TouchEvent event = shared.touchEvent;
            final Position start = mySharedState.startPosition;
            final int deltaX = event.getX() - start.x;
            final int deltaY = event.getY() - start.y;
            if ( Math.abs( deltaX ) > mySharedState.configuration.deadZoneSize.width ) return false;
            if ( Math.abs( deltaY ) > mySharedState.configuration.deadZoneSize.height ) return false;
            return true;
            }

        private boolean dragThresholdReached()
            {
            final TouchEvent event = shared.touchEvent;
            final Position start = mySharedState.startPosition;
            final int deltaX = event.getX() - start.x;
            final int deltaY = event.getY() - start.y;
            if ( Math.abs( deltaX ) > shared.configuration.dragThresholdInPixels.width ) return true;
            if ( Math.abs( deltaY ) > shared.configuration.dragThresholdInPixels.height ) return true;
            final long deltaInMillis = shared.touchEvent.timestamp() - shared.startTimestamp;
            return ( deltaInMillis > shared.configuration.dragThresholdInMillis );
            }
        }


    private final class StateDragging extends State
        {
        public final void enter()
            {
            final TouchEvent event = shared.touchEvent;
            final int deltaX = ( event.getX() - shared.startPosition.x ) / 2;
            final int deltaY = ( event.getY() - shared.startPosition.y ) / 2;
            shared.startPosition.x += deltaX;
            shared.startPosition.y += deltaY;

            myLastDragSteps.setTo( 0, 0 );
            myAccumulatedDeltas.setTo( 0, 0 );
            }

        public final void processTouchEvent()
            {
            if ( gestureConditionMet() )
                {
                Log.info( "GESTURE DURING DRAG" );
                Log.info( "DRAG STEPS ALREADY EMITTED: {}/{}", myLastDragSteps.x, myLastDragSteps.y );
                Log.info( "ABORTING DRAG - EMITTING GESTURE" );
                shared.gestureState.transition();
                shared.dragSteps.x = 0;
                shared.dragSteps.y = 0;
                return;
                }

            final TouchEvent event = shared.touchEvent;
            if ( event.isRelease() )
                {
                shared.idleState.transition();
                return;
                }

            final float speedX = Math.abs( shared.speedX ) + shared.configuration.speedLowBoundary;
            final float speedY = Math.abs( shared.speedY ) + shared.configuration.speedLowBoundary;
            final float limitedSpeedX = Math.min( speedX, shared.configuration.speedHighBoundary );
            final float limitedSpeedY = Math.min( speedY, shared.configuration.speedHighBoundary );
            myAccumulatedDeltas.x += MathExtended.round( shared.deltaFromPrevious.x * limitedSpeedX );
            myAccumulatedDeltas.y += MathExtended.round( shared.deltaFromPrevious.y * limitedSpeedY );

            final Size dragStepSize = shared.configuration.dragStepSize;
            final int stepWidth = dragStepSize.width;
            final int stepHeight = dragStepSize.height;
            final int halfWidth = stepWidth / 2;
            final int halfHeight = stepHeight / 2;

            final int dragStepsX = ( myAccumulatedDeltas.x - halfWidth ) / stepWidth;
            final int dragStepsY = ( myAccumulatedDeltas.y - halfHeight ) / stepHeight;

            shared.dragSteps.x += dragStepsX - myLastDragSteps.x;
            shared.dragSteps.y += dragStepsY - myLastDragSteps.y;

            myLastDragSteps.setTo( dragStepsX, dragStepsY );
            }

        private final Position myLastDragSteps = new Position();

        private final Position myAccumulatedDeltas = new Position();
        }


    private final class StateGesture extends State
        {
        public void enter()
            {
            shared.recognizedGesture = recognize( shared.deltaFromStart.x, shared.deltaFromStart.y );

            shared.idleState.transition();
            }

        private String recognize( float aDeltaX, float aDeltaY )
            {
            final float scaledX = Math.abs( aDeltaX ) * shared.configuration.gestureDirectionIgnoreFactor;
            final float scaledY = Math.abs( aDeltaY ) * shared.configuration.gestureDirectionIgnoreFactor;
            if ( Math.abs( aDeltaX ) > scaledY ) aDeltaY = 0;
            if ( Math.abs( aDeltaY ) > scaledX ) aDeltaX = 0;
            final int indexX = determineStrokeIndex( aDeltaX );
            final int indexY = determineStrokeIndex( aDeltaY );
            final int strokeIndex = indexY * 3 + indexX;
            return TouchGestures.STROKES[ strokeIndex ];
            }

        private int determineStrokeIndex( final float aDelta )
            {
            if ( aDelta < 0 ) return 0;
            if ( aDelta > 0 ) return 2;
            return 1;
            }

        public final void processTouchEvent()
            {
            throw new IllegalStateException();
            }
        }
    }
