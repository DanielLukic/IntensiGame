//#condition ONLINE

package net.intensicode.core;

public interface AchievementIconCallback
    {
    void onAchievementIcon( String aAchievementId, final ImageResource aImageResource );

    void onAchievementIconFailed( String aAchievementId, Throwable aThrowable );
    }
