package net.intensicode.net.connection;

import net.intensicode.net.*;
import net.intensicode.util.Log;

import java.io.IOException;

public final class AutoConnection implements TransportConnection
    {
    public AutoConnection()
        {
        myConnection = new KeepAliveSocketHttpTransportConnection();
        }

    // From TransportConnection

    public final boolean hasBeenInterrupted()
        {
        return myConnection.hasBeenInterrupted();
        }

    public void prepare( final Request aRequest ) throws IOException
        {
        myConnection.prepare( aRequest );
        }

    public Response process( final Request aRequest ) throws IOException, InterruptedException
        {
        try
            {
            return myConnection.process( aRequest );
            }
        catch ( final SecurityException e )
            {
            if ( !downgradeConnection( aRequest ) ) throw e;
            return process( aRequest );
            }
        catch ( final IOException e )
            {
            createNewConnection();
            throw e;
            }
        }

    public final void cancel()
        {
        myConnection.cancel();
        }

    public final void close()
        {
        myConnection.close();
        }

    // Implementation

    private boolean downgradeConnection( final Request aRequest ) throws IOException
        {
        if ( myConnection instanceof KeepAliveSocketHttpTransportConnection )
            {
            Log.debug( "downgrading to HttpTransportConnection" );
            terminateOldKeepAliveConnection();
            createAndPrepareNewHttpConnection( aRequest );
            return true;
            }
        else
            {
            return false;
            }
        }

    private void terminateOldKeepAliveConnection()
        {
        final KeepAliveSocketHttpTransportConnection oldKeepAliveConnection = (KeepAliveSocketHttpTransportConnection) myConnection;
        oldKeepAliveConnection.terminate();
        }

    private void createAndPrepareNewHttpConnection( final Request aRequest ) throws IOException
        {
        myConnection = new HttpTransportConnection();
        myConnection.prepare( aRequest );
        }

    private void createNewConnection()
        {
        if ( myConnection instanceof KeepAliveSocketHttpTransportConnection )
            {
            terminateOldKeepAliveConnection();
            myConnection = new KeepAliveSocketHttpTransportConnection();
            }
        else
            {
            myConnection = new HttpTransportConnection();
            }
        }

    private TransportConnection myConnection;
    }
