//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.Log;
import net.intensicode.core.DirectScreen;

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

        devicePixelFactorX = targetSmoothing / xSmoothing;
        devicePixelFactorY = targetSmoothing / ySmoothing;

        devicePixelFactor = ( devicePixelFactorX + devicePixelFactorY ) / 2;

        final float xFactor = devicePixelFactorX * xSmoothing;
        final float yFactor = devicePixelFactorY * ySmoothing;
        Log.info( "device pixel scaling factor: {}/{}", xFactor, yFactor );
        }
    }
