//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.*;

public final class TouchSlider implements TouchEventListener
    {
    public final Rectangle touchableArea = new Rectangle();

    public final Size stepSizeInPixels = new Size( 1, 1 );

    public final Position slideSteps = new Position();

    public Rectangle optionalHotzone;


    public TouchSlider( final TouchSliderConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    public final void clearSlideSteps()
        {
        slideSteps.x = slideSteps.y = 0;
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        setCurrentEvent( aTouchEvent );

        if ( isFirstPosition() ) setFirstPosition();

        if ( !isInsideHotzone( aTouchEvent ) ) return;

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
        myTouchEvent = aTouchEvent;
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
        if ( Math.abs( myDeltaX ) >= myConfiguration.slideMoveThresholdInPixels || Math.abs( myDeltaY ) >= myConfiguration.slideMoveThresholdInPixels )
            {
            updateSlideDeltas( myConfiguration.slideMoveThresholdInPixels );
            }
        else if ( myDeltaTimestamp > myConfiguration.newSlideStartThresholdInMillis || myTouchEvent.isRelease() )
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

        float rawDelta = Math.abs( aRawDelta );
        if ( rawDelta <= myConfiguration.initialStepThresholdInPixels ) return 0;
        rawDelta -= myConfiguration.initialStepThresholdInPixels;

        final float stepPixels = stepSizeInPixels.width;

        // if more than the initial step size..
        if ( rawDelta > stepSizeInPixels.width )
            {
            // then subtract the additionalStepThresholdInPixels
            rawDelta = Math.max( stepPixels, rawDelta - myConfiguration.additionalStepThresholdInPixels );
            }

        return (int) ( sign * rawDelta / stepPixels );
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

        final int thresholdInPixels = myConfiguration.slideStartThresholdInPixels;
        updateSlideDeltas( thresholdInPixels );
        }

    private boolean slideStartConditionsMet()
        {
        final boolean slideTimeLongEnough = myDeltaTimestamp > myConfiguration.slideStartThresholdInMillis;
        final boolean horizontalMovementBigEnough = Math.abs( myDeltaX ) >= myConfiguration.slideStartThresholdInPixels;
        final boolean verticalMovementBigEnough = Math.abs( myDeltaY ) >= myConfiguration.slideStartThresholdInPixels;
        return slideTimeLongEnough && ( horizontalMovementBigEnough || verticalMovementBigEnough );
        }


    private int myDeltaX;

    private int myDeltaY;

    private long myDeltaTimestamp;

    private TouchEvent myTouchEvent;

    private boolean mySlideStarted;

    private boolean myFirstPositionSet;

    private long myLastPositionTimestamp;

    private final TouchSliderConfiguration myConfiguration;

    private final Position mySlideDeltas = new Position();

    private final Position myLastPosition = new Position();

    private final Position myLastSlideSteps = new Position();
    }
