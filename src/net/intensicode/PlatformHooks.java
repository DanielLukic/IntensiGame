package net.intensicode;

public interface PlatformHooks
    {
    void checkForUpdate( String aUpdateUrl, int aVersionNumber, UpdateCallback aCallback );

    void trackState( String aCategory, String aAction, String aLabel );

    void trackPageView( String aPageId );

    void trackException( String aErrorId, String aMessage, Throwable aOptionalThrowable );

    void showBannerAd();

    void hideBannerAd();

    boolean hasBannerAds();

    int getBannerAdHeight();

    void positionAdBanner( int aVerticalPosition );

    void triggerNewBannerAd();

    boolean hasFullscreenAds();

    void preloadFullscreenAd();

    void triggerNewFullscreenAd();
    }
