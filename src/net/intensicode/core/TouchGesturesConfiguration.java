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
                directionIgnoreFactor = 2f;
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
                directionIgnoreFactor = 1.6f;
                break;
            }
        }

    public int gestureStartThresholdInMillis;

    public int breakTimeThresholdInMillis;

    public int samePositionThresholdInPixels;

    public int strokeThresholdInPixels;

    public float directionIgnoreFactor;
    }
