//#condition TOUCH

package net.intensicode.touch.controls;

import net.intensicode.core.DirectGraphics;
import net.intensicode.touch.TouchEvent;
import net.intensicode.util.*;

public final class StateStarting extends State
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
        else if ( gestureConditionMet() ) shared.gestureState.transition();
        else if ( dragThresholdReached() ) shared.draggingState.transition();
            else if ( shared.touchEvent.isRelease() ) shared.idleState.transition();
        }

    private boolean isStillInDeadZone()
        {
        final TouchEvent event = shared.touchEvent;
        final Position start = shared.startPosition;
        final int deltaX = event.getX() - start.x;
        final int deltaY = event.getY() - start.y;
        if ( Math.abs( deltaX ) > shared.configuration.deadZoneSize.width ) return false;
        if ( Math.abs( deltaY ) > shared.configuration.deadZoneSize.height ) return false;
        return true;
        }

    private boolean dragThresholdReached()
        {
        final TouchEvent event = shared.touchEvent;
        final Position start = shared.startPosition;
        final int deltaX = event.getX() - start.x;
        final int deltaY = event.getY() - start.y;
        if ( Math.abs( deltaX ) > shared.configuration.dragThresholdInPixels.width ) return true;
        if ( Math.abs( deltaY ) > shared.configuration.dragThresholdInPixels.height ) return true;
        final long deltaInMillis = shared.touchEvent.timestamp() - shared.startTimestamp;
        return ( deltaInMillis > shared.configuration.dragThresholdInMillis );
        }
    }
