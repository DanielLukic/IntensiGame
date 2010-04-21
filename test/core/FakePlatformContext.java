package core;

import net.intensicode.PlatformContext;

public final class FakePlatformContext implements PlatformContext
    {
    public long compatibleTimeInMillis()
        {
        return System.currentTimeMillis();
        }

    public void openWebBrowser( final String aURL )
        {
        }
    }
