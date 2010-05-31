//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.touch.TouchControlsConfiguration;

public final class DragStepSize implements ConfigurableIntegerValue
    {
    public DragStepSize( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "dragStepSize";
        }

    public final String getInfoText()
        {
        return "Length in pixels that are considered 'one step' when dragging.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue + " pixels";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConfiguration.dragStepSize.width = myConfiguration.dragStepSize.height = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAXIMUM_THRESHOLD_IN_PIXELS;
        }

    public final int getCurrentValue()
        {
        return myConfiguration.dragStepSize.width;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TouchControlsConfiguration myConfiguration;

    private static final int MAXIMUM_THRESHOLD_IN_PIXELS = 250;
    }
