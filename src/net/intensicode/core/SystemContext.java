package net.intensicode.core;

import net.intensicode.screens.ScreenBase;

public interface SystemContext
    {
    GameSystem getGameSystem();

    ScreenBase createMainScreen( GameSystem aGameSystem ) throws Exception;

    void onApplicationShouldPause( GameSystem aGameSystem );

    void onFramesDropped( GameSystem aGameSystem );

    void terminateApplication();
    }
