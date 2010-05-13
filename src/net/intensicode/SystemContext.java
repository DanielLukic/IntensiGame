package net.intensicode;

import net.intensicode.core.GameSystem;
import net.intensicode.screens.ScreenBase;

public interface SystemContext
    {
    /**
     * Implement your logic for choosing a resources sub folder here or do not override this method to use the default
     * behavior of reading a resources.properties file and letting IntensiGame choose the best resolution.
     *
     * Note: The chosen folder should have the p and l subfolders. It should not be one of them. But it may be.. :)
     *
     * Note: You may also return null here to tell the system to not use a subfolder at all.
     *
     * Note: In any case, the system will still look for p and l subfolders within whatever folder you may return here.
     * When you return null here, the system will look for p and l in the root folder.
     */
    String determineResourcesFolder( final int aWidth, final int aHeight, final String aScreenOrientationId );

    /**
     * Helper method to make the game system available to everyone who has access to the system context object.
     */
    GameSystem system();

    //#if FEEDBACK

    /**
     * Fill the given EmailData object's to and subject fields. This is used when the user chooses to send feedback or
     * a bug report.
     */
    void fillEmailData( EmailData aEmailData );

    //#endif

    /**
     * You have to return your main screen or main controller object in here. The game system will call this method
     * after it has setup everything.
     */
    ScreenBase createMainScreen() throws Exception;

    /**
     * Used internally to access the platform-specific configurable values.
     */
    ConfigurationElementsTree getPlatformValues();

    /**
     * Used internally to access the game system configurable values.
     */
    ConfigurationElementsTree getSystemValues();

    /**
     * Here you should provide your application-specific configurable values.
     */
    ConfigurationElementsTree getApplicationValues();

    void loadConfigurableValues();

    void saveConfigurableValues();

    /**
     * This will be called whenever frames have been dropped. Possible causes: Your frame drawing or your
     * control ticks take too long. Or the system is overloaded.
     */
    void onFramesDropped();

    /**
     * Called when ControlSequenceHandler detects info sequence.
     */
    void onInfoTriggered();

    /**
     * Called when ControlSequenceHandler detects debug sequence.
     */
    void onDebugTriggered();

    /**
     * Called when ControlSequenceHandler detects cheat sequence.
     */
    void onCheatTriggered();

    /**
     * Called when game system detects application being paused or sent to background. You should do whatever is
     * necessary to pause your game logic. Could be as simple as saying gameModel.gameState = PAUSED.
     */
    void onPauseApplication();

    /**
     * Called before the application is destroyed.
     */
    void onDestroyApplication();

    //#if ORIENTATION_DYNAMIC

    /**
     * Called when screen orientation changes and dynamic screen orientation mode is activated for the build. When this
     * is called, the screen orientation has already changed. You should call ScreenStack#onOrientationChanged to pass
     * this info through to all stacked screens. (Unless you handle orientation changes differently in your screens.)
     * You will probably also want to call TouchHandler#removeAllTouchables to avoid false touch areas.
     */
    void onOrientationChanged();

    //#endif

    /**
     * Call this to trigger the game system configuration menu. This will include all configurable values provided by
     * getPlatformValues, getSystemValues and getApplicationValues.
     */
    void triggerConfigurationMenu();

    /**
     * This should terminate the application asap. You should not call this directly.
     * Use GameSystem#shutdownAndExit instead.
     */
    void terminateApplication();
    }
