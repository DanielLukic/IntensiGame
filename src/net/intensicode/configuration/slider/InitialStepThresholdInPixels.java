//#condition TOUCH

package net.intensicode.configuration.slider;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchSlider;

public final class InitialStepThresholdInPixels implements ConfigurableIntegerValue
    {
    public InitialStepThresholdInPixels( final TouchSlider aTouchSlider )
        {
        myTouchSlider = aTouchSlider;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Initial Step Threshold";
        }

    public final String getInfoText()
        {
        return "Number of pixels that are subtracted from the initial slide movement. " +
               "Determines how 'hard' you have to move before it has an effect. " +
               "This is applied after slideStartThresholdInPixels have been counted.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchSlider.initialStepThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myTouchSlider.initialStepThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchSlider myTouchSlider;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }
