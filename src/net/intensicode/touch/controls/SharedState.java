//#condition TOUCH

package net.intensicode.touch.controls;

import net.intensicode.util.*;
import net.intensicode.core.DirectGraphics;
import net.intensicode.touch.*;

public final class SharedState
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
