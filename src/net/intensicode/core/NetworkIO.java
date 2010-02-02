package net.intensicode.core;

public interface NetworkIO
    {
    void sendAndReceive( String aURL, byte[] aBody, NetworkCallback aCallback );
    }
