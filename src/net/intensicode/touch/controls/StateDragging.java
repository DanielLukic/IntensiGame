//#condition TOUCH

package net.intensicode.touch.controls;

import net.intensicode.touch.TouchEvent;
import net.intensicode.util.*;

public final class StateDragging extends State
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
