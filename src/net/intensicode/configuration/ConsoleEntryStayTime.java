package net.intensicode.configuration;

import net.intensicode.ConfigurableValue;
import net.intensicode.screens.ConsoleOverlay;

public final class ConsoleEntryStayTime implements ConfigurableValue
    {
    public ConsoleEntryStayTime( final ConsoleOverlay aConsoleOverlay )
        {
        myConsoleOverlay = aConsoleOverlay;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Console entry stay time";
        }

    public final String getInfoText()
        {
        return "Number of ticks a new console entry will stay visible.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return Integer.toString( aConfiguredValue );
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myConsoleOverlay.defaultStayTimeInTicks = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return myConsoleOverlay.recommendedMaxStayTimeInTicks;
        }

    public final int getCurrentValue()
        {
        return myConsoleOverlay.defaultStayTimeInTicks;
        }

    public final int getStepSize()
        {
        return 1;
        }


    private final ConsoleOverlay myConsoleOverlay;
    }
