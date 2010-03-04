package net.intensicode.core;

public abstract class AnalogController
    {
    //#if SENSORS

    public boolean useOrientationSensor;

    public boolean useAccelerationSensor;

    //#endif

    public int xDeltaFixed;

    public int yDeltaFixed;


    public final void onControlTick()
        {
        //#if SENSORS
        if ( useOrientationSensor ) mapOrientationToMovement();
        if ( useAccelerationSensor ) mapAccelerationToMovement();
        //#endif
        if ( hasNewData() ) updateDeltaValues();
        else clearDeltaValues();
        }

    // Protected Abstract API

    //#if SENSORS

    protected abstract void mapOrientationToMovement();

    protected abstract void mapAccelerationToMovement();

    //#endif

    protected abstract boolean hasNewData();

    protected abstract void updateDeltaValues();

    // Implementation

    private void clearDeltaValues()
        {
        xDeltaFixed = yDeltaFixed = 0;
        }
    }
