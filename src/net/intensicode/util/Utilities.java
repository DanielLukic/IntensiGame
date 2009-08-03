package net.intensicode.util;

import javax.microedition.io.*;
import java.io.*;

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

    //#if CLDC11

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

    //#endif

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

    //#if CLDC11

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

    //#endif

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
