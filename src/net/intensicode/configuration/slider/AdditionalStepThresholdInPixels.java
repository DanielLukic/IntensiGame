//#condition TOUCH

package net.intensicode.configuration.slider;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchSlider;

public final class AdditionalStepThresholdInPixels implements ConfigurableIntegerValue
    {
    public AdditionalStepThresholdInPixels( final TouchSlider aTouchSlider )
        {
        myTouchSlider = aTouchSlider;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Additional Step Threshold";
        }

    public final String getInfoText()
        {
        return "Number of pixels that are subtracted from the slide movement after the first 'step' has been generated. " +
               "Determines how 'hard' you have to move before more than one step is being generated.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchSlider.additionalStepThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myTouchSlider.additionalStepThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchSlider myTouchSlider;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }
