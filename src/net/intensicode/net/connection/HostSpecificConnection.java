package net.intensicode.net.connection;

import net.intensicode.net.Response;
import net.intensicode.net.Request;
import net.intensicode.util.Log;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public final class HostSpecificConnection implements TransportConnection
    {
    public HostSpecificConnection()
        {
        }

    // From TransportConnection

    public final boolean hasBeenInterrupted()
        {
        final Enumeration connections = myConnections.elements();
        while ( connections.hasMoreElements() )
            {
            final TransportConnection connection = (TransportConnection) connections.nextElement();
            if ( connection.hasBeenInterrupted() ) return true;
            }
        return false;
        }

    public void prepare( final Request aRequest ) throws IOException
        {
        if ( aRequest.baseUrl == null ) throw new IllegalArgumentException();

        final TransportConnection connection = getHostSpecificConnection( aRequest.baseUrl );
        connection.prepare( aRequest );
        }

    public Response process( final Request aRequest ) throws IOException, InterruptedException
        {
        final TransportConnection connection = getHostSpecificConnection( aRequest.baseUrl );
        return connection.process( aRequest );
        }

    public final void cancel()
        {
        final Enumeration connections = myConnections.elements();
        while ( connections.hasMoreElements() )
            {
            final TransportConnection connection = (TransportConnection) connections.nextElement();
            connection.cancel();
            }
        }

    public final void close()
        {
        final Enumeration connections = myConnections.elements();
        while ( connections.hasMoreElements() )
            {
            final TransportConnection connection = (TransportConnection) connections.nextElement();
            connection.close();
            }
        }

    // Implementation

    private TransportConnection getHostSpecificConnection( final String aBaseUrl )
        {
        if ( !myConnections.containsKey( aBaseUrl ) )
            {
            Log.debug( "creating host-specific connection for {}", aBaseUrl );
            final TransportConnection connection = createNewConnection( aBaseUrl );
            myConnections.put( aBaseUrl, connection );
            }
        return (TransportConnection) myConnections.get( aBaseUrl );
        }

    private TransportConnection createNewConnection( final String aBaseUrl )
        {
        if ( aBaseUrl.indexOf( "google" ) > -1 ) return new HttpTransportConnection();
        return new AutoConnection();
        }

    private final Hashtable myConnections = new Hashtable();
    }
