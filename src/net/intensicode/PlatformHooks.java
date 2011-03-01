package net.intensicode;

public interface PlatformHooks
    {
    void trackState( String aCategory, String aAction, String aLabel );

    void trackPageView( String aPageId );

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
