//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.Log;

import java.io.IOException;

public final class TouchConfigurationHelper
    {
    public TouchConfigurationHelper( final ResourcesManager aResourcesManager, final TouchSliderConfiguration aSliderConfiguration, final TouchGesturesConfiguration aGesturesConfiguration )
        {
        myResourcesManager = aResourcesManager;
        mySliderConfiguration = aSliderConfiguration;
        myGesturesConfiguration = aGesturesConfiguration;
        }

    public final void tryLoadingTouchConfiguration()
        {
        try
            {
            final Configuration properties = myResourcesManager.loadConfiguration( "touch.properties" );
            final float targetSmoothing = properties.readFloat( "target.smoothing", TouchConfiguration.DEFAULT_TARGET_SMOOTHING );

            final TouchSliderConfiguration[] sliderPresets = loadSliderConfigurations( properties );
            final TouchGesturesConfiguration[] gesturesPresets = loadGesturesConfigurations( properties );

            mySliderConfiguration.targetSmoothing = targetSmoothing;
            mySliderConfiguration.presets = sliderPresets;

            myGesturesConfiguration.targetSmoothing = targetSmoothing;
            myGesturesConfiguration.presets = gesturesPresets;
            }
        catch ( final IOException e )
            {
            Log.error( "failed loading touch properties. falling back to defaults.", e );
            mySliderConfiguration.initDefaults();
            myGesturesConfiguration.initDefaults();
            }
        }

    private TouchSliderConfiguration[] loadSliderConfigurations( final Configuration aProperties )
        {
        final String[] presetNames = aProperties.readList( "slider.presets", null, "," );
        Log.debug( "loading {} slider presets", presetNames.length );
        final TouchSliderConfiguration[] presets = new TouchSliderConfiguration[presetNames.length];
        for ( int idx = 0; idx < presetNames.length; idx++ )
            {
            final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
            configuration.initFromProperties( aProperties, presetNames[ idx ] );
            presets[ idx ] = configuration;
            }
        return presets;
        }

    private TouchGesturesConfiguration[] loadGesturesConfigurations( final Configuration aProperties )
        {
        final String[] presetNames = aProperties.readList( "gestures.presets", null, "," );
        Log.debug( "loading {} gestures presets", presetNames.length );
        final TouchGesturesConfiguration[] presets = new TouchGesturesConfiguration[presetNames.length];
        for ( int idx = 0; idx < presetNames.length; idx++ )
            {
            final TouchGesturesConfiguration configuration = new TouchGesturesConfiguration();
            configuration.initFromProperties( aProperties, presetNames[ idx ] );
            presets[ idx ] = configuration;
            }
        return presets;
        }


    private final ResourcesManager myResourcesManager;

    private final TouchSliderConfiguration mySliderConfiguration;

    private final TouchGesturesConfiguration myGesturesConfiguration;
    }
