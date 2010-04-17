//#condition TOUCH

package net.intensicode.configuration.gestures;

import net.intensicode.ConfigurableFloatValue;
import net.intensicode.core.TouchGesturesConfiguration;

public final class GesturesDirectionIgnoreFactor implements ConfigurableFloatValue
    {
    public GesturesDirectionIgnoreFactor( final TouchGesturesConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
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

    public final String getValueAsText( final float aConfiguredValue )
        {
        return Float.toString( MINIMUM_VALUE + aConfiguredValue );
        }

    public final void setNewValue( final float aConfiguredValue )
        {
        myConfiguration.directionIgnoreFactor = MINIMUM_VALUE + aConfiguredValue;
        }

    public final float getValueRange()
        {
        return VALUE_RANGE;
        }

    public final float getCurrentValue()
        {
        return myConfiguration.directionIgnoreFactor - MINIMUM_VALUE;
        }

    public final float getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchGesturesConfiguration myConfiguration;

    private static final float MINIMUM_VALUE = 1f;

    private static final float MAXIMUM_VALUE = 5f;

    private static final float VALUE_RANGE = MAXIMUM_VALUE - MINIMUM_VALUE;
    }
