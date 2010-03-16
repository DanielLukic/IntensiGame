//#condition TIMING

package net.intensicode.configuration;

import net.intensicode.ConfigurableValue;
import net.intensicode.util.Timing;

public final class DumpTiming implements ConfigurableValue
    {
    public final String getTitle()
        {
        return "Dump timing stats";
        }

    public final String getInfoText()
        {
        return "Dump the current timing stats.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return "DUMP";
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        final StringBuffer buffer = new StringBuffer();
        Timing.dumpInto( buffer );
        System.out.println( buffer );
        }

    public final int getMaxValue()
        {
        return 0;
        }

    public final int getCurrentValue()
        {
        return 0;
        }

    public final int getStepSize()
        {
        return 0;
        }
    }
