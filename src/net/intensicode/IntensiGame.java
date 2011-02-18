package net.intensicode;

import net.intensicode.core.GameSystem;
import net.intensicode.screens.ScreenBase;

public abstract class IntensiGame implements SystemContext
    {
    protected IntensiGame()
        {
        }

    // From SystemContext

    public void trackPageView( final String aPageId )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void trackState( final String aNewState )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public int getBannerAdHeight()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public boolean hasBannerAds()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public boolean hasFullscreenAds()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void hideBannerAd()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void positionAdBanner( final int aVerticalPosition )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void preloadFullscreenAd()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void showBannerAd()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void triggerNewBannerAd()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public void triggerNewFullscreenAd()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    public String determineResourcesFolder( final int aWidth, final int aHeight, final String aScreenOrientationId )
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

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

    //#if ORIENTATION_DYNAMIC

    public void onOrientationChanged()
        {
        throw new RuntimeException( PLACEHOLDER_MESSAGE );
        }

    //#endif

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
