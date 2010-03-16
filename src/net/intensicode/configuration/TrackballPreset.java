package net.intensicode.configuration;

import net.intensicode.ConfigurableIntegerValue;
import net.intensicode.core.AnalogController;
import net.intensicode.util.*;

public final class TrackballPreset implements ConfigurableIntegerValue
    {
    public TrackballPreset( final AnalogController aAnalogController )
        {
        myAnalogController = aAnalogController;
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
        myAnalogController.initialTicksThreshold = 0;
        myAnalogController.multiTicksThreshold = 3;
        myAnalogController.additionalMultiTicksThreshold = 1;
        myAnalogController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myAnalogController.forcedSilenceBetweenEventsInMillis = 0;
        myAnalogController.multiEventThresholdInMillis = 250;
        myAnalogController.silenceBeforeUpdateInMillis = 50;
        }

    private void applyDirectPreset()
        {
        myAnalogController.initialTicksThreshold = 0;
        myAnalogController.multiTicksThreshold = 0;
        myAnalogController.additionalMultiTicksThreshold = 1;
        myAnalogController.directionIgnoreFactorFixed = FixedMath.FIXED_5;
        myAnalogController.forcedSilenceBetweenEventsInMillis = 0;
        myAnalogController.multiEventThresholdInMillis = 250;
        myAnalogController.silenceBeforeUpdateInMillis = 0;
        }

    private void applySystemPreset()
        {
        myAnalogController.initialTicksThreshold = 0;
        myAnalogController.multiTicksThreshold = 6;
        myAnalogController.additionalMultiTicksThreshold = 6;
        myAnalogController.directionIgnoreFactorFixed = FixedMath.FIXED_5;
        myAnalogController.forcedSilenceBetweenEventsInMillis = 0;
        myAnalogController.multiEventThresholdInMillis = 250;
        myAnalogController.silenceBeforeUpdateInMillis = 0;
        }

    private void applyFastPreset()
        {
        myAnalogController.initialTicksThreshold = 0;
        myAnalogController.multiTicksThreshold = 4;
        myAnalogController.additionalMultiTicksThreshold = 2;
        myAnalogController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myAnalogController.forcedSilenceBetweenEventsInMillis = 25;
        myAnalogController.multiEventThresholdInMillis = 125;
        myAnalogController.silenceBeforeUpdateInMillis = 50;
        }

    private void applyMediumPreset()
        {
        myAnalogController.initialTicksThreshold = 0;
        myAnalogController.multiTicksThreshold = 5;
        myAnalogController.additionalMultiTicksThreshold = 3;
        myAnalogController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myAnalogController.forcedSilenceBetweenEventsInMillis = 50;
        myAnalogController.multiEventThresholdInMillis = 200;
        myAnalogController.silenceBeforeUpdateInMillis = 75;
        }

    private void applySlowPreset()
        {
        myAnalogController.initialTicksThreshold = 0;
        myAnalogController.multiTicksThreshold = 6;
        myAnalogController.additionalMultiTicksThreshold = 4;
        myAnalogController.directionIgnoreFactorFixed = FixedMath.FIXED_1;
        myAnalogController.forcedSilenceBetweenEventsInMillis = 75;
        myAnalogController.multiEventThresholdInMillis = 250;
        myAnalogController.silenceBeforeUpdateInMillis = 125;
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


    private final AnalogController myAnalogController;

    private static final String[] PRESETS = { "KEEP CURRENT", "DEFAULTS", "DIRECT 1:1", "SYSTEM", "FAST", "MEDIUM", "SLOW" };
    }
