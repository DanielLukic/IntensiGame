//#condition TOUCH

package net.intensicode.configuration.slider;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchSlider;

public final class NewSlideStartThresholdInMillis implements ConfigurableIntegerValue
    {
    public NewSlideStartThresholdInMillis( final TouchSlider aTouchSlider )
        {
        myTouchSlider = aTouchSlider;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "New Slide Threshold";
        }

    public final String getInfoText()
        {
        return "Milliseconds before 'no movement' is considered start of a new slide.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchSlider.newSlideStartThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myTouchSlider.newSlideStartThresholdInMillis;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchSlider myTouchSlider;

    private static final int MAXIMUM_THRESHOLD_IN_MILLIS = 250;
    }
