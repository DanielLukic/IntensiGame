package net.intensicode;

import net.intensicode.core.GameSystem;
import net.intensicode.screens.ScreenBase;

public interface IntensiGameContext
    {
    GameSystem system();

    SystemContext context();

    IntensiGameHelper helper();

    ScreenBase createMainScreen() throws Exception;

    ConfigurationElementsTree getApplicationValues();

    void onFramesDropped();

    void onInfoTriggered();

    void onPauseApplication();

    void onDestroyApplication();

    //#if ORIENTATION_DYNAMIC

    void onOrientationChanged();

    //#endif
    }
