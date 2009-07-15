package net.intensicode.net;

import org.json.me.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.*;

import net.intensicode.util.Utilities;

public abstract class Request
    {
    public final JSONObject data = new JSONObject();

    public final Hashtable headers;

    public final Hashtable cookies;

    public final String id;

    public int autoRetries;

    public String lastModified;

    public String baseUrl;

    public String accept = "application/json";



    public void addToContext( final Hashtable aContext )
        {
        aContext.put( "request_url", toUrl() );
        }

    public final String getParameter( final String aKey )
        {
        if ( data.isNull( aKey ) ) return null;
        return data.get( aKey ).toString();
        }

    public final void setParameter( final String aKey, final Object aValue )
        {
        data.put( aKey, aValue );
        }

    public void urlifyInto( final StringBuffer aBuffer )
        {
        if ( baseUrl != null )
            {
            aBuffer.setLength( 0 );
            aBuffer.append( baseUrl );
            }

        aBuffer.append( '/' );
        aBuffer.append( id );

        if ( data.length() == 0 ) return;

        aBuffer.append( '?' );

        urlifyParamtersInto( aBuffer );
        }

    public final String toUrl()
        {
        final StringBuffer buffer = new StringBuffer();
        urlifyInto( buffer );

        final String result = buffer.toString();
        if ( result.startsWith( "http://" ) ) return result;

        insertHttpProtocolMarker( buffer, result );
        return buffer.toString();
        }

    public boolean hasBody()
        {
        return getBodyBytes().length > 0;
        }

    public abstract byte[] getBodyBytes();

    public abstract void setBody( String aBody ) throws UnsupportedEncodingException;

    public abstract String getHttpMethod();

    // From Object

    //#if DEBUG || LOGGING

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer( "Request(" );
        urlifyInto( buffer );
        return buffer.toString();
        }

    //#endif

    // Protected Interface

    protected Request( final String aId )
        {
        id = aId;
        cookies = new Hashtable();
        headers = new Hashtable();
        }

    // Implementation

    private void insertHttpProtocolMarker( final StringBuffer aBuffer, final String aResult )
        {
        removeProtocolMarkerIfNecessary( aBuffer, aResult );
        aBuffer.insert( 0, "http://" );
        }

    private void removeProtocolMarkerIfNecessary( final StringBuffer aBuffer, final String aResult )
        {
        final int protocolMarkerIndex = aResult.indexOf( "://" );
        if ( protocolMarkerIndex > -1 ) aBuffer.delete( 0, protocolMarkerIndex + 3 );
        }

    private void urlifyParamtersInto( final StringBuffer aBuffer )
        {
        final Enumeration keys = data.keys();
        while ( keys.hasMoreElements() )
            {
            final Object key = keys.nextElement();
            final Object value = data.get( key.toString() );
            if ( value == null ) continue;
            aBuffer.append( Utilities.httpEscape( key.toString() ) );
            aBuffer.append( '=' );
            aBuffer.append( Utilities.httpEscape( value.toString() ) );
            aBuffer.append( '&' );
            }
        aBuffer.setLength( aBuffer.length() - 1 );
        }

    protected static final byte[] EMPTY_BODY_BYTES = new byte[0];
    }
