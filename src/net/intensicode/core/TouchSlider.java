//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchSlider implements TouchEventListener
    {
    public final Rectangle touchableArea = new Rectangle();

    public Rectangle optionalHotzone;

    public int slideStartThresholdInMillis = 1000 / 20;

    public int slideStartThresholdInPixels = 5;

    public int slideMoveThresholdInPixels = 3;

    public int newSlideStartThresholdInMillis = 1000 / 20;

    public int initialStepThresholdInPixels = 4;

    public int additionalStepThresholdInPixels = 4;

    public final Size stepSizeInPixels = new Size( 1, 1 );

    public final Position slideSteps = new Position();


    public final void clearSlideSteps()
        {
        slideSteps.x = slideSteps.y = 0;
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        final Rectangle hotzone = optionalHotzone != null ? optionalHotzone : touchableArea;

        final boolean touchInside = hotzone.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        if ( !touchInside ) return;

        if ( !myFirstPositionSet || aTouchEvent.isPress() )
            {
            myLastPositionTimestamp = aTouchEvent.timestamp();
            myLastPosition.x = aTouchEvent.getX();
            myLastPosition.y = aTouchEvent.getY();
            myFirstPositionSet = true;
            }

        final int deltaX = aTouchEvent.getX() - myLastPosition.x;
        final int deltaY = aTouchEvent.getY() - myLastPosition.y;

        if ( mySlideStarted )
            {
            if ( Math.abs( deltaX ) >= slideMoveThresholdInPixels || Math.abs( deltaY ) >= slideMoveThresholdInPixels )
                {
                mySlideDeltas.x += deltaX;
                mySlideDeltas.y += deltaY;

                myLastPositionTimestamp = aTouchEvent.timestamp();
                myLastPosition.x = aTouchEvent.getX();
                myLastPosition.y = aTouchEvent.getY();

                final int slideStepsX = processRawDelta( mySlideDeltas.x );
                final int slideStepsY = processRawDelta( mySlideDeltas.y );
                slideSteps.x += slideStepsX - myLastSlideSteps.x;
                slideSteps.y += slideStepsY - myLastSlideSteps.y;
                myLastSlideSteps.x = slideStepsX;
                myLastSlideSteps.y = slideStepsY;
                }
            else if ( aTouchEvent.timestamp() - myLastPositionTimestamp > newSlideStartThresholdInMillis )
                {
                mySlideStarted = false;
                myFirstPositionSet = false;

                myLastPositionTimestamp = aTouchEvent.timestamp();
                myLastPosition.x = aTouchEvent.getX();
                myLastPosition.y = aTouchEvent.getY();

                mySlideDeltas.x = mySlideDeltas.y = 0;
                myLastSlideSteps.x = myLastSlideSteps.y = 0;

                Log.debug( "new slide started because of newSlideStartThresholdInMillis" );
                }
            else if ( aTouchEvent.isRelease() )
                    {
                    mySlideStarted = false;
                    myFirstPositionSet = false;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.x = aTouchEvent.getX();
                    myLastPosition.y = aTouchEvent.getY();

                    mySlideDeltas.x = mySlideDeltas.y = 0;
                    myLastSlideSteps.x = myLastSlideSteps.y = 0;

                    Log.debug( "new slide started because of isRelease" );
                    }
            }
        else
            {
            if ( aTouchEvent.timestamp() - myLastPositionTimestamp > slideStartThresholdInMillis )
                {
                if ( Math.abs( deltaX ) >= slideStartThresholdInPixels || Math.abs( deltaY ) >= slideStartThresholdInPixels )
                    {
                    mySlideStarted = false;
                    myFirstPositionSet = false;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.x = aTouchEvent.getX();
                    myLastPosition.y = aTouchEvent.getY();

                    mySlideDeltas.x = mySlideDeltas.y = 0;
                    myLastSlideSteps.x = myLastSlideSteps.y = 0;

                    mySlideDeltas.x += deltaX;
                    mySlideDeltas.y += deltaY;
                    mySlideStarted = true;

                    Log.debug( "initial slide started" );
                    }
                }
            }
        }

    private int processRawDelta( final int aRawDelta )
        {
        final int sign = aRawDelta < 0 ? -1 : +1;

        int rawDelta = Math.abs( aRawDelta ) ;
        if ( rawDelta <= initialStepThresholdInPixels )
            {
            return 0;
            }
        rawDelta -= initialStepThresholdInPixels;

        // if more than the initial step size..
        if ( rawDelta > stepSizeInPixels.width )
            {
            // then subtract the additionalStepThresholdInPixels
            rawDelta = Math.max( stepSizeInPixels.width, rawDelta - additionalStepThresholdInPixels );
            }

        return sign * rawDelta / stepSizeInPixels.width;
        }


    private boolean myFirstPositionSet;

    private boolean mySlideStarted;

    private long myLastPositionTimestamp;

    private final Position mySlideDeltas = new Position();

    private final Position myLastPosition = new Position();

    private final Position myLastSlideSteps = new Position();
    }
