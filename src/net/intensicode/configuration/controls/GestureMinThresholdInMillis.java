//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.touch.*;

public final class GestureMinThresholdInMillis implements ConfigurableIntegerValue
    {
    public GestureMinThresholdInMillis( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "gestureMinThresholdInMillis";
        }

    public final String getInfoText()
        {
        return "Milliseconds before movement is considered to be starting a gesture.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.gestureMinThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_SILENCE_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.gestureMinThresholdInMillis;
        }

    public final int getStepSize()
        {
        return 1;
        }


    private final TouchControlsConfiguration myConfiguration;

    private static final int MAXIMUM_SILENCE_IN_MILLIS = 500;
    }
