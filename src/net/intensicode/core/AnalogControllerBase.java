package net.intensicode.core;

public abstract class AnalogControllerBase extends AnalogController
    {
    public void reset()
        {
        super.reset();
        clearTemporaries();
        }

    // Protected API

    protected AnalogControllerBase()
        {
        clearTemporaries();
        }

    protected final synchronized void onSystemUpdateEvent( final int aStepsX, final int aStepsY )
        {
        onSystemUpdateEvent( aStepsX, aStepsY, System.currentTimeMillis() );
        }

    protected final synchronized void onSystemUpdateEvent( final int aStepsX, final int aStepsY, final long aTimestamp )
        {
        if ( systemSpecificNowInMillis() - myLastUpdateTimeStamp < forcedSilenceBetweenEventsInMillis ) return;

        if ( aStepsX < 0 ) update( INDEX_LEFT, aStepsX );
        if ( aStepsX > 0 ) update( INDEX_RIGHT, aStepsX );
        if ( aStepsY < 0 ) update( INDEX_UP, aStepsY );
        if ( aStepsY > 0 ) update( INDEX_DOWN, aStepsY );

        if ( myAccumulationTicks == 0 ) myFirstDataTimeStamp = aTimestamp;
        myLastDataTimeStamp = aTimestamp;
        myAccumulationTicks++;
        myNewDataFlag = true;
        }

    protected abstract long systemSpecificNowInMillis();

    // From AnalogController

    protected final boolean hasNewData()
        {
        final long nowInMillis = systemSpecificNowInMillis();

        final long millisSinceFirstEvent = nowInMillis - myFirstDataTimeStamp;
        if ( millisSinceFirstEvent > multiEventThresholdInMillis ) return myNewDataFlag;

        final long millisSinceLastEvent = nowInMillis - myLastDataTimeStamp;
        return millisSinceLastEvent >= silenceBeforeUpdateInMillis && myNewDataFlag;
        }

    protected final synchronized void updateDeltaValues()
        {
        processValues();
        normalizeValues();

        leftDelta = myAccumulatedValues[ INDEX_LEFT ];
        rightDelta = myAccumulatedValues[ INDEX_RIGHT ];
        upDelta = myAccumulatedValues[ INDEX_UP ];
        downDelta = myAccumulatedValues[ INDEX_DOWN ];

        leftMax = myLargestValues[ INDEX_LEFT ];
        rightMax = myLargestValues[ INDEX_RIGHT ];
        upMax = myLargestValues[ INDEX_UP ];
        downMax = myLargestValues[ INDEX_DOWN ];

        leftRaw = myAccumulatedRawValues[ INDEX_LEFT ];
        rightRaw = myAccumulatedRawValues[ INDEX_RIGHT ];
        upRaw = myAccumulatedRawValues[ INDEX_UP ];
        downRaw = myAccumulatedRawValues[ INDEX_DOWN ];

        myLastUpdateTimeStamp = systemSpecificNowInMillis();

        accumulationTicks = myAccumulationTicks;

        clearTemporaries();
        }

    // Implementation

    private void update( final int aValueIndex, final int aRawInput )
        {
        final int absoluteInput = Math.abs( aRawInput );

        myLargestValues[ aValueIndex ] = Math.max( myLargestValues[ aValueIndex ], absoluteInput );
        myAccumulatedRawValues[ aValueIndex ] += absoluteInput;
        myAccumulatedValues[ aValueIndex ] += absoluteInput;
        }

    private void processValues()
        {
        processValue( INDEX_LEFT );
        processValue( INDEX_RIGHT );
        processValue( INDEX_UP );
        processValue( INDEX_DOWN );
        }

    private void processValue( final int aValueIndex )
        {
        if ( myAccumulatedValues[ aValueIndex ] <= initialTicksThreshold )
            {
            myAccumulatedValues[ aValueIndex ] = 0;
            return;
            }

        // convert the 'initial ticks' into the 'first tick':
        myAccumulatedValues[ aValueIndex ] -= initialTicksThreshold;
        myAccumulatedValues[ aValueIndex ] += 1;

        if ( myAccumulatedValues[ aValueIndex ] <= 1 + multiTicksThreshold )
            {
            myAccumulatedValues[ aValueIndex ] = 1;
            return;
            }

        // convert the 'multi ticks' into the 'second tick':
        myAccumulatedValues[ aValueIndex ] -= multiTicksThreshold;
        myAccumulatedValues[ aValueIndex ] += 1;

        final int additionalMultiTicksRaw = myAccumulatedValues[ aValueIndex ] - 2;
        final int additionalMultiTicks = additionalMultiTicksRaw / ( additionalMultiTicksThreshold + 1 );
        myAccumulatedValues[ aValueIndex ] = 2 + additionalMultiTicks;
        }

    private void normalizeValues()
        {
        final int maxDelta = Math.max( leftDelta, Math.max( rightDelta, Math.max( upDelta, downDelta ) ) );
        final int threshold = maxDelta / 3;
        if ( leftDelta <= threshold ) leftDelta = 0;
        if ( rightDelta <= threshold ) rightDelta = 0;
        if ( upDelta <= threshold ) upDelta = 0;
        if ( downDelta <= threshold ) downDelta = 0;
        }

    private void clearTemporaries()
        {
        for ( int idx = 0; idx < INDEX_COUNT; idx++ )
            {
            myLargestValues[ idx ] = myAccumulatedRawValues[ idx ] = myAccumulatedValues[ idx ] = 0;
            myInitialTicksThresholds[ idx ] = initialTicksThreshold;
            myMultiTicksThresholds[ idx ] = multiTicksThreshold;
            myAdditionalMultiTicksThresholds[ idx ] = additionalMultiTicksThreshold;
            }

        myAccumulationTicks = 0;
        myNewDataFlag = false;
        }


    private boolean myNewDataFlag;

    private int myAccumulationTicks;

    private long myLastDataTimeStamp;

    private long myFirstDataTimeStamp;

    private long myLastUpdateTimeStamp;

    private static final int INDEX_LEFT = 0;

    private static final int INDEX_RIGHT = 1;

    private static final int INDEX_UP = 2;

    private static final int INDEX_DOWN = 3;

    private static final int INDEX_COUNT = 4;

    private final int[] myLargestValues = new int[INDEX_COUNT];

    private final int[] myAccumulatedValues = new int[INDEX_COUNT];

    private final int[] myAccumulatedRawValues = new int[INDEX_COUNT];

    private final int[] myInitialTicksThresholds = new int[INDEX_COUNT];

    private final int[] myMultiTicksThresholds = new int[INDEX_COUNT];

    private final int[] myAdditionalMultiTicksThresholds = new int[INDEX_COUNT];
    }
