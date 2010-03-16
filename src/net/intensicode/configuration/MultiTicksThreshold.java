package net.intensicode.configuration;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TrackballController;

public final class MultiTicksThreshold implements ConfigurableIntegerValue
    {
    public MultiTicksThreshold( final TrackballController aTrackballController )
        {
        myTrackballController = aTrackballController;
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
        myTrackballController.multiTicksThreshold = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_MULTI_TICKS_THRESHOLD;
        }

    public final int getCurrentValue()
        {
        return myTrackballController.multiTicksThreshold;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballController myTrackballController;

    private static final int MAXIMUM_MULTI_TICKS_THRESHOLD = 30;
    }
