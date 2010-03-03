//#condition SENSORS

package net.intensicode.core;

public abstract class SensorsManager
    {
    public final Orientation orientation = new Orientation();

    public final Acceleration acceleration = new Acceleration();


    public abstract boolean hasOrientation();

    public abstract boolean hasAcceleration();

    public abstract void onControlTick();
    }
