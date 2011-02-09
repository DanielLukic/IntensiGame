//#condition ONLINE

package net.intensicode.core;

public interface ScoreSubmissionCallback
    {
    void onScoreSubmitted( boolean aIsNewHighscore );

    void onScoreSubmissionFailed( Throwable t );
    }
