package net.intensicode.core;

public abstract class AnalogControllerBase extends AnalogController
    {
    protected AnalogControllerBase()
        {
        }

    // Protected API

    protected final synchronized void onSystemUpdateEvent( final int aStepsX, final int aStepsY )
        {
        if ( aStepsX < 0 ) update( INDEX_LEFT, aStepsX );
        if ( aStepsX > 0 ) update( INDEX_RIGHT, aStepsX );
        if ( aStepsY < 0 ) update( INDEX_UP, aStepsY );
        if ( aStepsY > 0 ) update( INDEX_DOWN, aStepsY );

        if ( myAccumulationTicks == 0 ) myFirstDataTimeStamp = System.currentTimeMillis();
        myLastDataTimeStamp = System.currentTimeMillis();

        myAccumulationTicks++;

        myNewDataFlag = true;
        }

    // From AnalogController

    protected final boolean hasNewData()
        {
        final long nowInMillis = System.currentTimeMillis();

        final long millisSinceFirstEvent = nowInMillis - myFirstDataTimeStamp;
        if ( millisSinceFirstEvent > multiEventThresholdInMillis ) return myNewDataFlag;

        final long millisSinceLastEvent = nowInMillis - myLastDataTimeStamp;
        return millisSinceLastEvent > silenceBeforeUpdateInMillis && myNewDataFlag;
        }

    protected final synchronized void updateDeltaValues()
        {
        leftDelta = myAccumulatedValues[ INDEX_LEFT ];
        rightDelta = myAccumulatedValues[ INDEX_RIGHT ];
        upDelta = myAccumulatedValues[ INDEX_UP ];
        downDelta = myAccumulatedValues[ INDEX_DOWN ];

        leftMax = myLargestValues[ INDEX_LEFT ];
        rightMax = myLargestValues[ INDEX_RIGHT ];
        upMax = myLargestValues[ INDEX_UP ];
        downMax = myLargestValues[ INDEX_DOWN ];

        accumulationTicks = myAccumulationTicks;

        clearTemporaries();
        }

    // Implementation

    private void update( final int aValueIndex, final int aRawInput )
        {
        final int absoluteInput = Math.abs( aRawInput );
        myLargestValues[ aValueIndex ] = Math.max( myLargestValues[ aValueIndex ], absoluteInput );
        myAccumulatedValues[ aValueIndex ] += absoluteInput;
        }

    private void clearTemporaries()
        {
        for ( int idx = 0; idx < INDEX_COUNT; idx++ )
            {
            myLargestValues[ idx ] = myAccumulatedValues[ idx ] = 0;
            }

        myFirstDataTimeStamp = myLastDataTimeStamp = 0;
        myAccumulationTicks = 0;
        myNewDataFlag = false;
        }


    private boolean myNewDataFlag;

    private int myAccumulationTicks;

    private long myLastDataTimeStamp;

    private long myFirstDataTimeStamp;

    private static final int INDEX_LEFT = 0;

    private static final int INDEX_RIGHT = 1;

    private static final int INDEX_UP = 2;

    private static final int INDEX_DOWN = 3;

    private static final int INDEX_COUNT = 4;

    private final int[] myLargestValues = new int[INDEX_COUNT];

    private final int[] myAccumulatedValues = new int[INDEX_COUNT];
    }
