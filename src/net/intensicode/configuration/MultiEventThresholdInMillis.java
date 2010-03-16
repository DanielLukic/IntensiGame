//#condition TRACKBALL

package net.intensicode.configuration;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TrackballController;

public final class MultiEventThresholdInMillis implements ConfigurableIntegerValue
    {
    public MultiEventThresholdInMillis( final TrackballController aTrackballController )
        {
        myTrackballController = aTrackballController;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "multiEventThresholdInMillis";
        }

    public final String getInfoText()
        {
        return "Milliseconds before the current trackball changes are considered multiple events. " +
               "This value determines in what interval trackball events are fired if the user continously moves the trackball. ";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTrackballController.multiEventThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myTrackballController.multiEventThresholdInMillis;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballController myTrackballController;

    private static final int MAXIMUM_THRESHOLD_IN_MILLIS = 250;
    }
