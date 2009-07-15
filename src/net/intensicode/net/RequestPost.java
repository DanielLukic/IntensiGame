package net.intensicode.net;

import javax.microedition.io.HttpConnection;
import java.io.UnsupportedEncodingException;

public final class RequestPost extends Request
    {
    public RequestPost( final String aId )
        {
        super( aId );
        }

    public final byte[] getBodyBytes()
        {
        return myBodyBytes != null ? myBodyBytes : EMPTY_BODY_BYTES;
        }

    public final void setBody( final String aBody ) throws UnsupportedEncodingException
        {
        myBodyBytes = aBody.getBytes( "utf-8" );
        }

    public final String getHttpMethod()
        {
        return HttpConnection.POST;
        }

    private byte[] myBodyBytes;
    }
