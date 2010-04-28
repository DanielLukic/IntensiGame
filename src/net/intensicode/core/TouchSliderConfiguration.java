//#condition TOUCH

package net.intensicode.core;

public final class TouchSliderConfiguration extends TouchConfiguration
    {
    public TouchSliderConfiguration[] presets;


    public final String[] getLabels()
        {
        return super.getLabels( presets );
        }

    public final void setTo( final TouchSliderConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;
        slideStartThresholdInMillis = aNewConfiguration.slideStartThresholdInMillis;
        slideStartThresholdInPixels = aNewConfiguration.slideStartThresholdInPixels;
        slideMoveThresholdInPixels = aNewConfiguration.slideMoveThresholdInPixels;
        newSlideStartThresholdInMillis = aNewConfiguration.newSlideStartThresholdInMillis;
        initialStepThresholdInPixels = aNewConfiguration.initialStepThresholdInPixels;
        additionalStepThresholdInPixels = aNewConfiguration.additionalStepThresholdInPixels;

        slideStartThresholdInMillis *= deviceTimingFactor;
        newSlideStartThresholdInMillis *= deviceTimingFactor;
        slideStartThresholdInPixels *= devicePixelFactor;
        slideMoveThresholdInPixels *= devicePixelFactor;
        initialStepThresholdInPixels *= devicePixelFactor;
        additionalStepThresholdInPixels *= devicePixelFactor;
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
        presets = new TouchSliderConfiguration[5];

        presets[ 0 ] = new TouchSliderConfiguration();
        presets[ 0 ].slideStartThresholdInMillis = 40;
        presets[ 0 ].slideStartThresholdInPixels = 25;
        presets[ 0 ].slideMoveThresholdInPixels = 20;
        presets[ 0 ].newSlideStartThresholdInMillis = 50;
        presets[ 0 ].initialStepThresholdInPixels = 25;
        presets[ 0 ].additionalStepThresholdInPixels = 120;

        presets[ 1 ] = new TouchSliderConfiguration();
        presets[ 1 ].slideStartThresholdInMillis = 30;
        presets[ 1 ].slideStartThresholdInPixels = 25;
        presets[ 1 ].slideMoveThresholdInPixels = 15;
        presets[ 1 ].newSlideStartThresholdInMillis = 45;
        presets[ 1 ].initialStepThresholdInPixels = 20;
        presets[ 1 ].additionalStepThresholdInPixels = 100;

        presets[ 2 ] = new TouchSliderConfiguration();
        presets[ 2 ].slideStartThresholdInMillis = 25;
        presets[ 2 ].slideStartThresholdInPixels = 20;
        presets[ 2 ].slideMoveThresholdInPixels = 10;
        presets[ 2 ].newSlideStartThresholdInMillis = 40;
        presets[ 2 ].initialStepThresholdInPixels = 20;
        presets[ 2 ].additionalStepThresholdInPixels = 100;

        presets[ 3 ] = new TouchSliderConfiguration();
        presets[ 3 ].slideStartThresholdInMillis = 25;
        presets[ 3 ].slideStartThresholdInPixels = 15;
        presets[ 3 ].slideMoveThresholdInPixels = 10;
        presets[ 3 ].newSlideStartThresholdInMillis = 35;
        presets[ 3 ].initialStepThresholdInPixels = 20;
        presets[ 3 ].additionalStepThresholdInPixels = 60;

        presets[ 4 ] = new TouchSliderConfiguration();
        presets[ 4 ].slideStartThresholdInMillis = 25;
        presets[ 4 ].slideStartThresholdInPixels = 10;
        presets[ 4 ].slideMoveThresholdInPixels = 8;
        presets[ 4 ].newSlideStartThresholdInMillis = 30;
        presets[ 4 ].initialStepThresholdInPixels = 15;
        presets[ 4 ].additionalStepThresholdInPixels = 40;
        }


    public int slideStartThresholdInMillis;

    public int slideStartThresholdInPixels;

    public int slideMoveThresholdInPixels;

    public int newSlideStartThresholdInMillis;

    public int initialStepThresholdInPixels;

    public int additionalStepThresholdInPixels;
    }
