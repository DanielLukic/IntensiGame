//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.core.Configuration;

public final class TouchGesturesConfiguration extends TouchConfiguration
    {
    public final void setTo( final TouchConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;
        final TouchGesturesConfiguration newGesturesConfiguration = (TouchGesturesConfiguration) aNewConfiguration;
        gestureStartThresholdInMillis = newGesturesConfiguration.gestureStartThresholdInMillis;
        breakTimeThresholdInMillis = newGesturesConfiguration.breakTimeThresholdInMillis;
        samePositionThresholdInPixels = newGesturesConfiguration.samePositionThresholdInPixels;
        strokeThresholdInPixels = newGesturesConfiguration.strokeThresholdInPixels;
        directionIgnoreFactor = newGesturesConfiguration.directionIgnoreFactor;
        }

    public final void initFromProperties( final Configuration aProperties, final String aPresetName )
        {
        final String prefix = "gestures." + aPresetName;
        label = aProperties.readString( prefix, "label", aPresetName );
        gestureStartThresholdInMillis = aProperties.readInt( prefix, "gestureStartThresholdInMillis", 25 );
        breakTimeThresholdInMillis = aProperties.readInt( prefix, "breakTimeThresholdInMillis", 20 );
        samePositionThresholdInPixels = aProperties.readInt( prefix, "samePositionThresholdInPixels", 10 );
        strokeThresholdInPixels = aProperties.readInt( prefix, "strokeThresholdInPixels", 40 );
        directionIgnoreFactor = aProperties.readFloat( prefix, "directionIgnoreFactor", 20 );
        }

    public final void initDefaults()
        {
        presets = new TouchGesturesConfiguration[3];
        presets[ 0 ] = createLowSensitivityConfiguration();
        presets[ 2 ] = createMediumSensitivityConfiguration();
        presets[ 2 ] = createHighSensitivityConfiguration();
        }

    public static TouchGesturesConfiguration createLowSensitivityConfiguration()
        {
        final TouchGesturesConfiguration configuration = new TouchGesturesConfiguration();
        configuration.label = "LOW";
        configuration.gestureStartThresholdInMillis = 40;
        configuration.breakTimeThresholdInMillis = 120;
        configuration.samePositionThresholdInPixels = 25;
        configuration.strokeThresholdInPixels = 20;
        configuration.directionIgnoreFactor = 1.9f;
        return configuration;
        }

    public static TouchGesturesConfiguration createMediumSensitivityConfiguration()
        {
        final TouchGesturesConfiguration configuration = new TouchGesturesConfiguration();
        configuration.label = "MEDIUM";
        configuration.gestureStartThresholdInMillis = 25;
        configuration.breakTimeThresholdInMillis = 80;
        configuration.samePositionThresholdInPixels = 12;
        configuration.strokeThresholdInPixels = 6;
        configuration.directionIgnoreFactor = 1.85f;
        return configuration;
        }

    public static TouchGesturesConfiguration createHighSensitivityConfiguration()
        {
        final TouchGesturesConfiguration configuration = new TouchGesturesConfiguration();
        configuration.label = "HIGH";
        configuration.gestureStartThresholdInMillis = 25;
        configuration.breakTimeThresholdInMillis = 40;
        configuration.samePositionThresholdInPixels = 8;
        configuration.strokeThresholdInPixels = 4;
        configuration.directionIgnoreFactor = 1.75f;
        return configuration;
        }


    public int gestureStartThresholdInMillis = 25;

    public int breakTimeThresholdInMillis = 80;

    public int samePositionThresholdInPixels = 12;

    public int strokeThresholdInPixels = 6;

    public float directionIgnoreFactor = 1.85f;
    }
