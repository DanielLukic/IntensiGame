package net.intensicode.core;

public abstract class AnalogController
    {
    public static final int DEFAULT_SILENCE_IN_MILLIS = 10;

    public static final int DEFAULT_SILENCE_TIMEOUT_IN_MILLIS = 25;

    public static final int DEFAULT_IGNORE_FACTOR = 2;

    //#if SENSORS

    public boolean useOrientationSensor;

    public boolean useAccelerationSensor;

    //#endif

    public int silenceBeforeUpdateInMillis = DEFAULT_SILENCE_IN_MILLIS;

    public int silenceTimeoutInMillis = DEFAULT_SILENCE_TIMEOUT_IN_MILLIS;

    public int ignoreFactor = DEFAULT_IGNORE_FACTOR;

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
        return Math.abs( getDeltaY() ) > Math.abs( getDeltaX() ) * ignoreFactor;
        }

    public final boolean shouldIgnoreDeltaY()
        {
        return Math.abs( getDeltaX() ) > Math.abs( getDeltaY() ) * ignoreFactor;
        }

    public final void clearDeltaValues()
        {
        leftDelta = rightDelta = upDelta = downDelta = 0;
        leftMax = rightMax = upMax = downMax = 0;
        accumulationTicks = 0;
        }

    public final void onControlTick()
        {
        //#if SENSORS
        if ( useOrientationSensor ) mapOrientationToMovement();
        if ( useAccelerationSensor ) mapAccelerationToMovement();
        //#endif

        if ( hasNewData() ) updateDeltaValues();
        else if ( autoClear ) clearDeltaValues();
        }

    // Protected Abstract API

    //#if SENSORS

    protected abstract void mapOrientationToMovement();

    protected abstract void mapAccelerationToMovement();

    //#endif

    protected abstract boolean hasNewData();

    protected abstract void updateDeltaValues();
    }
