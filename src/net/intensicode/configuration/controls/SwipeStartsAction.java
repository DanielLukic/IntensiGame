//#condition TOUCH

package net.intensicode.configuration.controls;

import net.intensicode.ConfigurableBooleanValue;
import net.intensicode.touch.TouchControlsConfiguration;

public final class SwipeStartsAction implements ConfigurableBooleanValue
    {
    public SwipeStartsAction( final TouchControlsConfiguration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "swipeStartsAction";
        }

    public final String getInfoText()
        {
        return "Gestures and drag movements are started when initial touch is outside the touchable area.";
        }

    public final String getValueAsText( final boolean aConfiguredValue )
        {
        return aConfiguredValue ? "YES" : "NO";
        }

    public final void setNewValue( final boolean aConfiguredValue )
        {
        myConfiguration.swipeStartsAction = aConfiguredValue;
        }

    public final boolean getCurrentValue()
        {
        return myConfiguration.swipeStartsAction;
        }


    private final TouchControlsConfiguration myConfiguration;
    }
