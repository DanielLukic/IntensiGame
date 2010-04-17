//#condition TRACKBALL

package net.intensicode.core;

public abstract class TrackballController
    {
    public int forcedSilenceBetweenEventsInMillis = 0;

    public int silenceBeforeUpdateInMillis = 50;

    public int multiEventThresholdInMillis = 250;

    public float directionIgnoreFactor = 1f;

    public int initialTicksThreshold = 0;

    public int multiTicksThreshold = 3;

    public int additionalMultiTicksThreshold = 1;

    public boolean autoClear;

    public int leftDelta;

    public int rightDelta;

    public int upDelta;

    public int downDelta;

    public int leftMax;

    public int rightMax;

    public int upMax;

    public int downMax;

    public int leftRaw;

    public int rightRaw;

    public int upRaw;

    public int downRaw;

    public int accumulationTicks;


    public final boolean hasNonZeroData()
        {
        return ( leftDelta > 0 || rightDelta > 0 || upDelta > 0 || downDelta > 0 );
        }

    public final int getDeltaX()
        {
        return rightDelta - leftDelta;
        }

    public final int getDeltaY()
        {
        return downDelta - upDelta;
        }

    public final int getMaxInAnyDirection()
        {
        return Math.max( leftMax, Math.max( rightMax, Math.max( upMax, downMax ) ) );
        }

    public final boolean shouldIgnoreDeltaX()
        {
        final float scaledDeltaX = Math.abs( getDeltaX() ) * directionIgnoreFactor;
        return Math.abs( getDeltaY() ) > scaledDeltaX;
        }

    public final boolean shouldIgnoreDeltaY()
        {
        final float scaledDeltaY = Math.abs( getDeltaY() ) * directionIgnoreFactor;
        return Math.abs( getDeltaX() ) > scaledDeltaY;
        }

    public void reset()
        {
        clearDeltaValues();
        }

    public final void clearDeltaValues()
        {
        leftDelta = rightDelta = upDelta = downDelta = 0;
        leftMax = rightMax = upMax = downMax = 0;
        accumulationTicks = 0;
        }

    public final void onControlTick()
        {
        if ( hasNewData() ) updateDeltaValues();
        else if ( autoClear ) clearDeltaValues();
        }

    // Protected Abstract API

    protected abstract boolean hasNewData();

    protected abstract void updateDeltaValues();
    }
