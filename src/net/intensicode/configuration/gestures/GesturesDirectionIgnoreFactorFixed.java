//#condition TOUCH

package net.intensicode.configuration.gestures;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TouchGestures;
import net.intensicode.util.*;

public final class GesturesDirectionIgnoreFactorFixed implements ConfigurableIntegerValue
    {
    public GesturesDirectionIgnoreFactorFixed( final TouchGestures aTouchGestures )
        {
        myTouchGestures = aTouchGestures;
        }

    // From ConfigurableIntegerValue

    public final String getTitle()
        {
        return "Diagonal Sensitivity";
        }

    public final String getInfoText()
        {
        return "Factor to apply to a direction when checking whether it is too small to be considered. " +
               "The higher this value, the more likely are diagonal or non-unidirectional movements. ";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        final float value = aConfiguredValue / (float) MAXIMUM_DECI_STEPS;
        return Float.toString( 1f + value * 4f );
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        // input: 0..MAXIMUM_DECI_STEPS
        // output float: 1.0..5.0
        // output fixed: F1..F5
        final int fixedRange = FixedMath.FIXED_5 - FixedMath.FIXED_1;
        final int scaled = aConfiguredValue * fixedRange / MAXIMUM_DECI_STEPS;
        final int scaledAndTransposed = scaled + FixedMath.FIXED_1;
        myTouchGestures.directionIgnoreFactorFixed = scaledAndTransposed;
        Log.debug( "myTouchGestures.directionIgnoreFactorFixed = {}", scaledAndTransposed );
        Log.debug( "myTouchGestures.directionIgnoreFactorFixed = " + scaledAndTransposed / (float) FixedMath.FIXED_MULTIPLIER );
        }

    public final int getMaxValue()
        {
        return MAXIMUM_DECI_STEPS;
        }

    public final int getCurrentValue()
        {
        Log.debug( "myTouchGestures.directionIgnoreFactorFixed = {}", myTouchGestures.directionIgnoreFactorFixed );
        Log.debug( "myTouchGestures.directionIgnoreFactorFixed = " + myTouchGestures.directionIgnoreFactorFixed / (float) FixedMath.FIXED_MULTIPLIER );
        final int valueFixed = myTouchGestures.directionIgnoreFactorFixed;
        final int deposed = valueFixed - FixedMath.FIXED_1;
        final int fixedRange = FixedMath.FIXED_5 - FixedMath.FIXED_1;
        final int deposedAndDescaled = deposed * MAXIMUM_DECI_STEPS / fixedRange;
        return deposedAndDescaled;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchGestures myTouchGestures;

    private static final int MAXIMUM_DECI_STEPS = 50;
    }
