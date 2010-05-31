//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.touch.TouchControlsConfiguration;

public final class DeadZoneSize implements ConfigurableIntegerValue
    {
    public DeadZoneSize( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "deadZoneSize";
        }

    public final String getInfoText()
        {
        return "Size in pixels around the initial touch point that are considered a 'dead zone'. " +
               "As long as the movement stays in this zone, no action is considered.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.deadZoneSize.width = myConfiguration.deadZoneSize.height = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.deadZoneSize.width;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchControlsConfiguration myConfiguration;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 125;
    }
