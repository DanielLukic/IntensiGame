package net.intensicode;

public interface PlatformContext
    {
    long compatibleTimeInMillis();

    void openWebBrowser( String aURL );
    }
