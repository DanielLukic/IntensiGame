package net.intensicode.configuration;

import net.intensicode.ConfigurableValue;
import net.intensicode.core.GameTiming;

public final class TicksPerSecond implements ConfigurableValue
    {
    public TicksPerSecond( final GameTiming aGameTiming )
        {
        myGameTiming = aGameTiming;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Ticks per second";
        }

    public final String getInfoText()
        {
        return "Determines how often, per second, the application logic is executed. " +
               "This should not be too high, because it will produce too much cpu load. " +
               "It should not be too low, because the system will feel laggish. " +
               "Note that changes will be properly applied only after a restart!";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return Integer.toString( aConfiguredValue );
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myGameTiming.ticksPerSecond = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAX_TICKS_PER_SECOND;
        }

    public final int getCurrentValue()
        {
        return myGameTiming.ticksPerSecond;
        }

    public final int getStepSize()
        {
        return 1;
        }


    private final GameTiming myGameTiming;

    private static final int MAX_TICKS_PER_SECOND = 64;
    }
