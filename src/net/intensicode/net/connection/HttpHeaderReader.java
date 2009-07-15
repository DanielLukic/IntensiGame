package net.intensicode.net.connection;

import net.intensicode.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public final class HttpHeaderReader
    {
    public final int getStatusCode()
        {
        final String status = (String) myHeaderFields.get( "status" );
        //#if DEBUG
        if ( status == null ) throw new IllegalStateException();
        //#endif
        return Integer.parseInt( status.substring( 0, status.indexOf( ' ' ) ) );
        }

    public final String getContentType()
        {
        return (String) myHeaderFields.get( "content-type" );
        }

    public final String getContentEncoding()
        {
        final String contentType = getContentType();
        if ( contentType == null ) return DEFAULT_ENCODING;

        final int charsetPosition = contentType.indexOf( CHARSET_ATTRIBUTE );
        if ( charsetPosition == -1 ) return DEFAULT_ENCODING;

        final int charsetStart = charsetPosition + CHARSET_ATTRIBUTE.length();

        final int delimiterPosition = contentType.indexOf( ';', charsetPosition );
        if ( delimiterPosition < charsetPosition ) return contentType.substring( charsetStart );

        return contentType.substring( charsetStart, delimiterPosition );
        }

    public final Hashtable readFully( final InputStream aInputStream ) throws IOException
        {
        while ( true )
            {
            final String line = readLine( aInputStream );
            if ( line.length() == 0 ) break;

            //#if DEBUG
            Log.debug( "HEADER LINE: {}", line );
            //#endif

            final int assignmentMarker = line.indexOf( ':' );
            if ( assignmentMarker == -1 )
                {
                final int firstSpace = line.indexOf( ' ' );
                if ( firstSpace > 0 )
                    {
                    final String status = line.substring( firstSpace + 1 );
                    Log.debug( "STATUS: {}", status );
                    myHeaderFields.put( "status", status );
                    }
                continue;
                }

            final String key = line.substring( 0, assignmentMarker ).toLowerCase();
            final String value = line.substring( line.indexOf( ' ' ) + 1 );
            copyHeaderField( myHeaderFields, key, value );
            }

        return myHeaderFields;
        }

    public static String readLine( final InputStream aInputStream ) throws IOException
        {
        final StringBuffer buffer = new StringBuffer();
        while ( true )
            {
            final int red = aInputStream.read();
            if ( red == -1 ) break;
            if ( red == 13 )
                {
                final int check = aInputStream.read();
                if ( check != 10 ) throw new RuntimeException( "nyi" );
                break;
                }
            buffer.append( (char) red );
            }
        return buffer.toString();
        }

    public static void copyHeaderField( final Hashtable aHashtable, final String aKey, final String aValue )
        {
        if ( aKey.equalsIgnoreCase( "set-cookie" ) )
            {
            final String cookieKey = aValue.substring( 0, aValue.indexOf( '=' ) );
            final String cookieValue = aValue.substring( aValue.indexOf( '=' ) + 1, aValue.indexOf( ';' ) );
            aHashtable.put( cookieKey, cookieValue );
            }
        else
            {
            aHashtable.put( aKey, aValue );
            }
        }



    private final Hashtable myHeaderFields = new Hashtable();

    private static final String DEFAULT_ENCODING = "utf-8";

    private static final String CHARSET_ATTRIBUTE = "charset=";
    }
