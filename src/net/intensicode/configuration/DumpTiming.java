//#condition TIMING

package net.intensicode.configuration;

import net.intensicode.ConfigurableActionValue;
import net.intensicode.util.Timing;

public final class DumpTiming implements ConfigurableActionValue
    {
    public final String getTitle()
        {
        return "Dump timing stats";
        }

    public final String getInfoText()
        {
        return "Dump the current timing stats.";
        }

    public final void trigger()
        {
        final StringBuffer buffer = new StringBuffer();
        Timing.dumpInto( buffer );
        System.out.println( buffer );
        }
    }
