//#condition TOUCH

package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;
import net.intensicode.touch.*;

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

        mySliderConfiguration.initDefaults();
        myGesturesConfiguration.initDefaults();

        myTouchSlider = new TouchSlider( mySliderConfiguration );
        myTouchSlider.stepSizeInPixels.setTo( 18, 18 );
        touch().addListener( myTouchSlider );

        myTouchGestures = new TouchGestures( myGesturesConfiguration, system().platform );
        touch().addListener( myTouchGestures );

        mySliderSensitivity = mySliderConfiguration.presets.length / 2;
        myGesturesSensitivity = myGesturesConfiguration.presets.length / 2;
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
            myTouchSlider.touchableArea.height = halfHeight;
            myTouchSlider.touchableArea.applyOutsets( -halfHeight / 10 );

            myTouchGestures.optionalHotzone = new Rectangle();
            myTouchGestures.optionalHotzone.x = 0;
            myTouchGestures.optionalHotzone.y = halfHeight;
            myTouchGestures.optionalHotzone.width = width;
            myTouchGestures.optionalHotzone.height = halfHeight;
            myTouchGestures.optionalHotzone.applyOutsets( -halfHeight / 10 );
            }
        else
            {
            final int halfWidth = width / 2;

            myTouchSlider.touchableArea.x = 0;
            myTouchSlider.touchableArea.y = 0;
            myTouchSlider.touchableArea.width = halfWidth;
            myTouchSlider.touchableArea.height = height;
            myTouchSlider.touchableArea.applyOutsets( -halfWidth / 10 );

            myTouchGestures.optionalHotzone = new Rectangle();
            myTouchGestures.optionalHotzone.x = halfWidth;
            myTouchGestures.optionalHotzone.y = 0;
            myTouchGestures.optionalHotzone.width = halfWidth;
            myTouchGestures.optionalHotzone.height = height;
            myTouchGestures.optionalHotzone.applyOutsets( -halfWidth / 10 );
            }
        }

    public final void onControlTick() throws Exception
        {
        super.onControlTick();

        mySliderConfiguration.setTo( mySliderConfiguration.presets[ mySliderSensitivity ] );
        myGesturesConfiguration.setTo( myGesturesConfiguration.presets[ myGesturesSensitivity ] );

        if ( keys().checkLeftSoftAndConsume() )
            {
            if ( mySliderSensitivity < mySliderConfiguration.presets.length ) mySliderSensitivity++;
            else mySliderSensitivity = 0;
            if ( myGesturesSensitivity < myGesturesConfiguration.presets.length ) myGesturesSensitivity++;
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
        return mySliderConfiguration.label;
        }

    private String getGesturesSensitivityLabel()
        {
        return myGesturesConfiguration.label;
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
