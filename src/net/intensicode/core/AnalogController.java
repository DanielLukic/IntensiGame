package net.intensicode.core;

public abstract class AnalogController
    {
    public static final int DEFAULT_SILENCE_IN_MILLIS = 1000/32;

    public static final int DEFAULT_SILENCE_TIMEOUT_IN_MILLIS = 1000/10;

    public static final int DEFAULT_IGNORE_FACTOR = 1;

    public int silenceBeforeUpdateInMillis = DEFAULT_SILENCE_IN_MILLIS;

    public int multiEventThresholdInMillis = DEFAULT_SILENCE_TIMEOUT_IN_MILLIS;

    public int directionIgnoreFactor = DEFAULT_IGNORE_FACTOR;

    public boolean autoClear;

    public int leftDelta;

    public int rightDelta;

    public int upDelta;

    public int downDelta;

    public int leftMax;

    public int rightMax;

    public int upMax;

    public int downMax;

    public int accumulationTicks;


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
        return Math.abs( getDeltaY() ) > Math.abs( getDeltaX() ) * directionIgnoreFactor;
        }

    public final boolean shouldIgnoreDeltaY()
        {
        return Math.abs( getDeltaX() ) > Math.abs( getDeltaY() ) * directionIgnoreFactor;
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
