//#condition ONLINE

package net.intensicode.core;

public interface ScoreSubmissionCallback
    {
    void onScoreSubmitted();

    void onScoreSubmissionFailed( Throwable t );
    }
