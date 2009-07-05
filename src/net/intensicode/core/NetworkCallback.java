package net.intensicode.core;

/**
 * TODO: Describe this!
 */
public interface NetworkCallback
    {
    void onReceived( byte[] aBytes );

    void onError( Throwable aThrowable );
    }
