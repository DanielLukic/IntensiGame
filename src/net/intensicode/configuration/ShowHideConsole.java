package net.intensicode.configuration;

import net.intensicode.ConfigurableValue;
import net.intensicode.screens.ConsoleOverlay;

public final class ShowHideConsole implements ConfigurableValue
    {
    public ShowHideConsole( final ConsoleOverlay aConsoleOverlay )
        {
        myConsoleOverlay = aConsoleOverlay;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Console overlay";
        }

    public final String getInfoText()
        {
        return "Show/hide the console overlay.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return aConfiguredValue != 0 ? "SHOW" : "HIDE";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConsoleOverlay.show = aConfiguredValue != 0;
        }

    public final int getMaxValue()
        {
        return 1;
        }

    public final int getCurrentValue()
        {
        return myConsoleOverlay.show ? 1 : 0;
        }

    public final int getStepSize()
        {
        return 1;
        }


    private final ConsoleOverlay myConsoleOverlay;
    }
