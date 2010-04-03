//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchSlider implements TouchEventListener
    {
    public final Rectangle touchableArea = new Rectangle();

    public Rectangle optionalHotzone;

    public int slideStartThresholdInMillis = 25;

    public int slideStartThresholdInPixels = 20;

    public int slideMoveThresholdInPixels = 10;

    public int newSlideStartThresholdInMillis = 50;

    public int initialStepThresholdInPixels = 10;

    public int additionalStepThresholdInPixels = 100;

    public final Size stepSizeInPixels = new Size( 1, 1 );

    public final Position slideSteps = new Position();


    public final void clearSlideSteps()
        {
        slideSteps.x = slideSteps.y = 0;
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        if ( !isInsideHotzone( aTouchEvent ) ) return;

        setCurrentEvent( aTouchEvent );

        if ( isFirstPosition() ) setFirstPosition();

        updateDeltasForThisEvent();

        if ( mySlideStarted ) onSliding();
        else onSlideNotStartedYet();
        }

    // Implementation

    private boolean isInsideHotzone( final TouchEvent aTouchEvent )
        {
        final Rectangle hotzone = optionalHotzone != null ? optionalHotzone : touchableArea;
        return hotzone.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        }

    public final void setCurrentEvent( final TouchEvent aTouchEvent )
        {
        this.aTouchEvent = aTouchEvent;
        }

    private boolean isFirstPosition()
        {
        return !myFirstPositionSet || aTouchEvent.isPress();
        }

    private void setFirstPosition()
        {
        updateLastPosition();
        myFirstPositionSet = true;
        }

    private void updateLastPosition()
        {
        myLastPositionTimestamp = aTouchEvent.timestamp();
        myLastPosition.x = aTouchEvent.getX();
        myLastPosition.y = aTouchEvent.getY();
        }

    private void updateDeltasForThisEvent()
        {
        deltaX = aTouchEvent.getX() - myLastPosition.x;
        deltaY = aTouchEvent.getY() - myLastPosition.y;
        deltaTimestamp = aTouchEvent.timestamp() - myLastPositionTimestamp;
        }

    public final void onSliding()
        {
        if ( Math.abs( deltaX ) >= slideMoveThresholdInPixels || Math.abs( deltaY ) >= slideMoveThresholdInPixels )
            {
            updateSlideDeltas( slideMoveThresholdInPixels );
            }
        else if ( deltaTimestamp > newSlideStartThresholdInMillis || aTouchEvent.isRelease() )
            {
            startNewSlide();
            }
        }

    public final void updateSlideDeltas( final int aThresholdInPixels )
        {
        if ( Math.abs( deltaX ) >= aThresholdInPixels )
            {
            mySlideDeltas.x += deltaX;

            myLastPositionTimestamp = aTouchEvent.timestamp();
            myLastPosition.x = aTouchEvent.getX();

            final int slideStepsX = processRawDelta( mySlideDeltas.x );
            slideSteps.x += slideStepsX - myLastSlideSteps.x;
            myLastSlideSteps.x = slideStepsX;
            }
        if ( Math.abs( deltaY ) >= aThresholdInPixels )
            {
            mySlideDeltas.y += deltaY;

            myLastPositionTimestamp = aTouchEvent.timestamp();
            myLastPosition.y = aTouchEvent.getY();

            final int slideStepsY = processRawDelta( mySlideDeltas.y );
            slideSteps.y += slideStepsY - myLastSlideSteps.y;
            myLastSlideSteps.y = slideStepsY;
            }
        }

    private int processRawDelta( final int aRawDelta )
        {
        final int sign = aRawDelta < 0 ? -1 : +1;

        int rawDelta = Math.abs( aRawDelta );
        if ( rawDelta <= initialStepThresholdInPixels ) return 0;
        rawDelta -= initialStepThresholdInPixels;

        // if more than the initial step size..
        if ( rawDelta > stepSizeInPixels.width )
            {
            // then subtract the additionalStepThresholdInPixels
            rawDelta = Math.max( stepSizeInPixels.width, rawDelta - additionalStepThresholdInPixels );
            }

        return sign * rawDelta / stepSizeInPixels.width;
        }

    private void startNewSlide()
        {
        mySlideStarted = false;
        myFirstPositionSet = false;

        updateLastPosition();

        mySlideDeltas.x = mySlideDeltas.y = 0;
        myLastSlideSteps.x = myLastSlideSteps.y = 0;
        }

    public final void onSlideNotStartedYet()
        {
        if ( !slideStartConditionsMet() ) return;

        startNewSlide();

        mySlideDeltas.x += deltaX;
        mySlideDeltas.y += deltaY;
        mySlideStarted = true;
        myFirstPositionSet = true;

        final int thresholdInPixels = slideStartThresholdInPixels;
        updateSlideDeltas( thresholdInPixels );
        }

    private boolean slideStartConditionsMet()
        {
        final boolean slideTimeLongEnough = deltaTimestamp > slideStartThresholdInMillis;
        final boolean horizontalMovementBigEnough = Math.abs( deltaX ) >= slideStartThresholdInPixels;
        final boolean verticalMovementBigEnough = Math.abs( deltaY ) >= slideStartThresholdInPixels;
        return slideTimeLongEnough && ( horizontalMovementBigEnough || verticalMovementBigEnough );
        }


    private int deltaX;

    private int deltaY;

    private long deltaTimestamp;

    private TouchEvent aTouchEvent;

    private boolean mySlideStarted;

    private boolean myFirstPositionSet;

    private long myLastPositionTimestamp;

    private final Position mySlideDeltas = new Position();

    private final Position myLastPosition = new Position();

    private final Position myLastSlideSteps = new Position();
    }
