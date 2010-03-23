//#condition TOUCH

package net.intensicode.configuration.gestures;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchGestures;

public final class StrokeThresholdInPixels implements ConfigurableIntegerValue
    {
    public StrokeThresholdInPixels( final TouchGestures aTouchGestures )
        {
        myTouchGestures = aTouchGestures;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Stroke Threshold";
        }

    public final String getInfoText()
        {
        return "Length in pixels that a stroke has to cover before it is valid.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchGestures.strokeThresholdInPixels = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myTouchGestures.strokeThresholdInPixels;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchGestures myTouchGestures;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 50;
    }