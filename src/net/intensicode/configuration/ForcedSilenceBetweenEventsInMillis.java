package net.intensicode.configuration;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TrackballController;

public final class ForcedSilenceBetweenEventsInMillis implements ConfigurableIntegerValue
    {
    public ForcedSilenceBetweenEventsInMillis( final TrackballController aTrackballController )
        {
        myTrackballController = aTrackballController;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "forcedSilenceBetweenEventsInMillis";
        }

    public final String getInfoText()
        {
        return "Milliseconds in which trackball changes are discared before starting a new event.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTrackballController.forcedSilenceBetweenEventsInMillis = aConfiguredValue;
        }

    public final int getMinValue()
        {
        return 0;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_SILENCE_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myTrackballController.forcedSilenceBetweenEventsInMillis;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballController myTrackballController;

    private static final int MAXIMUM_SILENCE_IN_MILLIS = 250;
    }
