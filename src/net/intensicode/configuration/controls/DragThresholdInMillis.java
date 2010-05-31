//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.touch.*;

public final class DragThresholdInMillis implements ConfigurableIntegerValue
    {
    public DragThresholdInMillis( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "dragThresholdInMillis";
        }

    public final String getInfoText()
        {
        return "Milliseconds before movement is considered to be starting a drag action.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.dragThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_SILENCE_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.dragThresholdInMillis;
        }

    public final int getStepSize()
        {
        return 1;
        }


    private final TouchControlsConfiguration myConfiguration;

    private static final int MAXIMUM_SILENCE_IN_MILLIS = 500;
    }