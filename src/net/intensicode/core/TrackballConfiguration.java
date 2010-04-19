//#condition TRACKBALL

package net.intensicode.core;

import net.intensicode.util.Log;

public final class TrackballConfiguration
    {
    public static final String[] STRING_VALUES = { "DEFAULT", "DIRECT 1:1", "SYSTEM", "FAST", "MEDIUM", "SLOW" };

    public static final int SENSITIVITY_DEFAULT = 0;

    public static final int SENSITIVITY_DIRECT = 1;

    public static final int SENSITIVITY_SYSTEM = 2;

    public static final int SENSITIVITY_FAST = 3;

    public static final int SENSITIVITY_MEDIUM = 4;

    public static final int SENSITIVITY_SLOW = 5;


    public void setSensitivityPreset( final int aSensitivityId )
        {
        Log.debug( "TouchGesturesConfiguration#setSensitivityPreset {}", aSensitivityId );
        switch ( aSensitivityId )
            {
            default:
            case SENSITIVITY_DEFAULT:
                applyDefaultPreset();
                break;
            case SENSITIVITY_DIRECT:
                applyDirectPreset();
                break;
            case SENSITIVITY_SYSTEM:
                applySystemPreset();
                break;
            case SENSITIVITY_FAST:
                applyFastPreset();
                break;
            case SENSITIVITY_MEDIUM:
                applyMediumPreset();
                break;
            case SENSITIVITY_SLOW:
                applySlowPreset();
                break;
            }
        }

    private void applyDefaultPreset()
        {
        initialTicksThreshold = 0;
        multiTicksThreshold = 3;
        additionalMultiTicksThreshold = 1;
        directionIgnoreFactor = 1f;
        forcedSilenceBetweenEventsInMillis = 0;
        multiEventThresholdInMillis = 250;
        silenceBeforeUpdateInMillis = 50;
        }

    private void applyDirectPreset()
        {
        initialTicksThreshold = 0;
        multiTicksThreshold = 0;
        additionalMultiTicksThreshold = 1;
        directionIgnoreFactor = 5f;
        forcedSilenceBetweenEventsInMillis = 0;
        multiEventThresholdInMillis = 250;
        silenceBeforeUpdateInMillis = 0;
        }

    private void applySystemPreset()
        {
        initialTicksThreshold = 0;
        multiTicksThreshold = 6;
        additionalMultiTicksThreshold = 6;
        directionIgnoreFactor = 5f;
        forcedSilenceBetweenEventsInMillis = 0;
        multiEventThresholdInMillis = 250;
        silenceBeforeUpdateInMillis = 0;
        }

    private void applyFastPreset()
        {
        initialTicksThreshold = 0;
        multiTicksThreshold = 4;
        additionalMultiTicksThreshold = 2;
        directionIgnoreFactor = 1f;
        forcedSilenceBetweenEventsInMillis = 25;
        multiEventThresholdInMillis = 125;
        silenceBeforeUpdateInMillis = 50;
        }

    private void applyMediumPreset()
        {
        initialTicksThreshold = 0;
        multiTicksThreshold = 5;
        additionalMultiTicksThreshold = 3;
        directionIgnoreFactor = 1f;
        forcedSilenceBetweenEventsInMillis = 50;
        multiEventThresholdInMillis = 200;
        silenceBeforeUpdateInMillis = 75;
        }

    private void applySlowPreset()
        {
        initialTicksThreshold = 0;
        multiTicksThreshold = 6;
        additionalMultiTicksThreshold = 4;
        directionIgnoreFactor = 1f;
        forcedSilenceBetweenEventsInMillis = 75;
        multiEventThresholdInMillis = 250;
        silenceBeforeUpdateInMillis = 125;
        }


    public int forcedSilenceBetweenEventsInMillis = 0;

    public int silenceBeforeUpdateInMillis = 50;

    public int multiEventThresholdInMillis = 250;

    public float directionIgnoreFactor = 1f;

    public int initialTicksThreshold = 0;

    public int multiTicksThreshold = 3;

    public int additionalMultiTicksThreshold = 1;
    }
