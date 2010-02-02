package net.intensicode.core;

import net.intensicode.screens.ScreenBase;

public interface SystemContext
    {
    ScreenBase createMainScreen( GameSystem aGameSystem ) throws Exception;

    void onApplicationShouldPause( GameSystem aGameSystem );

    void onFramesDropped( GameSystem aGameSystem );

    void terminateApplication();
    }
