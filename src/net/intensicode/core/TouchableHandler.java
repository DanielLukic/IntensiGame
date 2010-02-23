package net.intensicode.core;

public interface TouchableHandler
    {
    //#ifdef TOUCH
    void onPressed( Object aTouchable );

    void onReleased( Object aTouchable );
    //#endif
    }
