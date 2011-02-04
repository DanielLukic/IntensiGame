package net.intensicode.core;

import java.util.Enumeration;

public interface NetworkRequest
    {
    String METHOD_GET = "GET";
    String METHOD_POST = "POST";

    String url();

    String method();

    Enumeration headers();

    String headerValue( String aHeaderKey );

    byte[] body();
    }
