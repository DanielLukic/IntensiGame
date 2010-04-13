//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchSliderConfiguration
    {
    public static final int SENSITIVITY_LOW = 1;

    public static final int SENSITIVITY_MEDIUM = 2;

    public static final int SENSITIVITY_HIGH = 3;

    public void setSensitivityPreset( final int aSensitivityId )
        {
        Log.debug( "TouchSliderConfiguration#setSensitivityPreset {}", aSensitivityId );
        switch ( aSensitivityId )
            {
            default:
            case SENSITIVITY_LOW:
                slideStartThresholdInMillis = 25;
                slideStartThresholdInPixels = 20;
                slideMoveThresholdInPixels = 10;
                newSlideStartThresholdInMillis = 50;
                initialStepThresholdInPixels = 10;
                additionalStepThresholdInPixels = 100;
                break;
            case SENSITIVITY_MEDIUM:
                slideStartThresholdInMillis = 25;
                slideStartThresholdInPixels = 15;
                slideMoveThresholdInPixels = 8;
                newSlideStartThresholdInMillis = 40;
                initialStepThresholdInPixels = 8;
                additionalStepThresholdInPixels = 50;
                break;
            case SENSITIVITY_HIGH:
                slideStartThresholdInMillis = 25;
                slideStartThresholdInPixels = 10;
                slideMoveThresholdInPixels = 6;
                newSlideStartThresholdInMillis = 30;
                initialStepThresholdInPixels = 6;
                additionalStepThresholdInPixels = 30;
                break;
            }
        }

    public int slideStartThresholdInMillis = 25;

    public int slideStartThresholdInPixels = 20;

    public int slideMoveThresholdInPixels = 10;

    public int newSlideStartThresholdInMillis = 50;

    public int initialStepThresholdInPixels = 10;

    public int additionalStepThresholdInPixels = 100;
    }
