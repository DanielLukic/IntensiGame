package net.intensicode;

public interface UpdateCallback
    {
    void noUpdateAvailable();

    void updateCheckFailed( Throwable aException );

    void onUpdateAvailable( UpdateContext aUpdateContext, int aUpdateVersion );
    }
