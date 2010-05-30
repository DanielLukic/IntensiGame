//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.core.Configuration;
import net.intensicode.util.Size;

public final class TouchControlsConfiguration extends TouchConfiguration
    {
    public final void setTo( final TouchConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;
        final TouchControlsConfiguration newControlsConfiguration = (TouchControlsConfiguration) aNewConfiguration;
        deadZoneSize.setTo( newControlsConfiguration.deadZoneSize );
        dragStepSize.setTo( newControlsConfiguration.dragStepSize );
        gestureDirectionIgnoreFactor = newControlsConfiguration.gestureDirectionIgnoreFactor;
        gestureMinThresholdInMillis = newControlsConfiguration.gestureMinThresholdInMillis;
        gestureMaxThresholdInMillis = newControlsConfiguration.gestureMaxThresholdInMillis;
        dragThresholdInMillis = newControlsConfiguration.dragThresholdInMillis;
        dragThresholdInPixels.setTo( newControlsConfiguration.dragThresholdInPixels );
        speedLowBoundary = newControlsConfiguration.speedLowBoundary;
        speedHighBoundary = newControlsConfiguration.speedHighBoundary;
        swipeStartsAction = newControlsConfiguration.swipeStartsAction;
        }

    public final void initFromProperties( final Configuration aProperties, final String aPresetName )
        {
        final String prefix = "gestures." + aPresetName;
        label = aProperties.readString( prefix, "label", aPresetName );

        deadZoneSize.width = aProperties.readInt( prefix, "deadZoneSize.width", 13 );
        deadZoneSize.height = aProperties.readInt( prefix, "deadZoneSize.height", 13 );
        dragStepSize.width = aProperties.readInt( prefix, "dragStepSize.width", 18 );
        dragStepSize.height = aProperties.readInt( prefix, "dragStepSize.height", 18 );
        gestureDirectionIgnoreFactor = aProperties.readFloat( prefix, "gestureDirectionIgnoreFactor", 1.8f );
        gestureMinThresholdInMillis = aProperties.readInt( prefix, "gestureMinThresholdInMillis", 25 );
        gestureMaxThresholdInMillis = aProperties.readInt( prefix, "gestureMaxThresholdInMillis", 333 );
        dragThresholdInMillis = aProperties.readInt( prefix, "dragThresholdInMillis", 100 );
        dragThresholdInPixels.width = aProperties.readInt( prefix, "dragThresholdInPixels.width", 75 );
        dragThresholdInPixels.height = aProperties.readInt( prefix, "dragThresholdInPixels.height", 75 );
        speedLowBoundary = aProperties.readFloat( prefix, "speedLowBoundary", 0.3f );
        speedHighBoundary = aProperties.readFloat( prefix, "speedHighBoundary", 1.7f );
        swipeStartsAction = aProperties.readBoolean( prefix, "swipeStartsAction", true );
        }

    public final void initDefaults()
        {
        presets = new TouchControlsConfiguration[1];
        presets[ 0 ] = createDefaultConfiguration();
        }

    public static TouchControlsConfiguration createDefaultConfiguration()
        {
        final TouchControlsConfiguration configuration = new TouchControlsConfiguration();
        configuration.label = "DEFAULT";
        configuration.deadZoneSize.setTo( 13, 13 );
        configuration.dragStepSize.setTo( 18, 18 );
        configuration.gestureDirectionIgnoreFactor = 1.8f;
        configuration.gestureMinThresholdInMillis = 25;
        configuration.gestureMaxThresholdInMillis = 333;
        configuration.dragThresholdInMillis = 100;
        configuration.dragThresholdInPixels.setTo( 75, 75 );
        configuration.speedLowBoundary = 0.3f;
        configuration.speedHighBoundary = 1.7f;
        configuration.swipeStartsAction = true;
        return configuration;
        }


    public final Size deadZoneSize = new Size();

    public final Size dragStepSize = new Size();

    public float gestureDirectionIgnoreFactor;

    public int gestureMinThresholdInMillis;

    public int gestureMaxThresholdInMillis;

    public int dragThresholdInMillis;

    public final Size dragThresholdInPixels = new Size();

    public float speedLowBoundary;

    public float speedHighBoundary;

    public boolean swipeStartsAction;
    }
