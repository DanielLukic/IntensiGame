package net.intensicode.net.system;

import net.intensicode.net.Response;
import net.intensicode.net.Request;
import net.intensicode.net.TransportException;
import net.intensicode.net.connection.TransportConnection;
import net.intensicode.util.Log;

import java.io.IOException;

public final class TransportEventProcessor
    {
    public TransportEventProcessor( final TransportConnection aConnection, final TransportEvent aEvent )
        {
        myConnection = aConnection;
        myEvent = aEvent;
        }

    public void process() throws InterruptedException
        {
        bailOutIfInterrupted();
        try
            {
            sendRequest();
            }
        catch ( final Throwable t )
            {
            handleException( t );
            }
        finally
            {
            closeExistingConnection();
            }
        }

    private void bailOutIfInterrupted() throws InterruptedException
        {
        if ( myConnection.hasBeenInterrupted() ) throw new InterruptedException();
        }

    private void sendRequest() throws IOException, InterruptedException
        {
        final Request request = myEvent.request;

        myConnection.prepare( request );

        final Response response = myConnection.process( request );
        if ( response == Response.CANCELLED ) return;

        handleResponse( response );
        }

    private void handleResponse( final Response aResponse )
        {
        if ( aResponse.code == Response.HTTP_OK )
            {
            handleCompletedRequest( aResponse );
            }
        else
            {
            handleFailedRequest( aResponse );
            }
        }

    private void handleCompletedRequest( final Response aResponse )
        {
        //#if DEBUG
        //# Log.debug( "TransportSystem delivering {} response", myEvent.request.id );
        //#endif
        myEvent.completeRequestWith( aResponse );
        }

    private void handleFailedRequest( final Response aResponse )
        {
        //#if DEBUG
        Log.debug( "TransportSystem delivering failed {} response (code {})", myEvent.request.id, "" + aResponse.code );
        //#endif

        final TransportException exception = new TransportException( myEvent.request, aResponse );
        Log.debug( "Failed request data: {}", myEvent.request.data.toString( 2 ) );
        Log.debug( "TransportSystem failed {} response", myEvent.request.id );
        myEvent.callback.requestFailed( myEvent.request, exception );
        }

    private void closeExistingConnection()
        {
        myConnection.close();
        }

    private void handleException( final Throwable t ) throws InterruptedException
        {
        if ( t instanceof InterruptedException ) throw (InterruptedException) t;

        Log.debug( "Failed request data: {}", myEvent.request.data.toString( 2 ) );
        Log.debug( "TransportSystem failed {} response", myEvent.request.id );

        myEvent.failRequestWith( getWrappedException( t ) );
        }

    private TransportException getWrappedException( final Throwable aThrowable )
        {
        if ( aThrowable instanceof TransportException ) return (TransportException) aThrowable;
        return new TransportException( aThrowable );
        }



    private final TransportEvent myEvent;

    private final TransportConnection myConnection;
    }
