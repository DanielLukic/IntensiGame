package net.intensicode;

import net.intensicode.core.*;

public abstract class IntensiGame implements SystemContext
    {
    protected IntensiGame()
        {
        }

    // From SystemContext

    public GameSystem getGameSystem()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onFramesDropped( final GameSystem aGameSystem )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onApplicationShouldPause( final GameSystem aGameSystem )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public final void terminateApplication()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    private static final String PLACEHOLDER_MESSAGE = "this class is just a placeholder - use IntensiME or IntensiDroid";
    }
