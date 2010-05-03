package net.intensicode;

import net.intensicode.core.GameSystem;

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

    //#if FEEDBACK

    public void fillEmailData( final EmailData aEmailData )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    //#endif

    public ConfigurationElementsTree getPlatformValues()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public ConfigurationElementsTree getSystemValues()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public ConfigurationElementsTree getApplicationValues()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void loadConfigurableValues()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void saveConfigurableValues()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onFramesDropped()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void onInfoTriggered()
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

    public void triggerConfigurationMenu()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void terminateApplication()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    private static final String PLACEHOLDER_MESSAGE = "this class is just a placeholder - use IntensiME or IntensiDroid";
    }
