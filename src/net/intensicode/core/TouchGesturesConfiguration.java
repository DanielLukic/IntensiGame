//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

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
                directionIgnoreFactorFixed = FixedMath.FIXED_1 + FixedMath.FIXED_0_5 + FixedMath.FIXED_0_25 + FixedMath.FIXED_0_1;
                break;
            case SENSITIVITY_MEDIUM:
                breakTimeThresholdInMillis = 60;
                samePositionThresholdInPixels = 10;
                strokeThresholdInPixels = 5;
                directionIgnoreFactorFixed = FixedMath.FIXED_1 + FixedMath.FIXED_0_5 + FixedMath.FIXED_0_25;
                break;
            case SENSITIVITY_HIGH:
                breakTimeThresholdInMillis = 40;
                samePositionThresholdInPixels = 8;
                strokeThresholdInPixels = 4;
                directionIgnoreFactorFixed = FixedMath.FIXED_1 + FixedMath.FIXED_0_5 + FixedMath.FIXED_0_1;
                break;
            }
        }

    public int breakTimeThresholdInMillis = 80;

    public int samePositionThresholdInPixels = 12;

    public int strokeThresholdInPixels = 6;

    public int directionIgnoreFactorFixed = FixedMath.FIXED_1 + FixedMath.FIXED_0_5 + FixedMath.FIXED_0_25 + FixedMath.FIXED_0_1;
    }
