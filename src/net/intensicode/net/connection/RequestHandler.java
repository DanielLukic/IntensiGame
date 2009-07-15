package net.intensicode.net.connection;

import net.intensicode.net.Response;
import net.intensicode.net.Request;
import net.intensicode.net.TransportException;
import net.intensicode.util.Log;

import java.io.IOException;

public final class RequestHandler implements Runnable
    {
    public static final int DEFAULT_REQUEST_TIMEOUT_IN_MILLIS = 30000;

    public static int requestTimeoutInMillis = DEFAULT_REQUEST_TIMEOUT_IN_MILLIS;

    public Response response;

    public IOException exception;



    public RequestHandler( final TransportConnection aChainedConnection, final Request aRequest )
        {
        myChainedConnection = aChainedConnection;
        myRequest = aRequest;
        }

    public synchronized final Response process() throws IOException, InterruptedException
        {
        processRequestAsynchronously();

        wait( requestTimeoutInMillis );

        if ( exception != null ) throw exception;
        return response != null ? response : Response.TIMED_OUT;
        }

    // From Runnable

    public void run()
        {
        try
            {
            response = myChainedConnection.process( myRequest );
            }
        catch ( final IOException e )
            {
            exception = e;
            //#if DEBUG || LOGGING
            Log.error( e );
            //#endif
            }
        catch ( final Throwable t )
            {
            exception = new TransportException( t );
            //#if DEBUG || LOGGING
            Log.error( t );
            //#endif
            }
        finally
            {
            synchronized ( this )
                {
                notify();
                }
            }
        }

    // Implementation

    private void processRequestAsynchronously()
        {
        new Thread( this ).start();
        }



    private final Request myRequest;

    private final TransportConnection myChainedConnection;
    }
