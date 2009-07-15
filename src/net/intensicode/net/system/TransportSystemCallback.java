package net.intensicode.net.system;

import net.intensicode.net.Request;
import net.intensicode.net.Response;
import net.intensicode.net.TransportException;

public interface TransportSystemCallback
    {
    int getTransportPriority();

    void requestComplete( Request aRequest, Response aResponse );

    void requestFailed( Request aRequest, TransportException aException );
    }
