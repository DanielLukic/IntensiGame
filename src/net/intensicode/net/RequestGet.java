package net.intensicode.net;

import javax.microedition.io.HttpConnection;
import java.io.UnsupportedEncodingException;

public final class RequestGet extends Request
    {
    public RequestGet( final String aId )
        {
        super( aId );
        }

    public final byte[] getBodyBytes()
        {
        final String body = getParameter( "_body" );
        return body != null ? getBodyBytes( body ) : EMPTY_BODY_BYTES;
        }

    public final void setBody( final String aBody )
        {
        setParameter( "_body", aBody );
        }

    public String getHttpMethod()
        {
        return HttpConnection.GET;
        }

    // Implementation

    private byte[] getBodyBytes( final String aBody )
        {
        try
            {
            return aBody.getBytes( DEFAULT_BODY_ENCODING );
            }
        catch ( final UnsupportedEncodingException e )
            {
            return aBody.getBytes();
            }
        }

    private static final String DEFAULT_BODY_ENCODING = "utf-8";
    }
