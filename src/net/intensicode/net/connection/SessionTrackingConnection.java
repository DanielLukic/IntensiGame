package net.intensicode.net.connection;

import net.intensicode.net.Response;
import net.intensicode.net.Request;
import net.intensicode.util.Log;

import java.io.IOException;

public final class SessionTrackingConnection implements TransportConnection
    {
    public SessionTrackingConnection( final TransportConnection aChainedConnection )
        {
        myChainedConnection = aChainedConnection;
        }

    // Implementation

    public final boolean hasBeenInterrupted()
        {
        return myChainedConnection.hasBeenInterrupted();
        }

    public void prepare( final Request aRequest ) throws IOException
        {
        if ( mySession != null ) aRequest.cookies.put( SESSION_KEY, mySession );
        myChainedConnection.prepare( aRequest );
        }

    public final Response process( final Request aRequest ) throws IOException, InterruptedException
        {
        final Response response = myChainedConnection.process( aRequest );
        extractSessionId( response );
        return response;
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

    private void extractSessionId( final Response aResponse )
        {
        if ( aResponse.data.isNull( SESSION_KEY ) ) return;

        final String sessionId = aResponse.data.getString( SESSION_KEY );
        Log.debug( "Storing session id {}", sessionId );
        mySession = sessionId;
        }



    private static String mySession;

    private final TransportConnection myChainedConnection;

    private final static String SESSION_KEY = "_insomnia_session";
    }
