//#condition TRACKBALL

package net.intensicode.trackball;

import net.intensicode.PlatformContext;

public final class TrackballController implements TrackballEventListener
    {
    public final TrackballConfiguration configuration;

    private final PlatformContext myPlatformContext;

    public boolean autoClear;

    public int leftDelta;

    public int rightDelta;

    public int upDelta;

    public int downDelta;

    public int leftMax;

    public int rightMax;

    public int upMax;

    public int downMax;

    public int leftRaw;

    public int rightRaw;

    public int upRaw;

    public int downRaw;

    public int accumulationTicks;


    public TrackballController( final TrackballConfiguration aConfiguration, final PlatformContext aPlatformContext )
        {
        configuration = aConfiguration;
        myPlatformContext = aPlatformContext;
        clearTemporaries();
        }

    public final boolean hasNonZeroData()
        {
        return ( leftDelta > 0 || rightDelta > 0 || upDelta > 0 || downDelta > 0 );
        }

    public final int getDeltaX()
        {
        return rightDelta - leftDelta;
        }

    public final int getDeltaY()
        {
        return downDelta - upDelta;
        }

    public final int getMaxInAnyDirection()
        {
        return Math.max( leftMax, Math.max( rightMax, Math.max( upMax, downMax ) ) );
        }

    public final boolean shouldIgnoreDeltaX()
        {
        final float scaledDeltaX = Math.abs( getDeltaX() ) * configuration.directionIgnoreFactor;
        return Math.abs( getDeltaY() ) > scaledDeltaX;
        }

    public final boolean shouldIgnoreDeltaY()
        {
        final float scaledDeltaY = Math.abs( getDeltaY() ) * configuration.directionIgnoreFactor;
        return Math.abs( getDeltaX() ) > scaledDeltaY;
        }

    public void reset()
        {
        clearDeltaValues();
        clearTemporaries();
        }

    public final void clearDeltaValues()
        {
        leftDelta = rightDelta = upDelta = downDelta = 0;
        leftMax = rightMax = upMax = downMax = 0;
        accumulationTicks = 0;
        }

    public final void onControlTick()
        {
        if ( hasNewData() ) updateDeltaValues();
        else if ( autoClear ) clearDeltaValues();
        }

    // From TrackballEventListener

    public void onTrackballEvent( final TrackballEvent aTrackballEvent )
        {
        onSystemUpdateEvent( aTrackballEvent.getX(), aTrackballEvent.getY(), aTrackballEvent.timestamp() );
        }

    // Protected API

    protected final synchronized void onSystemUpdateEvent( final int aStepsX, final int aStepsY, final long aTimestamp )
        {
        final long updateDeltaInMillis = myPlatformContext.compatibleTimeInMillis() - myLastUpdateTimeStamp;
        if ( updateDeltaInMillis < configuration.forcedSilenceBetweenEventsInMillis ) return;

        if ( aStepsX < 0 ) update( INDEX_LEFT, aStepsX );
        if ( aStepsX > 0 ) update( INDEX_RIGHT, aStepsX );
        if ( aStepsY < 0 ) update( INDEX_UP, aStepsY );
        if ( aStepsY > 0 ) update( INDEX_DOWN, aStepsY );

        if ( myAccumulationTicks == 0 ) myFirstDataTimeStamp = aTimestamp;
        myLastDataTimeStamp = aTimestamp;
        myAccumulationTicks++;

        myNewDataFlag |= aStepsX != 0 || aStepsY != 0;
        }

    protected final boolean hasNewData()
        {
        final long nowInMillis = myPlatformContext.compatibleTimeInMillis();

        final long millisSinceFirstEvent = nowInMillis - myFirstDataTimeStamp;
        if ( millisSinceFirstEvent > configuration.multiEventThresholdInMillis ) return myNewDataFlag;

        final long millisSinceLastEvent = nowInMillis - myLastDataTimeStamp;
        return millisSinceLastEvent >= configuration.silenceBeforeUpdateInMillis && myNewDataFlag;
        }

    protected final synchronized void updateDeltaValues()
        {
        processValues();

        leftDelta = myAccumulatedValues[ INDEX_LEFT ];
        rightDelta = myAccumulatedValues[ INDEX_RIGHT ];
        upDelta = myAccumulatedValues[ INDEX_UP ];
        downDelta = myAccumulatedValues[ INDEX_DOWN ];

        normalizeDeltas();

        leftMax = myLargestValues[ INDEX_LEFT ];
        rightMax = myLargestValues[ INDEX_RIGHT ];
        upMax = myLargestValues[ INDEX_UP ];
        downMax = myLargestValues[ INDEX_DOWN ];

        leftRaw = myAccumulatedRawValues[ INDEX_LEFT ];
        rightRaw = myAccumulatedRawValues[ INDEX_RIGHT ];
        upRaw = myAccumulatedRawValues[ INDEX_UP ];
        downRaw = myAccumulatedRawValues[ INDEX_DOWN ];

        myLastUpdateTimeStamp = myPlatformContext.compatibleTimeInMillis();

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
        if ( myAccumulatedValues[ aValueIndex ] <= configuration.initialTicksThreshold )
            {
            myAccumulatedValues[ aValueIndex ] = 0;
            return;
            }

        // convert the 'initial ticks' into the 'first tick':
        myAccumulatedValues[ aValueIndex ] -= configuration.initialTicksThreshold;
        myAccumulatedValues[ aValueIndex ] += 1;

        if ( myAccumulatedValues[ aValueIndex ] <= 1 + configuration.multiTicksThreshold )
            {
            myAccumulatedValues[ aValueIndex ] = 1;
            return;
            }

        // convert the 'multi ticks' into the 'second tick':
        myAccumulatedValues[ aValueIndex ] -= configuration.multiTicksThreshold;
        myAccumulatedValues[ aValueIndex ] += 1;

        if ( configuration.additionalMultiTicksThreshold == 0 )
            {
            myAccumulatedValues[ aValueIndex ] = 2;
            return;
            }

        final int additionalMultiTicksRaw = myAccumulatedValues[ aValueIndex ] - 2;
        final int additionalMultiTicks = additionalMultiTicksRaw / configuration.additionalMultiTicksThreshold;
        myAccumulatedValues[ aValueIndex ] = 2 + additionalMultiTicks;
        }

    private void normalizeDeltas()
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
            myInitialTicksThresholds[ idx ] = configuration.initialTicksThreshold;
            myMultiTicksThresholds[ idx ] = configuration.multiTicksThreshold;
            myAdditionalMultiTicksThresholds[ idx ] = configuration.additionalMultiTicksThreshold;
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
