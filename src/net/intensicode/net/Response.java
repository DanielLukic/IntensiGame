package net.intensicode.net;

import org.json.me.*;

import javax.microedition.lcdui.Image;
import java.util.*;

public final class Response
    {
    public static final int HTTP_OK = 200;

    public static final int HTTP_NOT_MODIFIED = 304;

    public static final Response NULL = new Response( "NULL" );

    public static final Response SUCCESS = new Response( "{\"success\": true}", HTTP_OK ).setParameter( "success", "true" );

    public static final Response CANCELLED = new Response( "CANCELLED" );

    public static final Response TIMED_OUT = new Response( "TIMED_OUT" );

    public static final String IMAGE_DATA_KEY = "image_data";

    public static final String BODY = "body";

    public final JSONObject data = new JSONObject();

    public int code;

    public Response()
        {
        }

    public Response( final String aData )
        {
        setParameter( BODY, aData );
        }

    public Response( final String aData, final int aHttpStatusCode )
        {
        this( aData );
        code = aHttpStatusCode;
        }

    public void setParameter( final Hashtable aParameters )
        {
        final Enumeration enumeration = aParameters.keys();
        while ( enumeration.hasMoreElements() )
            {
            final Object key = enumeration.nextElement();
            final Object value = aParameters.get( key );
            setParameter( (String) key, value );
            }
        }

    public final Response setParameter( final String aKey, final Object aValue )
        {
        data.put( aKey.toLowerCase(), aValue );
        return this;
        }

    public final int getInt( final String aKey )
        {
        if ( data.isNull( aKey ) ) throw new IllegalStateException( aKey );
        return Integer.parseInt( data.get( aKey ).toString() );
        }

    public final double getDouble( final String aKey )
        {
        if ( data.isNull( aKey ) ) throw new IllegalStateException( aKey );
        return Double.parseDouble( data.get( aKey ).toString() );
        }

    public final String getParameter( final String aKey )
        {
        if ( data.isNull( aKey ) ) return null;
        return data.get( aKey ).toString();
        }

    public final int getNumberOfBytes()
        {
        if ( data.has( IMAGE_DATA_KEY ) ) return getImageData().length;
        if ( data.has( BODY ) ) return data.getString( BODY ).length();
        return 0;
        }

    public final byte[] getImageData()
        {
        return (byte[]) data.get( IMAGE_DATA_KEY );
        }

    public final Image getImage()
        {
        final byte[] imageData = getImageData();
        return Image.createImage( imageData, 0, imageData.length );
        }

    public String getBody()
        {
        if ( data.has( BODY ) ) return data.getAsString( BODY );
        return data.toString();
        }

    public String toErrorString()
        {
        if ( data.has( "error" ) ) return data.getString( "error" );
        else if ( data.has( "backtrace" ) ) return data.getString( "message" );
        else if ( data.has( "validation_errors" ) ) return createMessageFromValidationErrors();
        else return toString();
        }

    public void addToContext( final Hashtable aContext )
        {
        aContext.put( "response_code", Integer.toString( code ) );
        aContext.put( "response_error", toErrorString() );
        }

    // From Object

    public final String toString()
        {
        return data.toString();
        }

    // Implementation

    private String createMessageFromValidationErrors()
        {
        final Object validationErrors = data.get( "validation_errors" );
        if ( validationErrors instanceof JSONArray ) return createMessageFromValidationErrorsArray();
        if ( validationErrors instanceof JSONObject ) return createMessageFromValidationErrorsHash();
        throw new IllegalArgumentException( "unknown validation errors format: " + validationErrors );
        }

    private String createMessageFromValidationErrorsArray()
        {
        final JSONArray array = data.getJSONArray( "validation_errors" );
        final StringBuffer message = new StringBuffer();
        for ( int idx = 0; idx < array.length(); idx++ )
            {
            final JSONArray entry = array.getJSONArray( idx );
            message.append( entry.getString( 0 ) );
            message.append( ": " );
            message.append( entry.getString( 1 ) );
            message.append( "\n" );
            }
        if ( message.length() > 0 ) message.setLength( message.length() - 1 );
        return message.toString();
        }

    private String createMessageFromValidationErrorsHash()
        {
        final JSONObject hash = data.getJSONObject( "validation_errors" );
        final StringBuffer message = new StringBuffer();
        final Enumeration keys = hash.keys();
        while ( keys.hasMoreElements() )
            {
            final String key = (String) keys.nextElement();
            final JSONArray errors = hash.getJSONArray( key );

            message.append( key );
            message.append( ": " );

            for ( int idx = 0; idx < errors.length(); idx++ )
                {
                final Object entry = errors.get( idx );
                message.append( entry );
                message.append( "," );
                }

            message.setCharAt( message.length() - 1, '\n' );
            }

        return message.toString();
        }
    }
