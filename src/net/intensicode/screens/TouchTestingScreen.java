package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Assert;

public final class TouchTestingScreen extends MultiScreen
    {
    public TouchTestingScreen( final FontGenerator aTitleFont, final FontGenerator aTextFont )
        {
        myTitleFont = aTitleFont;
        myTextFont = aTextFont;
        }

    public final TouchTestingScreen shareSoftkeys( final SoftkeysScreen aSoftkeys )
        {
        mySoftkeys = aSoftkeys;
        return this;
        }

    // From AbstractScreen

    public final void onInitOnce() throws Exception
        {
        Assert.notNull( "shared SoftkeysScreen", mySoftkeys );

        addScreen( new ClearScreen() );
        addScreen( mySoftkeys );

        mySliderConfiguration.setSensitivityPreset( TouchSliderConfiguration.SENSITIVITY_MEDIUM );
        myTouchSlider = new TouchSlider( mySliderConfiguration );
        myTouchSlider.stepSizeInPixels.setTo( 18, 18 );

        myGesturesConfiguration.setSensitivityPreset( TouchGesturesConfiguration.SENSITIVITY_MEDIUM );
        myTouchGestures = new TouchGestures( myGesturesConfiguration, system().platform );
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
            myTouchGestures.optionalHotzone.y = height;

            myTouchGestures.optionalHotzone.x = halfWidth;
            myTouchGestures.optionalHotzone.y = 0;
            myTouchGestures.optionalHotzone.width = halfWidth;
            myTouchGestures.optionalHotzone.height = height;
            }
        }

    public final void onControlTick() throws Exception
        {
        super.onControlTick();

        if ( keys().checkLeftSoftAndConsume() )
            {
            }
        if ( keys().checkRightSoftAndConsume() )
            {
            stack().popScreen( this );
            }
        }

    public final void onDrawFrame()
        {
        super.onDrawFrame();

        }


    private SoftkeysScreen mySoftkeys;

    private TouchSlider myTouchSlider;

    private TouchGestures myTouchGestures;

    private final FontGenerator myTextFont;

    private final FontGenerator myTitleFont;

    private final TouchSliderConfiguration mySliderConfiguration = new TouchSliderConfiguration();

    private final TouchGesturesConfiguration myGesturesConfiguration = new TouchGesturesConfiguration();
    }
