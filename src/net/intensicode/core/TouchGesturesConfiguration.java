//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.Log;

public final class TouchGesturesConfiguration
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
                gestureStartThresholdInMillis = 40;
                breakTimeThresholdInMillis = 120;
                samePositionThresholdInPixels = 25;
                strokeThresholdInPixels = 20;
                directionIgnoreFactor = 1.9f;
                break;
            case SENSITIVITY_LOW:
                gestureStartThresholdInMillis = 30;
                breakTimeThresholdInMillis = 100;
                samePositionThresholdInPixels = 20;
                strokeThresholdInPixels = 10;
                directionIgnoreFactor = 1.9f;
                break;
            case SENSITIVITY_MEDIUM:
                gestureStartThresholdInMillis = 25;
                breakTimeThresholdInMillis = 80;
                samePositionThresholdInPixels = 12;
                strokeThresholdInPixels = 6;
                directionIgnoreFactor = 1.85f;
                break;
            case SENSITIVITY_HIGH:
                gestureStartThresholdInMillis = 25;
                breakTimeThresholdInMillis = 60;
                samePositionThresholdInPixels = 10;
                strokeThresholdInPixels = 5;
                directionIgnoreFactor = 1.75f;
                break;
            case SENSITIVITY_VERY_HIGH:
                gestureStartThresholdInMillis = 25;
                breakTimeThresholdInMillis = 40;
                samePositionThresholdInPixels = 8;
                strokeThresholdInPixels = 4;
                directionIgnoreFactor = 1.75f;
                break;
            }

        gestureStartThresholdInMillis *= deviceTimingFactor;
        breakTimeThresholdInMillis *= deviceTimingFactor;

        samePositionThresholdInPixels *= devicePixelFactor;
        strokeThresholdInPixels *= devicePixelFactor;
        }


    public float deviceTimingFactor = 1f;

    public float devicePixelFactorX = 1f;

    public float devicePixelFactorY = 1f;

    public float devicePixelFactor = 1f;

    public int gestureStartThresholdInMillis;

    public int breakTimeThresholdInMillis;

    public int samePositionThresholdInPixels;

    public int strokeThresholdInPixels;

    public float directionIgnoreFactor;
    }
