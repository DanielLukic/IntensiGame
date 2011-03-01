package net.intensicode;

import net.intensicode.core.GameSystem;
import net.intensicode.screens.ScreenBase;

public abstract class IntensiGame implements IntensiGameContext
    {
    protected IntensiGame()
        {
        }

    // From IntensiGameContext

    public GameSystem system()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public SystemContext context()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public IntensiGameHelper helper()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public ScreenBase createMainScreen() throws Exception
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    private static final String PLACEHOLDER_MESSAGE = "this class is just a placeholder - use IntensiME or IntensiDroid";
    }
