package core;

import net.intensicode.*;

public final class FakePlatformContext implements PlatformContext
    {
    public long compatibleTimeInMillis()
        {
        return System.currentTimeMillis();
        }

    public void openWebBrowser( final String aURL )
        {
        throw new RuntimeException( "nyi" );
        }

    public void sendEmail( final EmailData aEmailData )
        {
        throw new RuntimeException( "nyi" );
        }

    public String getPlatformSpecString()
        {
        throw new RuntimeException( "nyi" );
        }

    public String getGraphicsSpecString()
        {
        throw new RuntimeException( "nyi" );
        }

    public String getExtendedExceptionData( final Throwable aException )
        {
        throw new RuntimeException( "nyi" );
        }
    }
