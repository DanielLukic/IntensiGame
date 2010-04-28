//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.Log;

public abstract class TouchConfiguration
    {
    public static final float DEFAULT_TARGET_SMOOTHING = 0.6f;

    public float targetSmoothing = DEFAULT_TARGET_SMOOTHING;

    public float deviceTimingFactor = 1f;

    public float devicePixelFactorX = 1f;

    public float devicePixelFactorY = 1f;

    public float devicePixelFactor = 1f;

    public String label;


    public final String[] getLabels( final TouchConfiguration[] aConfigurations )
        {
        final String[] labels = new String[aConfigurations.length];
        for ( int idx = 0; idx < aConfigurations.length; idx++ )
            {
            labels[ idx ] = aConfigurations[ idx ].label;
            }
        return labels;
        }

    public void setDeviceParameters( final DirectScreen aScreen )
        {
        devicePixelFactorX = 1f;
        devicePixelFactorY = 1f;

        final float xSmoothing = aScreen.getTargetWidth() / (float) aScreen.getNativeWidth();
        final float ySmoothing = aScreen.getTargetHeight() / (float) aScreen.getNativeHeight();
        Log.info( "implicit native -> target factor: {}/{}", xSmoothing, ySmoothing );

        devicePixelFactorX = targetSmoothing / xSmoothing;
        devicePixelFactorY = targetSmoothing / ySmoothing;
        Log.info( "device pixel factor: {}/{}", devicePixelFactorX, devicePixelFactorY );

        devicePixelFactor = ( devicePixelFactorX + devicePixelFactorY ) / 2;
        Log.info( "device pixel factor (avg): {}", devicePixelFactor );

        final float xFactor = devicePixelFactorX * xSmoothing;
        final float yFactor = devicePixelFactorY * ySmoothing;
        Log.info( "resulting factor: {}/{}", xFactor, yFactor );
        }
    }
