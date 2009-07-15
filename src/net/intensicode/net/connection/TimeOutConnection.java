package net.intensicode.net.connection;

import net.intensicode.net.Response;
import net.intensicode.net.Request;

import java.io.IOException;

public final class TimeOutConnection implements TransportConnection
    {
    public TimeOutConnection( final TransportConnection aChainedConnection )
        {
        myChainedConnection = aChainedConnection;
        }

    // From TransportConnection

    public final boolean hasBeenInterrupted()
        {
        return myChainedConnection.hasBeenInterrupted();
        }

    public void prepare( final Request aRequest ) throws IOException
        {
        myChainedConnection.prepare( aRequest );
        }

    public final Response process( final Request aRequest ) throws IOException, InterruptedException
        {
        return createNewHandler( aRequest ).process();
        }

    public final void cancel()
        {
        myChainedConnection.cancel();
        }

    public final void close()
        {
        myChainedConnection.close();
        }

    // Implementation

    private RequestHandler createNewHandler( final Request aRequest )
        {
        return new RequestHandler( myChainedConnection, aRequest );
        }

    private final TransportConnection myChainedConnection;
    }
