//#condition TOUCH

package net.intensicode.configuration.gestures;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchGestures;

public final class BreakTimeThresholdInMillis implements ConfigurableIntegerValue
    {
    public BreakTimeThresholdInMillis( final TouchGestures aTouchGestures )
        {
        myTouchGestures = aTouchGestures;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Stroke Sensitivity";
        }

    public final String getInfoText()
        {
        return "Milliseconds before 'no movement' is considered the end of the current stroke. " +
               "The lower this is, the faster you have to draw the strokes. " +
               "Pausing too long will create a new stroke. " +
               "The higher this is, the longer you have to pause after a stroke before you can start the next one.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " ms";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myTouchGestures.breakTimeThresholdInMillis = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_SILENCE_IN_MILLIS;
        }

    public final int getCurrentValue()
        {
        return myTouchGestures.breakTimeThresholdInMillis;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchGestures myTouchGestures;

    private static final int MAXIMUM_SILENCE_IN_MILLIS = 250;
    }
