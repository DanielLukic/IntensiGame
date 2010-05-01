//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.Log;
import net.intensicode.core.*;

import java.io.IOException;

public final class TouchConfigurationHelper
    {
    public TouchConfigurationHelper( final ResourcesManager aResourcesManager, final TouchSliderConfiguration aSliderConfiguration, final TouchGesturesConfiguration aGesturesConfiguration, final TouchControlsConfiguration aControlsConfiguration )
        {
        myResourcesManager = aResourcesManager;
        mySliderConfiguration = aSliderConfiguration;
        myGesturesConfiguration = aGesturesConfiguration;
        myControlsConfiguration = aControlsConfiguration;
        }

    public final void tryLoadingTouchConfiguration()
        {
        try
            {
            myProperties = myResourcesManager.loadConfiguration( "touch.properties" );
            myTargetSmoothing = myProperties.readFloat( "target.smoothing", TouchConfiguration.DEFAULT_TARGET_SMOOTHING );

            tryLoadingSliderConfiguration();
            tryLoadingGesturesConfiguration();
            tryLoadingControlsConfiguration();
            }
        catch ( final IOException e )
            {
            Log.error( "failed loading touch.properties. falling back to defaults.", e );
            mySliderConfiguration.initDefaults();
            myGesturesConfiguration.initDefaults();
            myControlsConfiguration.initDefaults();
            }
        }

    private void tryLoadingSliderConfiguration()
        {
        try
            {
            mySliderConfiguration.presets = loadSliderConfigurations();
            mySliderConfiguration.targetSmoothing = myTargetSmoothing;
            }
        catch ( final Exception e )
            {
            Log.error( "failed loading slider configuration. falling back to defaults.", e );
            mySliderConfiguration.initDefaults();
            }
        }

    private void tryLoadingGesturesConfiguration()
        {
        try
            {
            myGesturesConfiguration.presets = loadGesturesConfigurations();
            myGesturesConfiguration.targetSmoothing = myTargetSmoothing;
            }
        catch ( final Exception e )
            {
            Log.error( "failed loading gestures configuration. falling back to defaults.", e );
            myGesturesConfiguration.initDefaults();
            }
        }

    private void tryLoadingControlsConfiguration()
        {
        try
            {
            myControlsConfiguration.presets = loadControlsConfigurations();
            myControlsConfiguration.targetSmoothing = myTargetSmoothing;
            }
        catch ( final Exception e )
            {
            Log.error( "failed loading touch.properties. falling back to defaults.", e );
            myControlsConfiguration.initDefaults();
            }
        }

    private TouchSliderConfiguration[] loadSliderConfigurations()
        {
        final String[] presetNames = myProperties.readList( "slider.presets", null, "," );
        Log.debug( "loading {} slider presets", presetNames.length );
        final TouchSliderConfiguration[] presets = new TouchSliderConfiguration[presetNames.length];
        for ( int idx = 0; idx < presetNames.length; idx++ )
            {
            final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
            configuration.initFromProperties( myProperties, presetNames[ idx ] );
            presets[ idx ] = configuration;
            }
        return presets;
        }

    private TouchGesturesConfiguration[] loadGesturesConfigurations()
        {
        final String[] presetNames = myProperties.readList( "gestures.presets", null, "," );
        Log.debug( "loading {} gestures presets", presetNames.length );
        final TouchGesturesConfiguration[] presets = new TouchGesturesConfiguration[presetNames.length];
        for ( int idx = 0; idx < presetNames.length; idx++ )
            {
            final TouchGesturesConfiguration configuration = new TouchGesturesConfiguration();
            configuration.initFromProperties( myProperties, presetNames[ idx ] );
            presets[ idx ] = configuration;
            }
        return presets;
        }

    private TouchControlsConfiguration[] loadControlsConfigurations()
        {
        final String[] presetNames = myProperties.readList( "controls.presets", null, "," );
        Log.debug( "loading {} controls presets", presetNames.length );
        final TouchControlsConfiguration[] presets = new TouchControlsConfiguration[presetNames.length];
        for ( int idx = 0; idx < presetNames.length; idx++ )
            {
            final TouchControlsConfiguration configuration = new TouchControlsConfiguration();
            configuration.initFromProperties( myProperties, presetNames[ idx ] );
            presets[ idx ] = configuration;
            }
        return presets;
        }


    private float myTargetSmoothing;

    private Configuration myProperties;

    private final ResourcesManager myResourcesManager;

    private final TouchSliderConfiguration mySliderConfiguration;

    private final TouchGesturesConfiguration myGesturesConfiguration;

    private final TouchControlsConfiguration myControlsConfiguration;
    }
