//#condition ONLINE

package net.intensicode.core;

public interface AchievementCallback
    {
    void onAchievementUnlocked( String aAchievementId, final String aDescription, boolean aNewUnlock );

    void onAchievementUnlockFailed( String aAchievementId, Throwable aThrowable );
    }
