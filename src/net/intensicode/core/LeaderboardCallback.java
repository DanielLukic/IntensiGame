package net.intensicode.core;

public interface LeaderboardCallback
    {
    void onScoresUpdate( LeaderboardEntry[] aScoresList );

    void onScoresUpdateFailed( Throwable t );
    }
