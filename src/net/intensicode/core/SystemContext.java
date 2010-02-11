package net.intensicode.core;

import net.intensicode.screens.ScreenBase;

public interface SystemContext
    {
    /**
     * Helper method to make the game system available to everyone who has access to the system context object.
     */
    GameSystem getGameSystem();

    /**
     * Return true here if you want the game system to render everything with OpenGL/EGL (if this is available on the
     * platform and device). Please note that you should do this only if you do not use the SystemFontGenerator at all.
     * Please note that you should use graphics with sizes being powers of two only. Otherwise the game system will
     * have to scale your graphics to make them compatible with OpenGL/EGL.
     */
    boolean useOpenglIfPossible();

    /**
     * You have to return your main screen or main controller object in here. The game system will call this method
     * after it has setup everything.
     */
    ScreenBase createMainScreen( GameSystem aGameSystem ) throws Exception;

    /**
     * The game system will call this method if it has determined that your application should enter a pause mode
     * because of external system events. For example when your application is being pushed to the background. Incoming
     * phone calls can cause this, for example.
     */
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
