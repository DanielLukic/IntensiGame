//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.Log;

public final class TouchGesturesConfiguration
    {
    public static final String[] SENSITIVITY_STRING_VALUES = { "LOW", "MED", "HI" };

    public static final int SENSITIVITY_LOW = 1;

    public static final int SENSITIVITY_MEDIUM = 2;

    public static final int SENSITIVITY_HIGH = 3;

    public void setSensitivityPreset( final int aSensitivityId )
        {
        Log.debug( "TouchGesturesConfiguration#setSensitivityPreset {}", aSensitivityId );
        switch ( aSensitivityId )
            {
            default:
            case SENSITIVITY_LOW:
                breakTimeThresholdInMillis = 80;
                samePositionThresholdInPixels = 12;
                strokeThresholdInPixels = 6;
                directionIgnoreFactor = 1.85f;
                break;
            case SENSITIVITY_MEDIUM:
                breakTimeThresholdInMillis = 60;
                samePositionThresholdInPixels = 10;
                strokeThresholdInPixels = 5;
                directionIgnoreFactor = 1.75f;
                break;
            case SENSITIVITY_HIGH:
                breakTimeThresholdInMillis = 40;
                samePositionThresholdInPixels = 8;
                strokeThresholdInPixels = 4;
                directionIgnoreFactor = 1.6f;
                break;
            }
        }

    public int breakTimeThresholdInMillis = 80;

    public int samePositionThresholdInPixels = 12;

    public int strokeThresholdInPixels = 6;

    public float directionIgnoreFactor = 1.85f;
    }
