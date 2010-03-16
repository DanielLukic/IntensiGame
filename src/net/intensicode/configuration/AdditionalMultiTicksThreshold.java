package net.intensicode.configuration;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.AnalogController;

public final class AdditionalMultiTicksThreshold implements ConfigurableIntegerValue
    {
    public AdditionalMultiTicksThreshold( final AnalogController aAnalogController )
        {
        myAnalogController = aAnalogController;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "additionalMultiTicksThreshold";
        }

    public final String getInfoText()
        {
        return "Number of 'ticks' after first multi event tick before considering adding another multi event step. " +
               "The higher this value, the more the trackball has to move before additional move steps are issued. ";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        if ( aConfiguredValue == 0 ) return "disable additional ticks";
        return aConfiguredValue + " ticks";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myAnalogController.additionalMultiTicksThreshold = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_ADDITIONAL_MULTI_TICKS_THRESHOLD;
        }

    public final int getCurrentValue()
        {
        return myAnalogController.additionalMultiTicksThreshold;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final AnalogController myAnalogController;

    private static final int MAXIMUM_ADDITIONAL_MULTI_TICKS_THRESHOLD = 30;
    }
