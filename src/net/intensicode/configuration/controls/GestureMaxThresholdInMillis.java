//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.touch.TouchControlsConfiguration;

public final class GestureMaxThresholdInMillis implements ConfigurableIntegerValue
    {
    public GestureMaxThresholdInMillis( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "gestureMaxThresholdInMillis";
        }

    public final String getInfoText()
        {
        return "Milliseconds after which movement is considered too long to be starting a gesture.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.gestureMaxThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_SILENCE_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.gestureMaxThresholdInMillis;
        }

    public final int getStepSize()
        {
        return 1;
        }


    private final TouchControlsConfiguration myConfiguration;

    private static final int MAXIMUM_SILENCE_IN_MILLIS = 500;
    }
