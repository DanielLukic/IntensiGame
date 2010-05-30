//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.core.Configuration;

public final class TouchSliderConfiguration extends TouchConfiguration
    {
    public final void setTo( final TouchConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;
        final TouchSliderConfiguration newSliderConfiguration = (TouchSliderConfiguration) aNewConfiguration;
        slideStartThresholdInMillis = newSliderConfiguration.slideStartThresholdInMillis;
        slideStartThresholdInPixels = newSliderConfiguration.slideStartThresholdInPixels;
        slideMoveThresholdInPixels = newSliderConfiguration.slideMoveThresholdInPixels;
        newSlideStartThresholdInMillis = newSliderConfiguration.newSlideStartThresholdInMillis;
        initialStepThresholdInPixels = newSliderConfiguration.initialStepThresholdInPixels;
        additionalStepThresholdInPixels = newSliderConfiguration.additionalStepThresholdInPixels;
        }

    public final void initFromProperties( final Configuration aProperties, final String aPresetName )
        {
        final String prefix = "slider." + aPresetName;
        label = aProperties.readString( prefix, "label", aPresetName );
        slideStartThresholdInMillis = aProperties.readInt( prefix, "slideStartThresholdInMillis", 25 );
        slideStartThresholdInPixels = aProperties.readInt( prefix, "slideStartThresholdInPixels", 20 );
        slideMoveThresholdInPixels = aProperties.readInt( prefix, "slideMoveThresholdInPixels", 10 );
        newSlideStartThresholdInMillis = aProperties.readInt( prefix, "newSlideStartThresholdInMillis", 40 );
        initialStepThresholdInPixels = aProperties.readInt( prefix, "initialStepThresholdInPixels", 20 );
        additionalStepThresholdInPixels = aProperties.readInt( prefix, "additionalStepThresholdInPixels", 100 );
        }

    public final void initDefaults()
        {
        presets = new TouchSliderConfiguration[3];
        presets[ 0 ] = createLowSensitivityConfiguration();
        presets[ 1 ] = createMediumSensitivityConfiguration();
        presets[ 2 ] = createHighSensitivityConfiguration();
        }

    public static TouchSliderConfiguration createLowSensitivityConfiguration()
        {
        final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
        configuration.label = "LOW";
        configuration.slideStartThresholdInMillis = 30;
        configuration.slideStartThresholdInPixels = 25;
        configuration.slideMoveThresholdInPixels = 15;
        configuration.newSlideStartThresholdInMillis = 45;
        configuration.initialStepThresholdInPixels = 20;
        configuration.additionalStepThresholdInPixels = 100;
        return configuration;
        }

    public static TouchSliderConfiguration createMediumSensitivityConfiguration()
        {
        final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
        configuration.label = "MEDIUM";
        configuration.slideStartThresholdInMillis = 25;
        configuration.slideStartThresholdInPixels = 20;
        configuration.slideMoveThresholdInPixels = 10;
        configuration.newSlideStartThresholdInMillis = 40;
        configuration.initialStepThresholdInPixels = 20;
        configuration.additionalStepThresholdInPixels = 100;
        return configuration;
        }

    public static TouchSliderConfiguration createHighSensitivityConfiguration()
        {
        final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
        configuration.label = "HIGH";
        configuration.slideStartThresholdInMillis = 25;
        configuration.slideStartThresholdInPixels = 15;
        configuration.slideMoveThresholdInPixels = 10;
        configuration.newSlideStartThresholdInMillis = 35;
        configuration.initialStepThresholdInPixels = 20;
        configuration.additionalStepThresholdInPixels = 60;
        return configuration;
        }


    //#if J2ME

    public int slideStartThresholdInMillis = 33;

    public int slideStartThresholdInPixels = 10;

    public int slideMoveThresholdInPixels = 10;

    public int newSlideStartThresholdInMillis = 50;

    public int initialStepThresholdInPixels = 10;

    public int additionalStepThresholdInPixels = 30;

    //#else

    //# public int slideStartThresholdInMillis = 25;

    //# public int slideStartThresholdInPixels = 20;

    //# public int slideMoveThresholdInPixels = 10;

    //# public int newSlideStartThresholdInMillis = 40;

    //# public int initialStepThresholdInPixels = 20;

    //# public int additionalStepThresholdInPixels = 100;

    //#endif
    }
