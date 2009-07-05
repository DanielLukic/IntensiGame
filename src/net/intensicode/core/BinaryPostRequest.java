package net.intensicode.core;

import net.intensicode.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;



/**
 * TODO: Describe this!
 */
final class BinaryPostRequest implements NetworkRequest
{
    BinaryPostRequest( final String aURL, final byte[] aBody, final NetworkCallback aCallback )
    {
        myURL = aURL;
        myBody = aBody;
        myCallback = aCallback;
    }

    // From NetworkRequest

    public final NetworkCallback callback()
    {
        return myCallback;
    }

    public final void execute()
    {
        try
        {
            NetworkIO.theStatus = "OPENING CONNECTION";

            final HttpConnection connection = ( HttpConnection ) Connector.open( myURL, Connector.READ_WRITE, true );
            connection.setRequestMethod( HttpConnection.GET );

            //#if DEBUG
            Log.debug( "Sending body: {}", new String( myBody ) );
            //#endif

            NetworkIO.theStatus = "UPLOADING HISCORE";

            final DataOutputStream output = connection.openDataOutputStream();
            output.write( myBody );
            output.close();

            final long contentLength = connection.getLength();
            final int responseCode = connection.getResponseCode();
            final String responseMessage = connection.getResponseMessage();

            //#if DEBUG
            Log.debug( "Content length: {}", contentLength );
            Log.debug( "Response code: {}", responseCode );
            Log.debug( "Response message: {}", responseMessage );
            //#endif

            NetworkIO.theStatus = "DOWNLOADING HISCORE";

            final DataInputStream input = connection.openDataInputStream();
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            while ( true )
            {
                final int newBytes = input.read( NetworkIO.theBuffer );
                if ( newBytes == -1 ) break;
                bytes.write( NetworkIO.theBuffer, 0, newBytes );
                if ( contentLength > 0 && bytes.size() >= contentLength ) break;
            }

            //#if DEBUG
            System.out.println( bytes.toString() );
            //#endif

            final byte[] responseBytes = bytes.toByteArray();
            myCallback.onReceived( responseBytes );

            NetworkIO.theStatus = "OK";
        }
        catch ( final IOException e )
        {
            myCallback.onError( e );
        }
    }



    private final String myURL;

    private final byte[] myBody;

    private final NetworkCallback myCallback;
}
