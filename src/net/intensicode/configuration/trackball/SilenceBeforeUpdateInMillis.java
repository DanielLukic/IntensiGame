//#condition TRACKBALL

package net.intensicode.configuration.trackball;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.trackball.TrackballConfiguration;

public final class SilenceBeforeUpdateInMillis implements ConfigurableIntegerValue
    {
    public SilenceBeforeUpdateInMillis( final TrackballConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "silenceBeforeUpdateInMillis";
        }

    public final String getInfoText()
        {
        return "Milliseconds before the current trackball changes are considered stable. " +
               "Can be considered the responsiveness of the trackball. " +
               "Unfortunately it is not that easy.. :)";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.silenceBeforeUpdateInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_SILENCE_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.silenceBeforeUpdateInMillis;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballConfiguration myConfiguration;

    private static final int MAXIMUM_SILENCE_IN_MILLIS = 250;
    }
