package net.intensicode.core;

public interface TouchableHandler
    {
    //#ifdef TOUCH_SUPPORTED
    void onPressed( Object aTouchable );

    void onReleased( Object aTouchable );
    //#endif
    }
