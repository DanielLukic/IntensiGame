package net.intensicode.net.system;

import net.intensicode.net.Request;
import net.intensicode.net.Response;
import net.intensicode.net.TransportException;

final class TransportEvent
    {
    public int priority;

    public final Request request;

    public final TransportSystemCallback callback;



    public TransportEvent( final int aPriority, final Request aRequest, final TransportSystemCallback aCallback )
        {
        priority = aPriority;
        request = aRequest;
        callback = aCallback;
        }

    public final void setBaseUrlIfNecessary( final String aBaseUrl )
        {
        if ( request.baseUrl != null ) return;
        request.baseUrl = aBaseUrl;
        }

    public final void completeRequestWith( final Response aResponse )
        {
        callback.requestComplete( request, aResponse );
        }

    public final void failRequestWith( final TransportException aException )
        {
        callback.requestFailed( request, aException );
        }

    // From Object

    public final String toString()
        {
        return request.id;
        }
    }
