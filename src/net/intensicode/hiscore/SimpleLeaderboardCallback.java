package net.intensicode.hiscore;

public interface SimpleLeaderboardCallback
    {
    void onScores( GammetaScore[] aBytes );

    void onError( Throwable aThrowable );
    }
