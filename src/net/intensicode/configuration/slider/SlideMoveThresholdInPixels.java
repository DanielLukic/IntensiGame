//#condition TOUCH

package net.intensicode.configuration.slider;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.*;

public final class SlideMoveThresholdInPixels implements ConfigurableIntegerValue
    {
    public SlideMoveThresholdInPixels( final TouchSliderConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Slide Move Threshold";
        }

    public final String getInfoText()
        {
        return "Number of pixels of movement before a new touch event has an effect on the active slide. " +
               "Can be considered the granularity of touch positions.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.slideMoveThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.slideMoveThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchSliderConfiguration myConfiguration;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }
