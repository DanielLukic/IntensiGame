//#condition TRACKBALL

package net.intensicode.configuration.trackball;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.TrackballController;
import net.intensicode.util.*;

public final class TrackballPreset implements ConfigurableIntegerValue
    {
    public TrackballPreset( final TrackballController aTrackballController )
        {
        myTrackballController = aTrackballController;
        }

    // From SeekBarDialogBase

    public final String getTitle()
        {
        return "Choose preset";
        }

    public final String getInfoText()
        {
        return "Apply a preset configuration to the trackball system.";
        }

    public final String getValueAsText( final int aConfiguredValue )
        {
        return PRESETS[ aConfiguredValue ];
        }

    public final void setNewValue( final int aConfiguredValue )
        {
        final String preset = PRESETS[ aConfiguredValue ];
        Log.debug( "switching to trackball preset {}", preset );
        if ( "KEEP CURRENT".equals( preset ) ) return;
        if ( "DEFAULTS".equals( preset ) ) applyDefaultsPreset();
        if ( "DIRECT 1:1".equals( preset ) ) applyDirectPreset();
        if ( "SYSTEM".equals( preset ) ) applySystemPreset();
        if ( "FAST".equals( preset ) ) applyFastPreset();
        if ( "MEDIUM".equals( preset ) ) applyMediumPreset();
        if ( "SLOW".equals( preset ) ) applySlowPreset();
        }

    private void applyDefaultsPreset()
        {
        myTrackballController.initialTicksThreshold = 0;
        myTrackballController.multiTicksThreshold = 3;
        myTrackballController.additionalMultiTicksThreshold = 1;
        myTrackballController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myTrackballController.forcedSilenceBetweenEventsInMillis = 0;
        myTrackballController.multiEventThresholdInMillis = 250;
        myTrackballController.silenceBeforeUpdateInMillis = 50;
        }

    private void applyDirectPreset()
        {
        myTrackballController.initialTicksThreshold = 0;
        myTrackballController.multiTicksThreshold = 0;
        myTrackballController.additionalMultiTicksThreshold = 1;
        myTrackballController.directionIgnoreFactorFixed = FixedMath.FIXED_5;
        myTrackballController.forcedSilenceBetweenEventsInMillis = 0;
        myTrackballController.multiEventThresholdInMillis = 250;
        myTrackballController.silenceBeforeUpdateInMillis = 0;
        }

    private void applySystemPreset()
        {
        myTrackballController.initialTicksThreshold = 0;
        myTrackballController.multiTicksThreshold = 6;
        myTrackballController.additionalMultiTicksThreshold = 6;
        myTrackballController.directionIgnoreFactorFixed = FixedMath.FIXED_5;
        myTrackballController.forcedSilenceBetweenEventsInMillis = 0;
        myTrackballController.multiEventThresholdInMillis = 250;
        myTrackballController.silenceBeforeUpdateInMillis = 0;
        }

    private void applyFastPreset()
        {
        myTrackballController.initialTicksThreshold = 0;
        myTrackballController.multiTicksThreshold = 4;
        myTrackballController.additionalMultiTicksThreshold = 2;
        myTrackballController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myTrackballController.forcedSilenceBetweenEventsInMillis = 25;
        myTrackballController.multiEventThresholdInMillis = 125;
        myTrackballController.silenceBeforeUpdateInMillis = 50;
        }

    private void applyMediumPreset()
        {
        myTrackballController.initialTicksThreshold = 0;
        myTrackballController.multiTicksThreshold = 5;
        myTrackballController.additionalMultiTicksThreshold = 3;
        myTrackballController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myTrackballController.forcedSilenceBetweenEventsInMillis = 50;
        myTrackballController.multiEventThresholdInMillis = 200;
        myTrackballController.silenceBeforeUpdateInMillis = 75;
        }

    private void applySlowPreset()
        {
        myTrackballController.initialTicksThreshold = 0;
        myTrackballController.multiTicksThreshold = 6;
        myTrackballController.additionalMultiTicksThreshold = 4;
        myTrackballController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myTrackballController.forcedSilenceBetweenEventsInMillis = 75;
        myTrackballController.multiEventThresholdInMillis = 250;
        myTrackballController.silenceBeforeUpdateInMillis = 125;
        }

    public final int getMaxValue()
        {
        return PRESETS.length - 1;
        }

    public final int getCurrentValue()
        {
        return 0;
        }

    public final int getStepSize()
        {
        return DEFAULT_STEP_SIZE;
        }


    private final TrackballController myTrackballController;

    private static final String[] PRESETS = { "KEEP CURRENT", "DEFAULTS", "DIRECT 1:1", "SYSTEM", "FAST", "MEDIUM", "SLOW" };
    }
