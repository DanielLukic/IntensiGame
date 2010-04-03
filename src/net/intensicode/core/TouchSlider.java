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
            Log.debug( "init slide" );
            Log.debug( "myFirstPositionSet? " + myFirstPositionSet );
            Log.debug( "isPress? " + aTouchEvent.isPress() );
            myLastPositionTimestamp = aTouchEvent.timestamp();
            myLastPosition.x = aTouchEvent.getX();
            myLastPosition.y = aTouchEvent.getY();
            myFirstPositionSet = true;
            }

        final int deltaX = aTouchEvent.getX() - myLastPosition.x;
        final int deltaY = aTouchEvent.getY() - myLastPosition.y;

        if ( mySlideStarted )
            {
            Log.debug( "in slide started" );
            if ( Math.abs( deltaX ) >= slideMoveThresholdInPixels || Math.abs( deltaY ) >= slideMoveThresholdInPixels )
                {
                Log.debug( "slideMoveThresholdInPixels reached" );

                if ( Math.abs( deltaX ) >= slideMoveThresholdInPixels )
                    {
                    Log.debug( "slideMoveThresholdInPixels x reached" );

                    mySlideDeltas.x += deltaX;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.x = aTouchEvent.getX();

                    final int slideStepsX = processRawDelta( mySlideDeltas.x );
                    slideSteps.x += slideStepsX - myLastSlideSteps.x;
                    myLastSlideSteps.x = slideStepsX;
                    }
                if ( Math.abs( deltaY ) >= slideMoveThresholdInPixels )
                    {
                    Log.debug( "slideMoveThresholdInPixels y reached" );

                    mySlideDeltas.y += deltaY;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.y = aTouchEvent.getY();

                    final int slideStepsY = processRawDelta( mySlideDeltas.y );
                    slideSteps.y += slideStepsY - myLastSlideSteps.y;
                    myLastSlideSteps.y = slideStepsY;
                    }
                }
            else if ( aTouchEvent.timestamp() - myLastPositionTimestamp > newSlideStartThresholdInMillis )
                {
                Log.debug( "newSlideStartThresholdInMillis reached - slide break" );

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
                    Log.debug( "slide done because of release event" );

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
            Log.debug( "in slide not started yet" );
            if ( aTouchEvent.timestamp() - myLastPositionTimestamp > slideStartThresholdInMillis )
                {
                Log.debug( "slideStartThresholdInMillis reached" );
                if ( Math.abs( deltaX ) >= slideStartThresholdInPixels || Math.abs( deltaY ) >= slideStartThresholdInPixels )
                    {
                    Log.debug( "slideStartThresholdInPixels reached" );

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

                    Log.debug( "initial slide started" );

                    if ( Math.abs( deltaX ) >= slideStartThresholdInPixels )
                        {
                        Log.debug( "slideMoveThresholdInPixels x reached" );

                        mySlideDeltas.x += deltaX;

                        myLastPositionTimestamp = aTouchEvent.timestamp();
                        myLastPosition.x = aTouchEvent.getX();

                        final int slideStepsX = processRawDelta( mySlideDeltas.x );
                        slideSteps.x += slideStepsX - myLastSlideSteps.x;
                        myLastSlideSteps.x = slideStepsX;
                        }

                    if ( Math.abs( deltaY ) >= slideStartThresholdInPixels )
                        {
                        Log.debug( "slideMoveThresholdInPixels y reached" );

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
        if ( rawDelta <= initialStepThresholdInPixels )
            {
            Log.debug( "processRawDelta {} => 0", aRawDelta );
            return 0;
            }
        rawDelta -= initialStepThresholdInPixels;

        // if more than the initial step size..
        if ( rawDelta > stepSizeInPixels.width )
            {
            // then subtract the additionalStepThresholdInPixels
            rawDelta = Math.max( stepSizeInPixels.width, rawDelta - additionalStepThresholdInPixels );
            }

        final int result = sign * rawDelta / stepSizeInPixels.width;
        Log.debug( "processRawDelta {} => {}", aRawDelta, result );
        return result;
        }


    private boolean myFirstPositionSet;

    private boolean mySlideStarted;

    private long myLastPositionTimestamp;

    private final Position mySlideDeltas = new Position();

    private final Position myLastPosition = new Position();

    private final Position myLastSlideSteps = new Position();
    }
