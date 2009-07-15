package net.intensicode.net.connection;

import net.intensicode.net.*;
import net.intensicode.util.Log;
import net.intensicode.util.Utilities;

import javax.microedition.io.*;
import java.io.*;
import java.util.Hashtable;

public final class SocketHttpTransportConnection extends TransportConnectionSkeleton
    {
    public SocketHttpTransportConnection()
        {
        this( "QiroMobile" );
        }

    public SocketHttpTransportConnection( final String aUserAgent )
        {
        myUserAgent = aUserAgent;
        }

    // From TransportConnection

    public void prepare( final Request aRequest )
        {
        final String url = aRequest.toUrl();
        Log.debug( "{} url: {}", aRequest.getHttpMethod(), url );

        request = aRequest;
        }

    public final Response process( final Request aRequest ) throws IOException
        {
        createConnection();
        prepareHttpConnection();
        writeRequestBodyIfAny();
        startReadingResponse();
        processResponseDataIfNecessary();
        return response;
        }

    // From TransportConnectionSkeleton

    protected DataInputStream createDataInputStream()
        {
        if ( input == null ) throw new IllegalStateException();
        return input;
        }

    protected synchronized void cleanUp()
        {
        if ( output != null ) Utilities.closeSafely( output );
        output = null;
        super.cleanUp();
        if ( connection != null ) Utilities.closeSafely( connection );
        connection = null;
        }

    // Implementation

    private void createConnection() throws IOException
        {
        myDetails.setFrom( request );

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
        output = connection.openDataOutputStream();
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
        input = connection.openDataInputStream();

        final Hashtable headerFields = myHeaderReader.readFully( input );

        response = new Response();
        response.code = myHeaderReader.getStatusCode();
        response.setParameter( headerFields );

        return response;
        }

    private Request request;

    private DataOutputStream output;

    private SocketConnection connection;

    private final String myUserAgent;

    private final ConnectionDetails myDetails = new ConnectionDetails();

    private final HttpHeaderReader myHeaderReader = new HttpHeaderReader();

    private final HttpHeaderWriter myHeaderWriter = new HttpHeaderWriter( myDetails );
    }
