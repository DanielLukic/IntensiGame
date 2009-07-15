package net.intensicode.net.connection;

import net.intensicode.net.Response;
import net.intensicode.util.Utilities;

import java.io.*;

public abstract class TransportConnectionSkeleton implements TransportConnection
    {
    public boolean hasBeenInterrupted()
        {
        return false;
        }

    public void cancel()
        {
        cleanUp();
        }

    public void close()
        {
        cleanUp();
        }

    // Protected Interface

    protected abstract DataInputStream createDataInputStream() throws IOException;

    protected void processResponseDataIfNecessary() throws IOException
        {
        if ( isNotModified() ) return;

        final byte[] data = readResponseData();

        if ( getContentType().startsWith( "image/" ) )
            {
            createImageResponse( data );
            }
        else
            {
            createJsonOrTextResponse( data );
            }
        }

    protected synchronized void cleanUp()
        {
        if ( input != null ) Utilities.closeSafely( input );
        input = null;
        }

    protected Response response;

    protected DataInputStream input;

    // Implementation

    private boolean isNotModified()
        {
        return response.code == Response.HTTP_NOT_MODIFIED;
        }

    private void createJsonOrTextResponse( final byte[] aData ) throws IOException
        {
        final String responseString = createResponseString( aData );

        checkForMaintenancePage( responseString );

        final String contentType = getContentType();
        if ( isJsonData( contentType, responseString ) )
            {
            createJsonResponse( responseString );
            }
        else
            {
            createTextResponse( responseString );
            }
        }

    private void checkForMaintenancePage( final String aResponseString ) throws IOException
        {
        if ( isMaintenancePage( aResponseString ) ) throw new IOException( "maintenance" );
        }

    private void createTextResponse( final String aResponseString )
        {
        response.setParameter( Response.BODY, aResponseString );
        }

    private void createJsonResponse( final String aResponseString ) throws IOException
        {
        response.setParameter( Response.BODY, aResponseString );
        Utilities.processJsonResponse( response, aResponseString );
        }

    private String createResponseString( final byte[] aData ) throws UnsupportedEncodingException
        {
        return new String( aData, DEFAULT_RESPONSE_ENCODING );
        }

    private String getContentType()
        {
        return response.getParameter( "content-type" );
        }

    private void createImageResponse( final byte[] aData )
        {
        response.setParameter( Response.IMAGE_DATA_KEY, aData );
        }

    private byte[] readResponseData() throws IOException
        {
        input = createDataInputStream();

        final String contentLength = response.getParameter( "content-length" );
        if ( contentLength == null )
            {
            return readUntilEndOfStream();
            }
        else
            {
            return readWithContentLength( contentLength );
            }
        }

    private byte[] readUntilEndOfStream() throws IOException
        {
        return Utilities.readIntoByteArray( input ).toByteArray();
        }

    private byte[] readWithContentLength( final String aContentLength ) throws IOException
        {
        final int lengthInBytes = Integer.parseInt( aContentLength );
        final byte[] data = new byte[lengthInBytes];
        input.readFully( data );
        return data;
        }

    private boolean isMaintenancePage( final String aResponseString )
        {
        return aResponseString.startsWith( "<!DOCTYPE" ) && aResponseString.indexOf( "The system is down for maintenance." ) != -1;
        }

    private boolean isJsonData( final String aContentType, final String aResponseString )
        {
        return aContentType.startsWith( "application/json" ) || aResponseString.startsWith( "{" );
        }

    private static final String DEFAULT_RESPONSE_ENCODING = "utf-8";
    }
