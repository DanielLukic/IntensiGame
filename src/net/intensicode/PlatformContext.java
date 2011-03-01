package net.intensicode;

public interface PlatformContext
    {
    public static final String SCREEN_ORIENTATION_PORTRAIT = "p";

    public static final String SCREEN_ORIENTATION_LANDSCAPE = "l";

    public static final String SCREEN_ORIENTATION_SQUARE = "s";

    long compatibleTimeInMillis();

    void openWebBrowser( String aURL );

    void sendEmail( EmailData aEmailData );

    String screenOrientationId();

    String getPlatformSpecString();

    String getGraphicsSpecString();

    String getExtendedExceptionData( Throwable aException );

    void showError( String aMessage, Throwable aOptionalThrowable );

    void showCriticalError( String aMessage, Throwable aOptionalThrowable );

    void storePreferences( String aPreferencesId, String aPropertyKey, boolean aValue );

    void register( String aComponentName, String aClassName );

    Object component( String aComponentName );

    PlatformHooks hooks();
    }
