//#condition TRACKBALL

package net.intensicode.configuration.trackball;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TrackballConfiguration;

public final class InitialTicksThreshold implements ConfigurableIntegerValue
    {
    public InitialTicksThreshold( final TrackballConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "initialTicksThreshold";
        }

    public final String getInfoText()
        {
        return "Initial 'ticks' to ignore before considering a trackball event valid. " +
               "The higher this value, the more the trackball has to move before an event is triggered. ";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ticks";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.initialTicksThreshold = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_INITIAL_TICKS_THRESHOLD;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.initialTicksThreshold;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballConfiguration myConfiguration;

    private static final int MAXIMUM_INITIAL_TICKS_THRESHOLD = 30;
    }
