//#condition TOUCH

package net.intensicode.configuration.slider;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchSliderConfiguration;

public final class SlideStartThresholdInPixels implements ConfigurableIntegerValue
    {
    public SlideStartThresholdInPixels( final TouchSliderConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Slide Start Sensitivity (px)";
        }

    public final String getInfoText()
        {
        return "Number of pixels before a slide is considered long engough to be started.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.slideStartThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.slideStartThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchSliderConfiguration myConfiguration;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }
