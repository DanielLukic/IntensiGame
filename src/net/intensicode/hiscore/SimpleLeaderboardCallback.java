package net.intensicode.hiscore;

public interface SimpleLeaderboardCallback
    {
    void onScores( Score[] aBytes );

    void onError( Throwable aThrowable );
    }
