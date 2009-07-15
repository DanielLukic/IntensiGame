package net.intensicode.net;

import java.io.IOException;
import java.util.Hashtable;

public class TransportException extends IOException
    {
    public final Request associatedRequest;

    public final Response response;

    public final Throwable cause;

    public Object associatedObject;

    public TransportException( final Response aResponse )
        {
        associatedRequest = null;
        response = aResponse;
        cause = null;
        }

    public TransportException( final Request aRequest, final Response aResponse )
        {
        associatedRequest = aRequest;
        response = aResponse;
        cause = null;
        }

    public TransportException( final Throwable aCause )
        {
        associatedRequest = null;
        response = null;
        cause = determineRealCause( aCause );
        }

    public Hashtable createErrorContext( final String aContextId )
        {
        final Hashtable context = new Hashtable();
        context.put( "where", aContextId );
        context.put( "message", getMessage() );
        if ( associatedRequest != null ) associatedRequest.addToContext( context );
        if ( response != null ) response.addToContext( context );
        if ( cause != null ) context.put( "cause", String.valueOf( cause ) );
        if ( isSecurityRelated() ) context.put( "security_related", "true" );
        if ( isTimeOutRelated() ) context.put( "timeout_related", "true" );
        return context;
        }

    public final boolean isSecurityRelated()
        {
        return cause != null && cause instanceof SecurityException;
        }

    public boolean isTimeOutRelated()
        {
        if ( response == null ) return false;
        final String body = response.getBody();
        if ( body == null ) return false;
        if ( body.indexOf( "TIMED_OUT" ) == -1 ) return false;
        return true;
        }

    // From Throwable

    public String getMessage()
        {
        if ( response != null ) return response.toErrorString();
        if ( cause != null ) return cause.getMessage();
        return "no message";
        }

    // Implementation

    private Throwable determineRealCause( final Throwable aCause )
        {
        if ( !( aCause instanceof TransportException ) ) return aCause;

        final TransportException wrapper = (TransportException) aCause;
        if ( wrapper.cause == null ) return aCause;

        return wrapper.cause;
        }
    }
