//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchSlider implements TouchEventListener
    {
    public final Rectangle touchableArea = new Rectangle();

    public Rectangle optionalHotzone;

    public int slideStartThresholdInMillis = 1000 / 20;

    public int slideStartThresholdInPixels = 5;

    public int slideMoveThresholdInPixels = 5;

    public int newSlideStartThresholdInMillis = 1000 / 10;

    public final Position slideDeltas = new Position();


    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        final Rectangle hotzone = optionalHotzone != null ? optionalHotzone : touchableArea;

        final boolean touchInside = hotzone.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        if ( !touchInside ) return;

        if ( aTouchEvent.isPress() && myFirstPositionSet )
            {
            throw new RuntimeException( "should not be, right? " );
            }

        if ( !aTouchEvent.isPress() && !myFirstPositionSet )
            {
            throw new RuntimeException( "should not be, right? " );
            }

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
                slideDeltas.x += deltaX;
                slideDeltas.y += deltaY;

                myLastPositionTimestamp = aTouchEvent.timestamp();
                myLastPosition.x = aTouchEvent.getX();
                myLastPosition.y = aTouchEvent.getY();
                }
            else if ( aTouchEvent.timestamp() - myLastPositionTimestamp > newSlideStartThresholdInMillis )
                {
                mySlideStarted = false;
                myFirstPositionSet = false;

                myLastPositionTimestamp = aTouchEvent.timestamp();
                myLastPosition.x = aTouchEvent.getX();
                myLastPosition.y = aTouchEvent.getY();
                }
            else if ( aTouchEvent.isRelease() )
                    {
                    mySlideStarted = false;
                    myFirstPositionSet = false;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.x = aTouchEvent.getX();
                    myLastPosition.y = aTouchEvent.getY();
                    }
            }
        else
            {
            if ( aTouchEvent.timestamp() - myLastPositionTimestamp > slideStartThresholdInMillis )
                {
                if ( Math.abs( deltaX ) >= slideStartThresholdInPixels || Math.abs( deltaY ) >= slideStartThresholdInPixels )
                    {
                    slideDeltas.x += deltaX;
                    slideDeltas.y += deltaY;
                    mySlideStarted = true;

                    myLastPositionTimestamp = aTouchEvent.timestamp();
                    myLastPosition.x = aTouchEvent.getX();
                    myLastPosition.y = aTouchEvent.getY();
                    }
                }
            }
        }


    private boolean myFirstPositionSet;

    private boolean mySlideStarted;

    private long myLastPositionTimestamp;

    private final Position myLastPosition = new Position();
    }
