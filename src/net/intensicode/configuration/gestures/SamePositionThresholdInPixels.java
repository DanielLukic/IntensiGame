//#condition TOUCH

package net.intensicode.configuration.gestures;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.touch.TouchGesturesConfiguration;

public final class SamePositionThresholdInPixels implements ConfigurableIntegerValue
    {
    public SamePositionThresholdInPixels( final TouchGesturesConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Same Position Sensitivity";
        }

    public final String getInfoText()
        {
        return "Pixels that a touch position has to changed before it is considered a different position.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.samePositionThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.samePositionThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchGesturesConfiguration myConfiguration;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }
