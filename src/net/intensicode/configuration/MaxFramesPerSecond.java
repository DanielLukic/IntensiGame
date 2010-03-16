package net.intensicode.configuration;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.GameTiming;

public final class MaxFramesPerSecond implements ConfigurableIntegerValue
    {
    public MaxFramesPerSecond( final GameTiming aGameTiming )
        {
        myGameTiming = aGameTiming;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Max frames per second";
        }

    public final String getInfoText()
        {
        return "Maximum number of frames drawn.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return Integer.toString( aConfiguredValue );
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        myGameTiming.maxFramesPerSecond = aConfiguredValue;
        }

    public final int getMaxValue()
        {
        return MAX_FRAMES_PER_SECOND;
        }

    public final int getCurrentValue()
        {
        return myGameTiming.maxFramesPerSecond;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final GameTiming myGameTiming;

    private static final int MAX_FRAMES_PER_SECOND = 64;
    }
