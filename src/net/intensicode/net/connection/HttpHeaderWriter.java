package net.intensicode.net.connection;

import net.intensicode.util.Utilities;

import java.io.IOException;
import java.io.OutputStream;

public final class HttpHeaderWriter
    {
    public HttpHeaderWriter( final ConnectionDetails aDetails /*is allowed to change*/ )
        {
        myDetails = aDetails;
        }

    // From TransportConnection

    public final void writeTo( final OutputStream aOutputStream ) throws IOException
        {
        final StringBuffer header = new StringBuffer();
        header.append( myDetails.method );
        header.append( " " );
        header.append( myDetails.requestPath );
        header.append( " HTTP/1.1\r\n" );
        header.append( "Host: " );
        header.append( myDetails.hostName );
        header.append( "\r\n" );
        header.append( "Cache-Control: no\r\n" );
        header.append( "Connection: keep-alive\r\n" );

        if ( myDetails.cookies.size() > 0 )
            {
            header.append( "Cookie: " );
            header.append( Utilities.createCookiesString( myDetails.cookies ) );
            header.append( "\r\n" );
            }

        header.append( "\r\n" );

        aOutputStream.write( header.toString().getBytes() );
        aOutputStream.flush();
        }

    private final ConnectionDetails myDetails;
    }
