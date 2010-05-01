//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.*;
import net.intensicode.core.Configuration;

public final class TouchControlsConfiguration extends TouchConfiguration
    {
    public TouchControlsConfiguration[] presets;


    public final String[] getLabels()
        {
        return super.getLabels( presets );
        }

    public final void setTo( final TouchControlsConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;
        deadZoneSize.setTo( aNewConfiguration.deadZoneSize );
        dragStepSize.setTo( aNewConfiguration.dragStepSize );
        gestureDirectionIgnoreFactor = aNewConfiguration.gestureDirectionIgnoreFactor;
        gestureMinThresholdInMillis = aNewConfiguration.gestureMinThresholdInMillis;
        gestureMaxThresholdInMillis = aNewConfiguration.gestureMaxThresholdInMillis;
        dragThresholdInMillis = aNewConfiguration.dragThresholdInMillis;
        dragThresholdInPixels.setTo( aNewConfiguration.dragThresholdInPixels );
        swipeStartsAction = aNewConfiguration.swipeStartsAction;
        }

    public final void initFromProperties( final Configuration aProperties, final String aPresetName )
        {
        final String prefix = "gestures." + aPresetName;
        label = aProperties.readString( prefix, "label", aPresetName );

        deadZoneSize.width = aProperties.readInt( prefix, "deadZoneSize.width", 9 );
        deadZoneSize.height = aProperties.readInt( prefix, "deadZoneSize.height", 9 );
        dragStepSize.width = aProperties.readInt( prefix, "dragStepSize.width", 18 );
        dragStepSize.height = aProperties.readInt( prefix, "dragStepSize.height", 18 );
        gestureDirectionIgnoreFactor = aProperties.readFloat( prefix, "gestureDirectionIgnoreFactor", 1.8f );
        gestureMinThresholdInMillis = aProperties.readInt( prefix, "gestureMinThresholdInMillis", 25 );
        gestureMaxThresholdInMillis = aProperties.readInt( prefix, "gestureMaxThresholdInMillis", 333 );
        dragThresholdInMillis = aProperties.readInt( prefix, "dragThresholdInMillis", 125 );
        dragThresholdInPixels.width = aProperties.readInt( prefix, "dragThresholdInPixels.width", 80 );
        dragThresholdInPixels.height = aProperties.readInt( prefix, "dragThresholdInPixels.height", 80 );
        swipeStartsAction = aProperties.readBoolean( prefix, "swipeStartsAction", true );
        }

    public final void initDefaults()
        {
        presets = new TouchControlsConfiguration[1];

        presets[ 0 ] = new TouchControlsConfiguration();
        presets[ 0 ].label = "DEFAULT";
        presets[ 0 ].deadZoneSize.setTo( 9, 9 );
        presets[ 0 ].dragStepSize.setTo( 18, 18 );
        presets[ 0 ].gestureDirectionIgnoreFactor = 1.8f;
        presets[ 0 ].gestureMinThresholdInMillis = 25;
        presets[ 0 ].gestureMaxThresholdInMillis = 333;
        presets[ 0 ].dragThresholdInMillis = 125;
        presets[ 0 ].dragThresholdInPixels.setTo( 80, 80 );
        presets[ 0 ].swipeStartsAction = true;
        }


    public final Size deadZoneSize = new Size();

    public final Size dragStepSize = new Size();

    public float gestureDirectionIgnoreFactor;

    public int gestureMinThresholdInMillis;

    public int gestureMaxThresholdInMillis;

    public int dragThresholdInMillis;

    public final Size dragThresholdInPixels = new Size();

    public boolean swipeStartsAction;
    }
