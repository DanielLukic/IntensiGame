//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchSlider implements TouchEventListener
    {
    public final TouchSliderConfiguration configuration;

    public final Rectangle touchableArea = new Rectangle();

    public final Size stepSizeInPixels = new Size( 1, 1 );

    public final Position slideSteps = new Position();

    public Rectangle optionalHotzone;


    public TouchSlider( final TouchSliderConfiguration aConfiguration )
        {
        configuration = aConfiguration;
        }

    public final void clearSlideSteps()
        {
        slideSteps.x = slideSteps.y = 0;
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        if ( aTouchEvent.isRelease() ) startNewSlide();

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

    private void setCurrentEvent( final TouchEvent aTouchEvent )
        {
        this.myTouchEvent = aTouchEvent;
        }

    private boolean isFirstPosition()
        {
        return !myFirstPositionSet || myTouchEvent.isPress();
        }

    private void setFirstPosition()
        {
        updateLastPosition();
        myFirstPositionSet = true;
        }

    private void updateLastPosition()
        {
        myLastPositionTimestamp = myTouchEvent.timestamp();
        myLastPosition.x = myTouchEvent.getX();
        myLastPosition.y = myTouchEvent.getY();
        }

    private void updateDeltasForThisEvent()
        {
        myDeltaX = myTouchEvent.getX() - myLastPosition.x;
        myDeltaY = myTouchEvent.getY() - myLastPosition.y;
        myDeltaTimestamp = myTouchEvent.timestamp() - myLastPositionTimestamp;
        }

    private void onSliding()
        {
        if ( Math.abs( myDeltaX ) >= configuration.slideMoveThresholdInPixels || Math.abs( myDeltaY ) >= configuration.slideMoveThresholdInPixels )
            {
            updateSlideDeltas( configuration.slideMoveThresholdInPixels );
            }
        else if ( myDeltaTimestamp > configuration.newSlideStartThresholdInMillis || myTouchEvent.isRelease() )
            {
            startNewSlide();
            }
        }

    private void updateSlideDeltas( final int aThresholdInPixels )
        {
        if ( Math.abs( myDeltaX ) >= aThresholdInPixels )
            {
            mySlideDeltas.x += myDeltaX;

            myLastPositionTimestamp = myTouchEvent.timestamp();
            myLastPosition.x = myTouchEvent.getX();

            final int slideStepsX = processRawDelta( mySlideDeltas.x );
            slideSteps.x += slideStepsX - myLastSlideSteps.x;
            myLastSlideSteps.x = slideStepsX;
            }
        if ( Math.abs( myDeltaY ) >= aThresholdInPixels )
            {
            mySlideDeltas.y += myDeltaY;

            myLastPositionTimestamp = myTouchEvent.timestamp();
            myLastPosition.y = myTouchEvent.getY();

            final int slideStepsY = processRawDelta( mySlideDeltas.y );
            slideSteps.y += slideStepsY - myLastSlideSteps.y;
            myLastSlideSteps.y = slideStepsY;
            }
        }

    private int processRawDelta( final int aRawDelta )
        {
        final int sign = aRawDelta < 0 ? -1 : +1;

        int rawDelta = Math.abs( aRawDelta );
        if ( rawDelta <= configuration.initialStepThresholdInPixels ) return 0;
        rawDelta -= configuration.initialStepThresholdInPixels;

        // if more than the initial step size..
        if ( rawDelta > stepSizeInPixels.width )
            {
            // then subtract the additionalStepThresholdInPixels
            rawDelta = Math.max( stepSizeInPixels.width, rawDelta - configuration.additionalStepThresholdInPixels );
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

    private void onSlideNotStartedYet()
        {
        if ( !slideStartConditionsMet() ) return;

        startNewSlide();

        mySlideDeltas.x += myDeltaX;
        mySlideDeltas.y += myDeltaY;
        mySlideStarted = true;
        myFirstPositionSet = true;

        final int thresholdInPixels = configuration.slideStartThresholdInPixels;
        updateSlideDeltas( thresholdInPixels );
        }

    private boolean slideStartConditionsMet()
        {
        final boolean slideTimeLongEnough = myDeltaTimestamp > configuration.slideStartThresholdInMillis;
        final boolean horizontalMovementBigEnough = Math.abs( myDeltaX ) >= configuration.slideStartThresholdInPixels;
        final boolean verticalMovementBigEnough = Math.abs( myDeltaY ) >= configuration.slideStartThresholdInPixels;
        return slideTimeLongEnough && ( horizontalMovementBigEnough || verticalMovementBigEnough );
        }


    private int myDeltaX;

    private int myDeltaY;

    private long myDeltaTimestamp;

    private TouchEvent myTouchEvent;

    private boolean mySlideStarted;

    private boolean myFirstPositionSet;

    private long myLastPositionTimestamp;

    private final Position mySlideDeltas = new Position();

    private final Position myLastPosition = new Position();

    private final Position myLastSlideSteps = new Position();
    }
