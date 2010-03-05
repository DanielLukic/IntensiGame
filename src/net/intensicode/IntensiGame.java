package net.intensicode;

import net.intensicode.core.*;

public abstract class IntensiGame implements SystemContext
    {
    protected IntensiGame()
        {
        }

    // From SystemContext

    public GameSystem system()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public boolean useOpenglIfPossible()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onFramesDropped()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onDebugTriggered()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onCheatTriggered()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onPauseApplication()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onDestroyApplication()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void terminateApplication()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    private static final String PLACEHOLDER_MESSAGE = "this class is just a placeholder - use IntensiME or IntensiDroid";
    }
