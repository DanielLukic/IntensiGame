package net.intensicode.util;

import net.intensicode.net.Response;

import javax.microedition.io.Connection;
import javax.microedition.io.HttpConnection;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONArray;

public class Utilities
    {
    public static final String SILENT_PHONE_NUMBER_CHARS = "()/- ";

    public static final String DEFAULT_ENCODING = "utf-8";



    public static String normalizePhoneNumber( final String aDirtyPhoneNumber )
        {
        if ( aDirtyPhoneNumber == null ) return null;

        // 0170887766123
        // 0170 887766123
        // 0170/887766123
        // 0170 / 887766123
        // 0170 88-77-66 123
        // 0170/88-77-66 123
        // 0170 / 88-77-66 123
        // (0170) 88-77-66 123
        // (0170)/88-77-66 123
        // (0170) / 88-77-66 123
        // +49 170 88-77-66 123
        // +49-170 88-77-66 123
        // +49(0)170 88-77-66 123
        // +49-(0)170 / 88-7766 (123)
        // +49-(0)170 / 88-7766 (0)/123

        final StringBuffer buffer = new StringBuffer( aDirtyPhoneNumber );
        while ( true )
            {
            final int hiddenZeroIndex = buffer.toString().indexOf( "(0)" );
            if ( hiddenZeroIndex == -1 ) break;
            buffer.delete( hiddenZeroIndex, hiddenZeroIndex + 2 );
            }
        if ( buffer.charAt( 0 ) == '+' )
            {
            buffer.deleteCharAt( 0 );
            buffer.insert( 0, "00" );
            }
        for ( int idx = buffer.length() - 1; idx >= 0; idx-- )
            {
            if ( SILENT_PHONE_NUMBER_CHARS.indexOf( buffer.charAt( idx ) ) == -1 ) continue;
            buffer.deleteCharAt( idx );
            }
        return buffer.toString();
        }

    public static boolean isValidLocation( final double aLongitude, final double aLatitude )
        {
        return isValidLongitude( aLongitude ) && isValidLatitude( aLatitude );
        }

    public static boolean isValidLongitude( final double aLongitude )
        {
        return aLongitude >= -180 && aLongitude <= 180;
        }

    public static boolean isValidLatitude( final double aLatitude )
        {
        return aLatitude >= -90 && aLatitude <= 90;
        }

    public static String getCombinedStringOrNull( final String aFirstOrNull, final String aDelimiter, final String aLastOrNull )
        {
        final StringBuffer realName = new StringBuffer();
        if ( aFirstOrNull != null && aFirstOrNull.length() > 0 && !aFirstOrNull.equalsIgnoreCase( "null" ) )
            {
            realName.append( aFirstOrNull );
            }
        if ( aLastOrNull != null && aLastOrNull.length() > 0 && !aLastOrNull.equalsIgnoreCase( "null" ) )
            {
            realName.append( aDelimiter );
            realName.append( aLastOrNull );
            }
        if ( realName.length() == 0 ) return null;
        return realName.toString();
        }

    public static String httpEscape( final String aUnescapedUrl )
        {
        final StringBuffer buffer = new StringBuffer( aUnescapedUrl );
        for ( int idx = buffer.length() - 1; idx >= 0; idx-- )
            {
            final char code = buffer.charAt( idx );
            if ( code >= '0' && code <= '9' ) continue;
            if ( code >= 'a' && code <= 'z' ) continue;
            if ( code >= 'A' && code <= 'Z' ) continue;
            buffer.deleteCharAt( idx );
            final String hex = Integer.toHexString( code ).toUpperCase();
            buffer.insert( idx, hex );
            if ( hex.length() == 1 ) buffer.insert( idx, "0" );
            buffer.insert( idx, "%" );
            }
        return buffer.toString();
        }

    public static String determineContentEncoding( final String aForcedEncodingOrNull, final String aContentType )
        {
        if ( aForcedEncodingOrNull != null ) return aForcedEncodingOrNull;

        if ( aContentType == null ) return null;

        final int charsetPosition = aContentType.indexOf( CHARSET_ATTRIBUTE );
        if ( charsetPosition == -1 ) return null;

        final int charsetStart = charsetPosition + CHARSET_ATTRIBUTE.length();

        final int delimiterPosition = aContentType.indexOf( ';', charsetPosition );
        if ( delimiterPosition < charsetPosition ) return aContentType.substring( charsetStart );

        return aContentType.substring( charsetStart, delimiterPosition );
        }

    public static void copyHeaderFields( final HttpConnection aConnection, final Response aResponse, final int aIndex ) throws IOException
        {
        final String data = aConnection.getHeaderField( aIndex );
        if ( data == null ) return;

        final String key = aConnection.getHeaderFieldKey( aIndex );
        final String value = aConnection.getHeaderField( aIndex );
        if ( key != null && value != null )
            {
            copyHeaderField( aResponse, key, value );
            }
        else if ( aIndex == 0 && value != null )
            {
            aResponse.setParameter( "Status", value );
            }
        else
            {
            Log.debug( "Unexpected null header at index {}", aIndex );
            }

        copyHeaderFields( aConnection, aResponse, aIndex + 1 );
        }

    public static void copyHeaderField( final Response aResponse, final String aKey, final String aValue )
        {
        if ( aKey.toLowerCase().equals( "set-cookie" ) )
            {
            final String cookieKey = aValue.substring( 0, aValue.indexOf( '=' ) );
            final String cookieValue = aValue.substring( aValue.indexOf( '=' ) + 1, aValue.indexOf( ';' ) );
            aResponse.setParameter( cookieKey, cookieValue );
            //#ifdef HTTP_DEBUG
            //# Log.debug( "cookie: {} => {}", cookieKey, cookieValue );
            //#endif
            }
        else
            {
            aResponse.setParameter( aKey, aValue );
            //#ifdef HTTP_DEBUG
            //# Log.debug( "headerField: {} => {}", key, value );
            //#endif
            }
        }

    public static void setCookies( final HttpConnection aConnection, final Hashtable aCookiesHash ) throws IOException
        {
        final String cookies = createCookiesString( aCookiesHash );
        aConnection.setRequestProperty( "cookie", cookies );

        //#ifdef HTTP_DEBUG
        //# Log.debug( "Cookies: {}", cookies );
        //#endif
        }

    public static String createCookiesString( final Hashtable aCookies ) throws IOException
        {
        final StringBuffer buffer = new StringBuffer();

        final Enumeration keys = aCookies.keys();
        while ( keys.hasMoreElements() )
            {
            final Object key = keys.nextElement();
            final Object value = aCookies.get( key );
            buffer.append( key );
            buffer.append( '=' );
            buffer.append( value );
            buffer.append( ';' );
            }
        return buffer.toString();
        }

    public static ByteArrayOutputStream readIntoByteArray( final InputStream aInputStream ) throws IOException
        {
        if ( aInputStream == null ) throw new IOException( "Resource not found" );

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
            {
            final byte[] buffer = new byte[4096];
            while ( true )
                {
                final int read = aInputStream.read( buffer );
                if ( read == -1 ) break;
                os.write( buffer, 0, read );
                }
            }
        finally
            {
            aInputStream.close();
            os.close();
            }

        return os;
        }

    public static String readIntoString( final Object aReference, final String aResourcePath ) throws IOException
        {
        final InputStream stream = aReference.getClass().getResourceAsStream( aResourcePath );
        if ( stream == null ) throw new IOException( "resource not found: " + aResourcePath );
        return readIntoString( stream, DEFAULT_ENCODING );
        }

    public static String readIntoString( final InputStream aInputStream ) throws IOException
        {
        return readIntoString( aInputStream, DEFAULT_ENCODING );
        }

    public static String readIntoString( final InputStream aInputStream, final String aEncoding ) throws IOException
        {
        final ByteArrayOutputStream os = readIntoByteArray( aInputStream );

        final String encoding = aEncoding != null ? aEncoding : DEFAULT_ENCODING;
        try
            {
            return new String( os.toByteArray(), encoding );
            }
        catch ( final UnsupportedEncodingException e )
            {
            return new String( os.toByteArray() );
            }
        }

    public static DynamicArray readTextResourceLines( final Object aReference, final String aResourcePath ) throws IOException
        {
        final String fileData = readIntoString( aReference, aResourcePath );
        final DynamicArray lines = new DynamicArray();
        StringUtil.splitLines( fileData, lines, 0 );
        return lines;
        }

    public static void closeSafely( final Object aStreamOrConnection )
        {
        try
            {
            if ( aStreamOrConnection instanceof InputStream ) ( (InputStream) aStreamOrConnection ).close();
            else if ( aStreamOrConnection instanceof OutputStream ) ( (OutputStream) aStreamOrConnection ).close();
            else if ( aStreamOrConnection instanceof Connection ) ( (Connection) aStreamOrConnection ).close();
            //#if DEBUG
            else throw new IllegalArgumentException( String.valueOf( aStreamOrConnection ) );
            //#endif
            }
        catch ( final Throwable t )
            {
            Log.error( "Utilities#closeSafely - ignoring exception", t );
            }
        }
    public static void processJsonResponse( final Response aResponse, final String aResponseString ) throws IOException
        {
        try
            {
            // Temporary(?) fix for broken insomnia /mobile/spots/bounding_box service:
            if ( aResponseString.equals( "[]" ) )
                {
                aResponse.setParameter( "spots", new JSONArray() );
                return;
                }

            final JSONObject jsonObject = new JSONObject( aResponseString );
            //#ifdef HTTP_DEBUG
            //# Log.debug( "JSON response:\n{}", jsonObject.toString( 2 ) );
            //#endif

            final Enumeration keys = jsonObject.keys();
            while ( keys.hasMoreElements() )
                {
                final String key = (String) keys.nextElement();
                final Object value = jsonObject.get( key );
                aResponse.setParameter( key, value );
                }
            }
        catch ( final JSONException e )
            {
            //#ifdef DEBUG
            e.printStackTrace();
            //#endif
            throw new IOException( e.toString() );
            }
        }

    public static boolean hasPimApi()
        {
        try
            {
            final String apiVersion = System.getProperty( "microedition.pim.version" );
            if ( apiVersion == null ) return false;

            final float version = Float.parseFloat( apiVersion );
            if ( version < 1f ) return false;
            }
        catch ( final Throwable aThrowable )
            {
            return false;
            }
        return true;
        }

    public static boolean hasWmApi()
        {
        try
            {
            Class.forName( "javax.wireless.messaging.Message" );
            return true;
            }
        catch ( final Throwable aThrowable )
            {
            // Just ignore..
            }
        return false;
        }

    public static boolean hasLocationApi()
        {
        try
            {
            Class.forName( "javax.microedition.location.Location" );
            return true;
            }
        catch ( final Throwable aThrowable )
            {
            // Just ignore..
            }
        return false;
        }

    private static final String CHARSET_ATTRIBUTE = "charset=";
    }