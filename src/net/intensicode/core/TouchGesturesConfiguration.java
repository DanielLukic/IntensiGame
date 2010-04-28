//#condition TOUCH

package net.intensicode.core;

public final class TouchGesturesConfiguration extends TouchConfiguration
    {
    public TouchGesturesConfiguration[] presets;


    public final String[] getLabels()
        {
        return super.getLabels( presets );
        }

    public final void setTo( final TouchGesturesConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;
        gestureStartThresholdInMillis = aNewConfiguration.gestureStartThresholdInMillis;
        breakTimeThresholdInMillis = aNewConfiguration.breakTimeThresholdInMillis;
        samePositionThresholdInPixels = aNewConfiguration.samePositionThresholdInPixels;
        strokeThresholdInPixels = aNewConfiguration.strokeThresholdInPixels;
        directionIgnoreFactor = aNewConfiguration.directionIgnoreFactor;
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
        presets = new TouchGesturesConfiguration[5];

        presets[ 0 ] = new TouchGesturesConfiguration();
        presets[ 0 ].gestureStartThresholdInMillis = 40;
        presets[ 0 ].breakTimeThresholdInMillis = 120;
        presets[ 0 ].samePositionThresholdInPixels = 25;
        presets[ 0 ].strokeThresholdInPixels = 20;
        presets[ 0 ].directionIgnoreFactor = 1.9f;

        presets[ 1 ] = new TouchGesturesConfiguration();
        presets[ 1 ].gestureStartThresholdInMillis = 30;
        presets[ 1 ].breakTimeThresholdInMillis = 100;
        presets[ 1 ].samePositionThresholdInPixels = 20;
        presets[ 1 ].strokeThresholdInPixels = 10;
        presets[ 1 ].directionIgnoreFactor = 1.9f;

        presets[ 2 ] = new TouchGesturesConfiguration();
        presets[ 2 ].gestureStartThresholdInMillis = 25;
        presets[ 2 ].breakTimeThresholdInMillis = 80;
        presets[ 2 ].samePositionThresholdInPixels = 12;
        presets[ 2 ].strokeThresholdInPixels = 6;
        presets[ 2 ].directionIgnoreFactor = 1.85f;

        presets[ 3 ] = new TouchGesturesConfiguration();
        presets[ 3 ].gestureStartThresholdInMillis = 25;
        presets[ 3 ].breakTimeThresholdInMillis = 60;
        presets[ 3 ].samePositionThresholdInPixels = 10;
        presets[ 3 ].strokeThresholdInPixels = 5;
        presets[ 3 ].directionIgnoreFactor = 1.75f;

        presets[ 4 ] = new TouchGesturesConfiguration();
        presets[ 4 ].gestureStartThresholdInMillis = 25;
        presets[ 4 ].breakTimeThresholdInMillis = 40;
        presets[ 4 ].samePositionThresholdInPixels = 8;
        presets[ 4 ].strokeThresholdInPixels = 4;
        presets[ 4 ].directionIgnoreFactor = 1.75f;
        }


    public int gestureStartThresholdInMillis;

    public int breakTimeThresholdInMillis;

    public int samePositionThresholdInPixels;

    public int strokeThresholdInPixels;

    public float directionIgnoreFactor;
    }
