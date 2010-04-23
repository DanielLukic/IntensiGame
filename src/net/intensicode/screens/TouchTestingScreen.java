package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class TouchTestingScreen extends MultiScreen
    {
    public TouchTestingScreen( final FontGenerator aFont )
        {
        myFont = aFont;
        }

    // From AbstractScreen

    public final void onInitOnce() throws Exception
        {
        addScreen( new ClearScreen( 0xFF95c03c ) );
        addScreen( mySoftkeys = new SoftkeysScreen( myFont ) );

        myTouchSlider = new TouchSlider( mySliderConfiguration );
        myTouchSlider.stepSizeInPixels.setTo( 18, 18 );
        touch().addListener( myTouchSlider );

        myTouchGestures = new TouchGestures( myGesturesConfiguration, system().platform );
        touch().addListener( myTouchGestures );

        mySliderSensitivity = TouchSliderConfiguration.SENSITIVITY_MEDIUM;
        myGesturesSensitivity = TouchGesturesConfiguration.SENSITIVITY_MEDIUM;
        }

    public final void onInitEverytime() throws Exception
        {
        mySoftkeys.setSoftkeys( "SENSITITIVY", "BACK" );

        final int width = screen().width();
        final int height = screen().height();
        if ( width < height )
            {
            final int halfHeight = height / 2;

            myTouchSlider.touchableArea.x = 0;
            myTouchSlider.touchableArea.y = 0;
            myTouchSlider.touchableArea.width = width;
            myTouchGestures.optionalHotzone.y = halfHeight;

            myTouchGestures.optionalHotzone.x = 0;
            myTouchGestures.optionalHotzone.y = halfHeight;
            myTouchGestures.optionalHotzone.width = width;
            myTouchGestures.optionalHotzone.height = halfHeight;
            }
        else
            {
            final int halfWidth = width / 2;

            myTouchSlider.touchableArea.x = 0;
            myTouchSlider.touchableArea.y = 0;
            myTouchSlider.touchableArea.width = halfWidth;
            myTouchSlider.touchableArea.height = height;

            myTouchGestures.optionalHotzone = new Rectangle();
            myTouchGestures.optionalHotzone.x = halfWidth;
            myTouchGestures.optionalHotzone.y = 0;
            myTouchGestures.optionalHotzone.width = halfWidth;
            myTouchGestures.optionalHotzone.height = height;
            }
        }

    public final void onControlTick() throws Exception
        {
        super.onControlTick();

        mySliderConfiguration.setSensitivityPreset( mySliderSensitivity );
        myGesturesConfiguration.setSensitivityPreset( myGesturesSensitivity );

        if ( keys().checkLeftSoftAndConsume() )
            {
            if ( mySliderSensitivity < TouchSliderConfiguration.MAX_VALUE ) mySliderSensitivity++;
            else mySliderSensitivity = 0;
            if ( myGesturesSensitivity < TouchGesturesConfiguration.MAX_VALUE ) myGesturesSensitivity++;
            else myGesturesSensitivity = 0;
            }
        if ( keys().checkRightSoftAndConsume() )
            {
            stack().popScreen( this );
            }
        }

    public final void onDrawFrame()
        {
        super.onDrawFrame();

        graphics().setColorRGB24( 0xFFFFFF );

        final Rectangle r1 = myTouchSlider.touchableArea;
        graphics().drawRect( r1.x, r1.y, r1.width, r1.height );

        final Rectangle r2 = myTouchGestures.optionalHotzone;
        graphics().drawRect( r2.x, r2.y, r2.width, r2.height );

        final String sliderLabel = getSliderSensitivityLabel();
        setBlitPositionToCenter( myTouchSlider.touchableArea );
        myFont.blitString( graphics(), sliderLabel, myBlitPos, FontGenerator.CENTER );

        myBlitPos.y += myFont.charHeight();
        myFont.blitString( graphics(), myTouchSlider.slideSteps.toString(), myBlitPos, FontGenerator.CENTER );

        final String gesturesLabel = getGesturesSensitivityLabel();
        setBlitPositionToCenter( myTouchGestures.optionalHotzone );
        myFont.blitString( graphics(), gesturesLabel, myBlitPos, FontGenerator.CENTER );

        myBlitPos.y += myFont.charHeight();
        myFont.blitString( graphics(), myTouchGestures.gesture.toString(), myBlitPos, FontGenerator.CENTER );
        }

    private void setBlitPositionToCenter( final Rectangle aRectangle )
        {
        myBlitPos.x = aRectangle.x + aRectangle.width / 2;
        myBlitPos.y = aRectangle.y + aRectangle.height / 2;
        }

    private String getSliderSensitivityLabel()
        {
        return TouchSliderConfiguration.SENSITIVITY_STRING_VALUES[ mySliderSensitivity ];
        }

    private String getGesturesSensitivityLabel()
        {
        return TouchGesturesConfiguration.SENSITIVITY_STRING_VALUES[ myGesturesSensitivity ];
        }


    private int mySliderSensitivity;

    private int myGesturesSensitivity;

    private SoftkeysScreen mySoftkeys;

    private TouchSlider myTouchSlider;

    private TouchGestures myTouchGestures;

    private final FontGenerator myFont;

    private final Position myBlitPos = new Position();

    private final TouchSliderConfiguration mySliderConfiguration = new TouchSliderConfiguration();

    private final TouchGesturesConfiguration myGesturesConfiguration = new TouchGesturesConfiguration();
    }
