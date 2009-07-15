package net.intensicode.net.system;

import net.intensicode.net.Request;

public interface TransportSystem
    {
    int PRIORITY_SYSTEM = 0;

    int PRIORITY_UI = 10;

    int PRIORITY_BUDDIES = 15;

    int PRIORITY_TILES = 20;

    int PRIORITY_CATEGORIES = 25;

    int PRIORITY_SPOTS = 30;

    int PRIORITY_BACKGROUND = 50;

    void start();

    void stop();

    void request( Request aRequest, TransportSystemCallback aCallback );

    void request( int aPriority, Request aRequest, TransportSystemCallback aCallback );

    void cancel( Request aRequest );

    void cancelAll();

    void cancelAll( String aRequestId );
    }
