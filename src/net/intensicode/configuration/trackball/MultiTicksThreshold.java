//#condition TRACKBALL

package net.intensicode.configuration.trackball;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.trackball.TrackballConfiguration;

public final class MultiTicksThreshold implements ConfigurableIntegerValue
    {
    public MultiTicksThreshold( final TrackballConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "multiTicksThreshold";
        }

    public final String getInfoText()
        {
        return "Number of 'ticks' after initial significant tick before considering a trackball event a multi event. " +
               "The higher this value, the more the trackball has to move before an event triggeres a move value higher than 1. ";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ticks";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.multiTicksThreshold = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_MULTI_TICKS_THRESHOLD;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.multiTicksThreshold;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballConfiguration myConfiguration;

    private static final int MAXIMUM_MULTI_TICKS_THRESHOLD = 30;
    }
