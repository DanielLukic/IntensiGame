package net.intensicode.net.connection;

import net.intensicode.net.Response;
import net.intensicode.net.Request;

import java.io.IOException;

public interface TransportConnection
    {
    boolean hasBeenInterrupted();

    void prepare( Request aRequest ) throws IOException;

    Response process( Request aRequest ) throws IOException, InterruptedException;

    void cancel();

    void close();
    }
