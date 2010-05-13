package core;

import net.intensicode.*;
import net.intensicode.util.DynamicArray;

public final class FakePlatformContext implements PlatformContext
    {
    public final DynamicArray timestamps = new DynamicArray();

    public long compatibleTimeInMillis()
        {
        if ( timestamps.size == 0 ) throw new IllegalStateException( "no more timestamps" );
        final Object timestamp = timestamps.remove( 0 );
        return Long.parseLong( String.valueOf( timestamp ) );
        }

    public void openWebBrowser( final String aURL )
        {
        throw new RuntimeException( "nyi" );
        }

    public void sendEmail( final EmailData aEmailData )
        {
        throw new RuntimeException( "nyi" );
        }

    public String screenOrientationId()
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

    public void showError( final String aMessage, final Throwable aOptionalThrowable )
        {
        throw new RuntimeException( "nyi" );
        }

    public void showCriticalError( final String aMessage, final Throwable aOptionalThrowable )
        {
        throw new RuntimeException( "nyi" );
        }
    }
