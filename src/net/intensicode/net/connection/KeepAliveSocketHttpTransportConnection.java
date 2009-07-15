package net.intensicode.net.connection;

import net.intensicode.net.*;
import net.intensicode.util.Utilities;
import net.intensicode.util.Log;

import javax.microedition.io.*;
import java.io.*;
import java.util.Hashtable;

public final class KeepAliveSocketHttpTransportConnection extends TransportConnectionSkeleton
    {
    public KeepAliveSocketHttpTransportConnection()
        {
        this( "QiroMobile" );
        }

    public KeepAliveSocketHttpTransportConnection( final String aUserAgent )
        {
        myUserAgent = aUserAgent;
        }

    public void terminate()
        {
        if ( output != null ) Utilities.closeSafely( output );
        if ( input != null ) Utilities.closeSafely( input );
        if ( connection != null ) Utilities.closeSafely( connection );
        myConnectionClosed = true;
        }

    // From TransportConnection

    public boolean hasBeenInterrupted()
        {
        Log.debug( "KeepAliveSocketHttpTransportConnection#hasBeenInterrupted" );
        return myConnectionClosed || super.hasBeenInterrupted();
        }

    public void prepare( final Request aRequest )
        {
        Log.debug( "KeepAliveSocketHttpTransportConnection#prepare" );

        storeRequest( aRequest );
        prepareForNewConnectionIfNecessary();
        }

    public final Response process( final Request aRequest ) throws IOException
        {
        Log.debug( "KeepAliveSocketHttpTransportConnection#process" );
        try
            {
            return doProcessing();
            }
        catch ( final SecurityException e )
            {
            return terminateConnectionAndPropagateException( e );
            }
        catch ( final IOException e )
            {
            return createNewConnectionAndPropagateException( e );
            }
        }

    public void cancel()
        {
        Log.debug( "KeepAliveSocketHttpTransportConnection#cancel" );
        terminate();
        }

    public void close()
        {
        Log.debug( "KeepAliveSocketHttpTransportConnection#close" );
        }

    // From TransportConnectionSkeleton

    protected DataInputStream createDataInputStream()
        {
        if ( input == null ) throw new IllegalStateException();
        return input;
        }

    protected synchronized void cleanUp()
        {
        // Keep alive..
        }

    // Implementation

    private void storeRequest( final Request aRequest )
        {
        final String url = aRequest.toUrl();
        Log.debug( "{} url: {}", aRequest.getHttpMethod(), url );
        request = aRequest;
        }

    private void prepareForNewConnectionIfNecessary()
        {
        if ( !myConnectionClosed ) return;

        connection = null;
        output = null;
        input = null;

        myConnectionClosed = false;
        }

    private Response doProcessing() throws IOException
        {
        createConnection();
        prepareHttpConnection();
        writeRequestBodyIfAny();
        startReadingResponse();
        processResponseDataIfNecessary();
        return response;
        }

    private Response terminateConnectionAndPropagateException( final SecurityException e )
        {
        terminate();
        throw e;
        }

    private Response createNewConnectionAndPropagateException( final IOException e ) throws IOException
        {
        terminate();
        prepareForNewConnectionIfNecessary();
        throw e;
        }

    private void createConnection() throws IOException
        {
        myDetails.setFrom( request );

        if ( connection != null )
            {
            Log.debug( "KeepAliveSocketHttpTransportConnection reusing connection to {}", connection.getAddress() );
            }

        if ( connection != null ) return;

        if ( myDetails.connectUrl.indexOf( "google" ) != -1 )
            {
            throw new SecurityException( "google oops" );
            }

        connection = (SocketConnection) Connector.open( myDetails.connectUrl, Connector.READ_WRITE, true );
        connection.setSocketOption( SocketConnection.DELAY, 0 );
        connection.setSocketOption( SocketConnection.KEEPALIVE, 1 );
        }

    private void prepareHttpConnection() throws IOException
        {
        setHeaderFields();
        sendHeader();
        }

    private void setHeaderFields()
        {
        setHeaderField( "X-Client-Version", myUserAgent );

        if ( request.accept != null ) setHeaderField( "Accept", request.accept );
        if ( request.lastModified != null ) setHeaderField( "If-Modified-Since", request.lastModified );

        if ( request.hasBody() )
            {
            setHeaderField( "Content-Type", "application/octet-stream" );
            setHeaderField( "Content-Length", Integer.toString( request.getBodyBytes().length ) );
            }
        }

    private void setHeaderField( final String aKey, final String aObject )
        {
        request.headers.put( aKey, aObject );
        }

    private void sendHeader() throws IOException
        {
        if ( output == null ) output = connection.openDataOutputStream();
        myHeaderWriter.writeTo( output );
        }

    private void writeRequestBodyIfAny() throws IOException
        {
        if ( !request.hasBody() ) return;
        output.write( request.getBodyBytes() );
        output.flush();
        }

    private Response startReadingResponse() throws IOException
        {
        if ( input == null ) input = connection.openDataInputStream();

        final Hashtable headerFields = myHeaderReader.readFully( input );

        response = new Response();
        response.code = myHeaderReader.getStatusCode();
        response.setParameter( headerFields );
        return response;
        }

    private Request request;

    private boolean myConnectionClosed;

    private DataOutputStream output;

    private SocketConnection connection;

    private final String myUserAgent;

    private final ConnectionDetails myDetails = new ConnectionDetails();

    private final HttpHeaderReader myHeaderReader = new HttpHeaderReader();

    private final HttpHeaderWriter myHeaderWriter = new HttpHeaderWriter( myDetails );
    }
