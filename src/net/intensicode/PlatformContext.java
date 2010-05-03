package net.intensicode;

public interface PlatformContext
    {
    long compatibleTimeInMillis();

    void openWebBrowser( String aURL );

    void sendEmail( EmailData aEmailData );

    String getPlatformSpecString();

    String getGraphicsSpecString();

    String getExtendedExceptionData( Throwable aException );
    }
