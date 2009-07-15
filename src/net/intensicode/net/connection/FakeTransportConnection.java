//#condition !fake_remove
package net.intensicode.net.connection;

import net.intensicode.net.*;

import java.io.*;

public final class FakeTransportConnection extends TransportConnectionSkeleton
    {
    public final void prepare( final Request aRequest ) throws IOException
        {
        final StringBuffer url = createFakeResourceUrl( aRequest );
        myResourceUrl = url.toString();
        }

    public final Response process( final Request aRequest ) throws IOException
        {
        startReadingResponse( aRequest );
        processResponseDataIfNecessary();
        return response;
        }

    // Protected Implementation

    protected DataInputStream createDataInputStream() throws IOException
        {
        final InputStream stream = getClass().getResourceAsStream( myResourceUrl );
        if ( stream == null ) throw new IOException( "not found: " + myResourceUrl );
        return new DataInputStream( stream );
        }

    // Implementation

    private StringBuffer createFakeResourceUrl( final Request aRequest )
        {
        final StringBuffer url = new StringBuffer( "/fake/" );
        url.append( aRequest.id );
        if ( isImageRequest( aRequest ) ) url.append( "/data.png" );
        else url.append( "/data.json" );
        return url;
        }

    private boolean isImageRequest( final Request aRequest )
        {
        return aRequest.accept.indexOf( "png" ) != -1;
        }

    private Response startReadingResponse( final Request aRequest ) throws IOException
        {
        response = new Response();
        response.code = 200;
        if ( isImageRequest( aRequest ) )
            {
            response.setParameter( "content-type", "image/png" );
            }
        else
            {
            response.setParameter( "content-type", "text/plain" );
            }
        return response;
        }

    private String myResourceUrl;
    }
