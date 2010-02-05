package net.intensicode.core;

import net.intensicode.screens.ScreenBase;

public interface SystemContext
    {
    GameSystem getGameSystem();

    ScreenBase createMainScreen( GameSystem aGameSystem ) throws Exception;

    void onApplicationShouldPause( GameSystem aGameSystem );

    /**
     * This will be called whenever frames have been dropped. Possible causes: Your frame drawing or your
     * control ticks take too long. Or the system is overloaded.
     */
    void onFramesDropped( GameSystem aGameSystem );

    /**
     * This should terminate the application asap. You should not call this directly.
     * Use GameSystem#shutdownAndExit instead.
     */
    void terminateApplication();
    }
