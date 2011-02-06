package net.intensicode.hiscore;

import net.intensicode.core.ChainedException;
import net.intensicode.util.*;

import java.io.UnsupportedEncodingException;

public final class GammetaAuthorization
    {
    public GammetaAuthorization( final Settings aSettings )
        {
        mySettings = aSettings;
        }

    public final void update( final String aHost, final String aDate, final String aSalt )
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( aHost );
        buffer.append( "\n" );
        buffer.append( mySettings.userAgent );
        buffer.append( "\n" );
        buffer.append( aDate );
        buffer.append( "\n" );
        buffer.append( mySettings.gameId );
        buffer.append( "\n" );
        if ( aSalt != null && aSalt.length() > 0 ) buffer.append( aSalt );

        Log.info( "authorization:\n{}", buffer );

        final byte[] inputBytesUtf8 = getUtf8BytesFromString( buffer.toString() );

        final HMACSHA1 digest = new HMACSHA1();
        digest.init( getUtf8BytesFromString( mySettings.key ) );
        digest.update( inputBytesUtf8, 0, inputBytesUtf8.length );

        final byte[] outputBytesUtf8 = digest.doFinal();
        myHmacSha1 = "GGC " + Base64.encodeBytes( outputBytesUtf8 );
        }

    private byte[] getUtf8BytesFromString( final String aData )
        {
        try
            {
            return aData.getBytes( "UTF-8" );
            }
        catch ( final UnsupportedEncodingException e )
            {
            throw new ChainedException( e );
            }
        }

    public final String toHmacSha1()
        {
        return myHmacSha1;
        }

    private String myHmacSha1;

    private final Settings mySettings;
    }
