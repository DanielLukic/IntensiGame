//#condition TOUCH

package net.intensicode.configuration.slider;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchSlider;

public final class SlideStartThresholdInMillis implements ConfigurableIntegerValue
    {
    public SlideStartThresholdInMillis( final TouchSlider aTouchSlider )
        {
        myTouchSlider = aTouchSlider;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Slide Start Sensitivity (ms)";
        }

    public final String getInfoText()
        {
        return "Milliseconds before a slide is started. " +
               "Until this time has passed, touch events are processed, but no output is generated. " +
               "This leaves time for touch gestures to be recognized before a slide event is generated. ";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchSlider.slideStartThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myTouchSlider.slideStartThresholdInMillis;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchSlider myTouchSlider;

    private static final int MAXIMUM_THRESHOLD_IN_MILLIS = 250;
    }