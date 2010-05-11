package net.intensicode;

import net.intensicode.screens.ScreenBase;
import net.intensicode.core.GameSystem;

public interface SystemContext
    {
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

    void onOrientationChanged();

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
