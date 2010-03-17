//#condition CONSOLE

package net.intensicode.configuration;

import net.intensicode.ConfigurableBooleanValue;
import net.intensicode.screens.ConsoleOverlay;

public final class ShowHideConsole implements ConfigurableBooleanValue
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

    public final String getValueAsText( final boolean aConfiguredValue )
        {
        return aConfiguredValue ? "SHOW" : "HIDE";
        }

    public final void setNewValue( final boolean aConfiguredValue )
        {
        myConsoleOverlay.show = aConfiguredValue;
        }

    public final boolean getCurrentValue()
        {
        return myConsoleOverlay.show;
        }


    private final ConsoleOverlay myConsoleOverlay;
    }
