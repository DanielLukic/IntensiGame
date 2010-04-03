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
        final Rectangle hotzone = optionalHotzone != null ? optionalHotzone : touchableArea;

        final boolean touchInside = hotzone == null || hotzone.contains( aTouchEvent.getX(), aTouchEvent.getY() );
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

        final long delta = aTouchEvent.timestamp() - myLastPositionTimestamp;

        if ( mySlideStarted )
            {
            if ( Math.abs( deltaX ) >= slideMoveThresholdInPixels || Math.abs( deltaY ) >= slideMoveThresholdInPixels )
                {
                if ( Math.abs( deltaX ) >= slideMoveThresholdInPixels )
                    {
                    mySlideDeltas.x += deltaX;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.x = aTouchEvent.getX();

                    final int slideStepsX = processRawDelta( mySlideDeltas.x );
                    slideSteps.x += slideStepsX - myLastSlideSteps.x;
                    myLastSlideSteps.x = slideStepsX;
                    }
                if ( Math.abs( deltaY ) >= slideMoveThresholdInPixels )
                    {
                    mySlideDeltas.y += deltaY;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.y = aTouchEvent.getY();

                    final int slideStepsY = processRawDelta( mySlideDeltas.y );
                    slideSteps.y += slideStepsY - myLastSlideSteps.y;
                    myLastSlideSteps.y = slideStepsY;
                    }
                }
            else if ( delta > newSlideStartThresholdInMillis )
                {
                mySlideStarted = false;
                myFirstPositionSet = false;

                myLastPositionTimestamp = aTouchEvent.timestamp();
                myLastPosition.x = aTouchEvent.getX();
                myLastPosition.y = aTouchEvent.getY();

                mySlideDeltas.x = mySlideDeltas.y = 0;
                myLastSlideSteps.x = myLastSlideSteps.y = 0;
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
                    }
            }
        else
            {
            if ( delta > slideStartThresholdInMillis )
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
                    myFirstPositionSet = true;

                    if ( Math.abs( deltaX ) >= slideStartThresholdInPixels )
                        {
                        mySlideDeltas.x += deltaX;

                        myLastPositionTimestamp = aTouchEvent.timestamp();
                        myLastPosition.x = aTouchEvent.getX();

                        final int slideStepsX = processRawDelta( mySlideDeltas.x );
                        slideSteps.x += slideStepsX - myLastSlideSteps.x;
                        myLastSlideSteps.x = slideStepsX;
                        }

                    if ( Math.abs( deltaY ) >= slideStartThresholdInPixels )
                        {
                        mySlideDeltas.y += deltaY;

                        myLastPositionTimestamp = aTouchEvent.timestamp();
                        myLastPosition.y = aTouchEvent.getY();

                        final int slideStepsY = processRawDelta( mySlideDeltas.y );
                        slideSteps.y += slideStepsY - myLastSlideSteps.y;
                        myLastSlideSteps.y = slideStepsY;
                        }
                    }
                }
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


    private boolean myFirstPositionSet;

    private boolean mySlideStarted;

    private long myLastPositionTimestamp;

    private final Position mySlideDeltas = new Position();

    private final Position myLastPosition = new Position();

    private final Position myLastSlideSteps = new Position();
    }
