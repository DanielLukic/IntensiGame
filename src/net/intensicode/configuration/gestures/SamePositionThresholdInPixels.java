//#condition TOUCH

package net.intensicode.configuration.gestures;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchGestures;

public final class SamePositionThresholdInPixels implements ConfigurableIntegerValue
    {
    public SamePositionThresholdInPixels( final TouchGestures aTouchGestures )
        {
        myTouchGestures = aTouchGestures;
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
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchGestures.samePositionThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myTouchGestures.samePositionThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchGestures myTouchGestures;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }
