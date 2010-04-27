//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.Log;

public final class TouchSliderConfiguration
    {
    public static final String[] SENSITIVITY_STRING_VALUES = { "VERY LOW", "LOW", "MEDIUM", "HIGH", "VERY HIGH" };

    public static final int SENSITIVITY_VERY_LOW = 0;

    public static final int SENSITIVITY_LOW = 1;

    public static final int SENSITIVITY_MEDIUM = 2;

    public static final int SENSITIVITY_HIGH = 3;

    public static final int SENSITIVITY_VERY_HIGH = 4;

    public static final int MIN_VALUE = SENSITIVITY_VERY_LOW;

    public static final int MAX_VALUE = SENSITIVITY_VERY_HIGH;

    public static final float TARGET_SMOOTHING = 0.6f;


    public void setDeviceParameters( final DirectScreen aScreen )
        {
        devicePixelFactorX = 1f;
        devicePixelFactorY = 1f;

        final float xSmoothing = aScreen.getTargetWidth() / (float) aScreen.getNativeWidth();
        final float ySmoothing = aScreen.getTargetHeight() / (float) aScreen.getNativeHeight();
        Log.info( "implicit native -> target factor: {}/{}", xSmoothing, ySmoothing );

        devicePixelFactorX = TARGET_SMOOTHING / xSmoothing;
        devicePixelFactorY = TARGET_SMOOTHING / ySmoothing;
        Log.info( "device pixel factor: {}/{}", devicePixelFactorX, devicePixelFactorY );

        devicePixelFactor = ( devicePixelFactorX + devicePixelFactorY ) / 2;
        Log.info( "device pixel factor (avg): {}", devicePixelFactor );

        final float xFactor = devicePixelFactorX * xSmoothing;
        final float yFactor = devicePixelFactorY * ySmoothing;
        Log.info( "resulting factor: {}/{}", xFactor, yFactor );
        }

    public void setSensitivityPreset( final int aSensitivityId )
        {
        switch ( aSensitivityId )
            {
            default:
            case SENSITIVITY_VERY_LOW:
                slideStartThresholdInMillis = 40;
                slideStartThresholdInPixels = 25;
                slideMoveThresholdInPixels = 20;
                newSlideStartThresholdInMillis = 50;
                initialStepThresholdInPixels = 25;
                additionalStepThresholdInPixels = 120;
                break;
            case SENSITIVITY_LOW:
                slideStartThresholdInMillis = 30;
                slideStartThresholdInPixels = 25;
                slideMoveThresholdInPixels = 15;
                newSlideStartThresholdInMillis = 45;
                initialStepThresholdInPixels = 20;
                additionalStepThresholdInPixels = 100;
                break;
            case SENSITIVITY_MEDIUM:
                slideStartThresholdInMillis = 25;
                slideStartThresholdInPixels = 20;
                slideMoveThresholdInPixels = 10;
                newSlideStartThresholdInMillis = 40;
                initialStepThresholdInPixels = 20;
                additionalStepThresholdInPixels = 100;
                break;
            case SENSITIVITY_HIGH:
                slideStartThresholdInMillis = 25;
                slideStartThresholdInPixels = 15;
                slideMoveThresholdInPixels = 10;
                newSlideStartThresholdInMillis = 35;
                initialStepThresholdInPixels = 20;
                additionalStepThresholdInPixels = 60;
                break;
            case SENSITIVITY_VERY_HIGH:
                slideStartThresholdInMillis = 25;
                slideStartThresholdInPixels = 10;
                slideMoveThresholdInPixels = 8;
                newSlideStartThresholdInMillis = 30;
                initialStepThresholdInPixels = 15;
                additionalStepThresholdInPixels = 40;
                break;
            }

        slideStartThresholdInMillis *= deviceTimingFactor;
        newSlideStartThresholdInMillis *= deviceTimingFactor;

        slideStartThresholdInPixels *= devicePixelFactor;
        slideMoveThresholdInPixels *= devicePixelFactor;
        initialStepThresholdInPixels *= devicePixelFactor;
        additionalStepThresholdInPixels *= devicePixelFactor;
        }


    public float deviceTimingFactor = 1f;

    public float devicePixelFactorX = 1f;

    public float devicePixelFactorY = 1f;

    public float devicePixelFactor = 1f;

    public int slideStartThresholdInMillis;

    public int slideStartThresholdInPixels;

    public int slideMoveThresholdInPixels;

    public int newSlideStartThresholdInMillis;

    public int initialStepThresholdInPixels;

    public int additionalStepThresholdInPixels;
    }
