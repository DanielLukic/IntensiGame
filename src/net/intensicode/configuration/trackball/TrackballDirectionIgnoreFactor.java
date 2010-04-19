//#condition TRACKBALL

package net.intensicode.configuration.trackball;

import net.intensicode.ConfigurableFloatValue;
import net.intensicode.core.TrackballConfiguration;

public final class TrackballDirectionIgnoreFactor implements ConfigurableFloatValue
    {
    public TrackballDirectionIgnoreFactor( final TrackballConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Diagonal move ignore factor";
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
        myConfiguration.directionIgnoreFactor = aConfiguredValue + MINIMUM_VALUE;
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


    private final TrackballConfiguration myConfiguration;

    private static final float MINIMUM_VALUE = 1f;

    private static final float MAXIMUM_VALUE = 5f;

    private static final float VALUE_RANGE = MAXIMUM_VALUE - MINIMUM_VALUE;
    }
