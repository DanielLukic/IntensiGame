package net.intensicode.util;

import net.intensicode.core.ChainedException;

import java.io.*;

public final class URLEncoder
    {
    public static String encode( final String s )
        {
        final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        final DataOutputStream dOut = new DataOutputStream( bOut );
        final StringBuffer ret = new StringBuffer(); //return value
        try
            {
            dOut.writeUTF( s );
            }
        catch ( IOException e )
            {
            throw new ChainedException(e);
            }
        final ByteArrayInputStream bIn = new ByteArrayInputStream( bOut.toByteArray() );
        bIn.read();
        bIn.read();
        int c = bIn.read();
        while ( c >= 0 )
            {
            if ( ( c >= 'a' && c <= 'z' ) || ( c >= 'A' && c <= 'Z' ) || ( c >= '0' && c <= '9' ) || c == '.' || c == '-' || c == '*' || c == '_' )
                {
                ret.append( (char) c );
                }
            else if ( c == ' ' )
                {
                ret.append( '+' );
                }
            else
                {
                if ( c < 128 )
                    {
                    appendHex( c, ret );
                    }
                else if ( c < 224 )
                    {
                    appendHex( c, ret );
                    appendHex( bIn.read(), ret );
                    }
                else if ( c < 240 )
                        {
                        appendHex( c, ret );
                        appendHex( bIn.read(), ret );
                        appendHex( bIn.read(), ret );
                        }
                }
            c = bIn.read();
            }
        return ret.toString();
        }

    private static void appendHex( final int arg0, final StringBuffer buff )
        {
        buff.append( '%' );
        if ( arg0 < 16 )
            {
            buff.append( '0' );
            }
        buff.append( Integer.toHexString( arg0 ) );
        }
    }
