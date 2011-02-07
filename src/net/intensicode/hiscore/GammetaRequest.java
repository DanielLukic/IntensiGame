package net.intensicode.hiscore;

import net.intensicode.core.NetworkRequest;
import net.intensicode.util.*;

import java.util.Enumeration;
import java.util.Hashtable;

public final class GammetaRequest implements NetworkRequest
    {
    public GammetaRequest( final Settings aSettings, final String aHost, final String aPath, final String aMethod )
        {
        mySettings = aSettings;
        myHost = aHost;
        myPath = aPath;
        myMethod = aMethod;

        myDate = GammetaDate.now();

        /*
        Host: sls.gamecloudservices.com
        User-Agent: DK Classic/1.2 (PC)
        Date: Tue, 02 Nov 2010 03:11:11 GMT
        Content-Type: application/json
        Content-Length: 186
        X-GGC-Version: 2010-09-01
        X-GGC-Mode: test
        X-GGC-Salt: xM8UK1cHzymg4+yqF2/M301WqL4=
        Authorization: GGC JK9aiHmXZMmS1nAt6QpLvUIvsQs=
        */
        }

    public void setDate( final String aDateString )
        {
        if ( myDate == null || myDate.length() == 0 ) throw new IllegalArgumentException();
        myDate = aDateString;
        }

    public void setSaltBase64( final String aSaltBase64 )
        {
        if ( aSaltBase64 == null || aSaltBase64.length() == 0 ) throw new IllegalArgumentException();
        mySaltBase64OrNull = aSaltBase64;
        }

    private String salt()
        {
        if ( mySaltBase64OrNull != null ) return mySaltBase64OrNull;

        if ( myVariables.isEmpty() ) return EMPTY_SALT_STRING;

        final StringBuffer salt = new StringBuffer();
        final Enumeration variables = myVariables.keys();
        while ( variables.hasMoreElements() )
            {
            final String key = (String) variables.nextElement();
            final Object value = myVariables.get( key );
            salt.append( value );
            salt.append( "\n" );
            }
        salt.setLength( salt.length() - 1 );
        return BASE64SHA1.encode( salt.toString() );
        }

    private String authorization()
        {
        final GammetaAuthorization authorization = new GammetaAuthorization( mySettings );
        authorization.update( myHost, myDate, salt() );
        return authorization.toHmacSha1();
        }

    public final void addQuery( final String aKey, final String aValue )
        {
        if ( myQuery.length() == 0 ) myQuery.append( '?' );
        else myQuery.append( "&" );
        myQuery.append( URLEncoder.encode( aKey ) );
        myQuery.append( '=' );
        myQuery.append( URLEncoder.encode( aValue ) );
        }

    public final void addVariable( final String aVariable, final Object aValue )
        {
        myOrderedVariables.add( aVariable );
        myVariables.put( aVariable, aValue );
        }

    public final void addTag( final String aTag )
        {
        myTags.add( aTag );
        }

    // From NetworkRequest

    public final String method()
        {
        return myMethod;
        }

    public final String url()
        {
        return "http://" + myHost + "/" + myPath + myQuery;
        }

    public final Enumeration headers()
        {
        if ( myHeaders.isEmpty() ) updateHeaders();
        return myHeaders.keys();
        }

    private void updateHeaders()
        {
        myHeaders.put( "Host", myHost );
        myHeaders.put( "User-Agent", mySettings.userAgent );
        myHeaders.put( "Date", myDate );
        if ( myMethod == METHOD_GET )
            {
            myHeaders.put( "Accept", "application/json" );
            }
        if ( myMethod == METHOD_POST )
            {
            myHeaders.put( "Content-Type", "application/json" );
            myHeaders.put( "Content-Length", String.valueOf( body().length ) );
            }
        myHeaders.put( "X-GGC-Version", "2010-09-01" );
        myHeaders.put( "X-GGC-Mode", mySettings.testMode ? "test" : "live" );

        final String saltBase64 = salt();
        if ( saltBase64 != EMPTY_SALT_STRING ) myHeaders.put( "X-GGC-Salt", saltBase64 );

        myHeaders.put( "Authorization", authorization() );
        }

    public final String headerValue( final String aHeaderKey )
        {
        if ( myHeaders.isEmpty() ) updateHeaders();
        return String.valueOf( myHeaders.get( aHeaderKey ) );
        }

    public final byte[] body()
        {
        final StringBuffer body = new StringBuffer();
        body.append( "{\n" );

        for ( int idx = 0; idx < myOrderedVariables.size; idx++ )
            {
            final String key = (String) myOrderedVariables.get( idx );
            final Object value = myVariables.get( key );
            append( body, key, value );
            }

        if ( !myTags.empty() )
            {
            body.append( "\t\"Tags\": [" );
            for ( int idx = 0; idx < myTags.size; idx++ )
                {
                final String tag = (String) myTags.get( idx );
                body.append( '"' );
                body.append( tag );
                body.append( "\", " );
                }
            body.setLength( body.length() - 2 );
            body.append( "]\n" );
            }

        if ( !myVariables.isEmpty() && myTags.empty() )
            {
            body.setLength( body.length() - 2 );
            body.append( "\n" );
            }

        body.append( "}" );

        Log.info( "GammetaRequest BODY:\n{}", body );

        return body.toString().getBytes();
        }

    private void append( final StringBuffer aBody, final String aKey, final Object aValue )
        {
        aBody.append( '\t' );
        aBody.append( '"' );
        aBody.append( aKey );
        aBody.append( "\"" );
        aBody.append( ": " );
        if ( aValue instanceof String )
            {
            aBody.append( '"' );
            aBody.append( aValue );
            aBody.append( '"' );
            }
        else
            {
            aBody.append( aValue );
            }
        aBody.append( ",\n" );
        }

    private String myDate;

    private String mySaltBase64OrNull;

    private final Settings mySettings;

    private final String myHost;

    private final String myPath;

    private final String myMethod;

    private final Hashtable myHeaders = new Hashtable();

    private final StringBuffer myQuery = new StringBuffer();

    private final Hashtable myVariables = new Hashtable();

    private final DynamicArray myTags = new DynamicArray();

    private final DynamicArray myOrderedVariables = new DynamicArray();

    private static final String EMPTY_SALT_STRING = "";
    }
