package net.intensicode.hiscore;

import net.intensicode.core.NetworkIO;

public class Gammeta
    {
    public Gammeta( final Settings aSettings, final NetworkIO aNetworkIO )
        {
        mySettings = aSettings;
        myNetworkIO = aNetworkIO;
        }

    public final SimpleLeaderboardService getSimpleLeaderboardService()
        {
        return new SimpleLeaderboardService( mySettings, myNetworkIO );
        }

    private final Settings mySettings;

    private final NetworkIO myNetworkIO;
    }
