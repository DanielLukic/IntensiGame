//#condition ONLINE

package net.intensicode.core;

public interface OnlineAPI
    {
    boolean hasNetworking();

    boolean isLoggedIn();

    String getUserName();

    void showDashboard();

    void showLeaderboard();

    void retrieveHighscores( LeaderboardCallback aCallback );

    void submitScore( int aScore, int aLevelNumberStartingAt1, ScoreSubmissionCallback aCallback );

    void progressAchievement( String aAchievementId, int aProgressInPercent );

    void unlockAchievement( String aAchievementId, AchievementCallback aCallback );

    void loadAchievementIcon( String aAchievementId, AchievementIconCallback aCallback );
    }
