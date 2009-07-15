package net.intensicode.net.connection;

import net.intensicode.net.*;
import net.intensicode.util.Log;
import net.intensicode.util.Utilities;

import javax.microedition.io.*;
import java.io.*;
import java.util.Enumeration;

public final class HttpTransportConnection extends TransportConnectionSkeleton
    {
    public HttpTransportConnection()
        {
        this( "QiroMobile" );
        }

    public HttpTransportConnection( final String aUserAgent )
        {
        myUserAgent = aUserAgent;
        }

    // From TransportConnection

    public final void prepare( final Request aRequest ) throws IOException
        {
        final String url = aRequest.toUrl();
        Log.debug( "{} url: {}", aRequest.getHttpMethod(), url );

        createHttpConnection( url );
        prepareHttpConnection( aRequest );
        }

    public final Response process( final Request aRequest ) throws IOException
        {
        writeRequestBodyIfAny( aRequest );
        startReadingResponse();
        processResponseDataIfNecessary();
        return response;
        }

    // Protected Implementation

    protected DataInputStream createDataInputStream() throws IOException
        {
        return connection.openDataInputStream();
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

    private void createHttpConnection( final String aUrl ) throws IOException
        {
        connection = (HttpConnection) Connector.open( aUrl );
        }

    private void prepareHttpConnection( final Request aRequest ) throws IOException
        {
        connection.setRequestMethod( aRequest.getHttpMethod() );

        setHeaderFields( aRequest );

        Utilities.setCookies( connection, aRequest.cookies );
        }

    private void setHeaderFields( final Request aRequest ) throws IOException
        {
        final Enumeration keys = aRequest.headers.keys();
        while ( keys.hasMoreElements() )
            {
            final String key = String.valueOf( keys.nextElement() );
            final String object = String.valueOf( aRequest.headers.get( key ) );
            Log.debug( "Adding header {} => {}", key, object );
            setHeaderField( key, object );
            }

        if ( aRequest.accept != null ) setHeaderField( "Accept", aRequest.accept );
        if ( aRequest.lastModified != null ) setHeaderField( "If-Modified-Since", aRequest.lastModified );

        setHeaderField( "X-Client-Version", myUserAgent );

        if ( aRequest.hasBody() )
            {
            setHeaderField( "Content-Type", "application/octet-stream" );
            setHeaderField( "Content-Length", Integer.toString( aRequest.getBodyBytes().length ) );
            }
        }

    private void setHeaderField( final String aKey, final String aObject ) throws IOException
        {
        connection.setRequestProperty( aKey, aObject );
        }

    private Response startReadingResponse() throws IOException
        {
        response = new Response();
        response.code = connection.getResponseCode();
        Utilities.copyHeaderFields( connection, response, 0 );
        return response;
        }

    private void writeRequestBodyIfAny( final Request aRequest ) throws IOException
        {
        if ( !aRequest.hasBody() ) return;
        output = connection.openDataOutputStream();
        output.write( aRequest.getBodyBytes() );
        output.flush();
        }

    private DataOutputStream output;

    private HttpConnection connection;

    private final String myUserAgent;
    }
