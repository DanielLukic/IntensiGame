//#condition ONLINE

package net.intensicode.core;

public interface ScoreSubmissionCallback
    {
    void onScoreSubmitted( int aScore, int aLevel, String aName, boolean aIsNewHighscore );

    void onScoreSubmissionFailed( Throwable t );
    }
