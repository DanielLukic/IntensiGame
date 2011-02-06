package net.intensicode.hiscore;

public interface SimpleLeaderboardCallback
    {
    void onScores( byte[] aBytes );

    void onError( Throwable aThrowable );
    }
