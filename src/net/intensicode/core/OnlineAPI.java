package net.intensicode.core;

public interface OnlineAPI
    {
    boolean hasNetworking();

    boolean isLoggedIn();

    String getUserName();

    void showDashboard();

    void showLeaderboard();

    void submitScore( int aScore, int aLevelNumberStartingAt1 );
    }
