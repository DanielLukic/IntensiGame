package net.intensicode.net.connection;

import net.intensicode.net.Request;
import net.intensicode.util.Log;

import java.util.Hashtable;

public final class ConnectionDetails
    {
    public String method;

    public String connectUrl;

    public String requestPath;

    public String hostName;

    public Hashtable cookies;



    public final void setFrom( final Request aRequest )
        {
        final StringBuffer url = new StringBuffer();
        aRequest.urlifyInto( url );

        final String requestUrl = url.toString();

        method = aRequest.getHttpMethod();
        connectUrl = getConnectUrl( requestUrl );
        requestPath = getRequestPath( requestUrl );
        hostName = getHostName( requestUrl );
        cookies = aRequest.cookies;

        //#if DEBUG
        Log.debug( "method: {}", method );
        Log.debug( "connectUrl: {}", connectUrl );
        Log.debug( "requestPath: {}", requestPath );
        Log.debug( "hostName: {}", hostName );
        Log.debug( "cookies: {}", cookies );
        final int protocolMarker = requestUrl.indexOf( "://" );
        if ( protocolMarker > -1 ) throw new IllegalArgumentException( requestUrl );
        //#endif
        }

    public static String getConnectUrl( final String aRequestUrl )
        {
        final int startOfPath = aRequestUrl.indexOf( '/' );

        final StringBuffer buffer = new StringBuffer( aRequestUrl );
        if ( startOfPath > -1 ) buffer.delete( startOfPath, aRequestUrl.length() );
        buffer.insert( 0, "socket://" );
        return buffer.toString();
        }

    public static String getRequestPath( final String aRequestUrl )
        {
        final int startOfPath = aRequestUrl.indexOf( '/' );
        return aRequestUrl.substring( startOfPath );
        }

    public static String getHostName( final String aRequestUrl )
        {
        final int endOfHost = aRequestUrl.indexOf( ":" );
        final int endIndex = endOfHost == -1 ? aRequestUrl.indexOf( '/' ) : endOfHost;
        return aRequestUrl.substring( 0, endIndex );
        }
    }
