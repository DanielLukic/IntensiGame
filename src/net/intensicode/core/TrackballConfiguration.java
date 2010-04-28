//#condition TRACKBALL

package net.intensicode.core;

public final class TrackballConfiguration
    {
    public TrackballConfiguration[] presets;

    public String label;


    public final String[] getLabels()
        {
        final String[] labels = new String[presets.length];
        for ( int idx = 0; idx < presets.length; idx++ )
            {
            labels[ idx ] = presets[ idx ].label;
            }
        return labels;
        }

    public final void setTo( final TrackballConfiguration aNewConfiguration )
        {
        label = aNewConfiguration.label;

        initialTicksThreshold = aNewConfiguration.initialTicksThreshold;
        multiTicksThreshold = aNewConfiguration.multiTicksThreshold;
        additionalMultiTicksThreshold = aNewConfiguration.additionalMultiTicksThreshold;
        directionIgnoreFactor = aNewConfiguration.directionIgnoreFactor;
        forcedSilenceBetweenEventsInMillis = aNewConfiguration.forcedSilenceBetweenEventsInMillis;
        multiEventThresholdInMillis = aNewConfiguration.multiEventThresholdInMillis;
        silenceBeforeUpdateInMillis = aNewConfiguration.silenceBeforeUpdateInMillis;
        }

    public final void initFromProperties( final Configuration aProperties, final String aPresetName )
        {
        final String prefix = "gestures." + aPresetName;
        label = aProperties.readString( prefix, "label", aPresetName );
        initialTicksThreshold = aProperties.readInt( prefix, "initialTicksThreshold", 0 );
        multiTicksThreshold = aProperties.readInt( prefix, "multiTicksThreshold", 3 );
        additionalMultiTicksThreshold = aProperties.readInt( prefix, "additionalMultiTicksThreshold", 1 );
        directionIgnoreFactor = aProperties.readFloat( prefix, "directionIgnoreFactor", 20 );
        forcedSilenceBetweenEventsInMillis = aProperties.readInt( prefix, "forcedSilenceBetweenEventsInMillis", 0 );
        multiEventThresholdInMillis = aProperties.readInt( prefix, "multiEventThresholdInMillis", 250 );
        silenceBeforeUpdateInMillis = aProperties.readInt( prefix, "silenceBeforeUpdateInMillis", 50 );
        }

    public final void initDefaults()
        {
        presets = new TrackballConfiguration[6];

        final TrackballConfiguration p0 = presets[ 0 ] = new TrackballConfiguration();
        p0.initialTicksThreshold = 0;
        p0.multiTicksThreshold = 3;
        p0.additionalMultiTicksThreshold = 1;
        p0.directionIgnoreFactor = 1f;
        p0.forcedSilenceBetweenEventsInMillis = 0;
        p0.multiEventThresholdInMillis = 250;
        p0.silenceBeforeUpdateInMillis = 50;

        final TrackballConfiguration p1 = presets[ 1 ] = new TrackballConfiguration();
        p1.initialTicksThreshold = 0;
        p1.multiTicksThreshold = 0;
        p1.additionalMultiTicksThreshold = 1;
        p1.directionIgnoreFactor = 5f;
        p1.forcedSilenceBetweenEventsInMillis = 0;
        p1.multiEventThresholdInMillis = 250;
        p1.silenceBeforeUpdateInMillis = 0;

        final TrackballConfiguration p2 = presets[ 2 ] = new TrackballConfiguration();
        p2.initialTicksThreshold = 0;
        p2.multiTicksThreshold = 6;
        p2.additionalMultiTicksThreshold = 6;
        p2.directionIgnoreFactor = 5f;
        p2.forcedSilenceBetweenEventsInMillis = 0;
        p2.multiEventThresholdInMillis = 250;
        p2.silenceBeforeUpdateInMillis = 0;

        final TrackballConfiguration p3 = presets[ 3 ] = new TrackballConfiguration();
        p3.initialTicksThreshold = 0;
        p3.multiTicksThreshold = 4;
        p3.additionalMultiTicksThreshold = 2;
        p3.directionIgnoreFactor = 1f;
        p3.forcedSilenceBetweenEventsInMillis = 25;
        p3.multiEventThresholdInMillis = 125;
        p3.silenceBeforeUpdateInMillis = 50;

        final TrackballConfiguration p4 = presets[ 4 ] = new TrackballConfiguration();
        p4.initialTicksThreshold = 0;
        p4.multiTicksThreshold = 5;
        p4.additionalMultiTicksThreshold = 3;
        p4.directionIgnoreFactor = 1f;
        p4.forcedSilenceBetweenEventsInMillis = 50;
        p4.multiEventThresholdInMillis = 200;
        p4.silenceBeforeUpdateInMillis = 75;

        final TrackballConfiguration p5 = presets[ 5 ] = new TrackballConfiguration();
        p5.initialTicksThreshold = 0;
        p5.multiTicksThreshold = 6;
        p5.additionalMultiTicksThreshold = 4;
        p5.directionIgnoreFactor = 1f;
        p5.forcedSilenceBetweenEventsInMillis = 75;
        p5.multiEventThresholdInMillis = 250;
        p5.silenceBeforeUpdateInMillis = 125;
        }

    public final boolean equals( final Object aThat )
        {
        if ( !( aThat instanceof TrackballConfiguration ) ) return false;

        final TrackballConfiguration that = (TrackballConfiguration) aThat;
        if ( this.forcedSilenceBetweenEventsInMillis != that.forcedSilenceBetweenEventsInMillis ) return false;
        if ( this.silenceBeforeUpdateInMillis != that.silenceBeforeUpdateInMillis ) return false;
        if ( this.multiEventThresholdInMillis != that.multiEventThresholdInMillis ) return false;
        if ( this.directionIgnoreFactor != that.directionIgnoreFactor ) return false;
        if ( this.initialTicksThreshold != that.initialTicksThreshold ) return false;
        if ( this.multiTicksThreshold != that.multiTicksThreshold ) return false;
        if ( this.additionalMultiTicksThreshold != that.additionalMultiTicksThreshold ) return false;

        return true;
        }

    public int forcedSilenceBetweenEventsInMillis = 0;

    public int silenceBeforeUpdateInMillis = 50;

    public int multiEventThresholdInMillis = 250;

    public float directionIgnoreFactor = 1f;

    public int initialTicksThreshold = 0;

    public int multiTicksThreshold = 3;

    public int additionalMultiTicksThreshold = 1;
    }
