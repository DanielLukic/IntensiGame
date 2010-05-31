//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableFloatValue;
import net.intensicode.touch.TouchControlsConfiguration;

public final class SpeedLowBoundary implements ConfigurableFloatValue
    {
    public SpeedLowBoundary( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From ConfigurableIntegerValue

    public final String getTitle()
        {
        return "speedLowBoundary";
        }

    public final String getInfoText()
        {
        return "Minimum speed multiplier to use for very slow drag action. This defines how slow a drag action can be.";
        }

    public final String getValueAsText( final float aConfiguredValue )
        {
        return Float.toString( MINIMUM_VALUE + aConfiguredValue );
        }

    public final void setNewValue( final float aConfiguredValue )
        {
        myConfiguration.speedLowBoundary = MINIMUM_VALUE + aConfiguredValue;
        }

    public final float getValueRange()
        {
        return VALUE_RANGE;
        }

    public final float getCurrentValue()
        {
        return myConfiguration.speedLowBoundary - MINIMUM_VALUE;
        }

    public final float getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchControlsConfiguration myConfiguration;

    private static final float MINIMUM_VALUE = 0f;

    private static final float MAXIMUM_VALUE = 5f;

    private static final float VALUE_RANGE = MAXIMUM_VALUE - MINIMUM_VALUE;
    }
