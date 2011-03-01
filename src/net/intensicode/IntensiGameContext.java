package net.intensicode;

import net.intensicode.core.GameSystem;
import net.intensicode.screens.ScreenBase;

public interface IntensiGameContext
    {
    GameSystem system();

    SystemContext context();

    IntensiGameHelper helper();

    ScreenBase createMainScreen() throws Exception;
    }
