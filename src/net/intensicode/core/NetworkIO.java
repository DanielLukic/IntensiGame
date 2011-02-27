package net.intensicode.core;

public interface NetworkIO
    {
    boolean isOnline();

    void sendAndReceive( String aURL, byte[] aBody, NetworkCallback aCallback );

    void process( NetworkRequest aRequest, NetworkCallback aNetworkCallback );
    }
