package net.intensicode.core;

public interface NetworkCallback
    {
    void onReceived( byte[] aBytes );

    void onError( Throwable aThrowable );
    }
