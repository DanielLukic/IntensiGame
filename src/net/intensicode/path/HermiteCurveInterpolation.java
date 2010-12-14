package net.intensicode.path;

import net.intensicode.util.*;


/**
 * See http://www.cubic.org/docs/hermite.htm for details.
 */
public final class HermiteCurveInterpolation implements Interpolation, PositionList
    {
    public static final int MAX_INPUT_POINTS = 32;


    public HermiteCurveInterpolation()
        {
        for ( int idx = 0; idx < MAX_INPUT_POINTS; idx++ )
            {
            myInputPositions[ idx ] = new PositionF();
            myIncomingTangents[ idx ] = new PositionF();
            myOutgoingTangents[ idx ] = new PositionF();
            }
        }

    // From PositionList

    public final void clear()
        {
        myInputSize = 0;
        }

    public final PositionList add( final PositionF aPosition )
        {
        if ( myInputSize > 0 )
            {
            final PositionF previous = myInputPositions[ myInputSize - 1 ];
            //#if DEBUG
            if ( previous.x == aPosition.x && previous.y == aPosition.y )
                {
                throw new IllegalArgumentException();
                }
            //#endif
            if ( previous.x == aPosition.x && previous.y == aPosition.y ) return this;
            }
        myInputPositions[ myInputSize++ ].setTo( aPosition );

        if ( myInputSize > 1 )
            {
            myIncomingTangents[ myInputSize - 2 ].setTo( calcIncomingTangent( myInputSize - 2 ) );
            myOutgoingTangents[ myInputSize - 2 ].setTo( calcOutgoingTangent( myInputSize - 2 ) );
            myIncomingTangents[ myInputSize - 1 ].setTo( calcIncomingTangent( myInputSize - 1 ) );
            myOutgoingTangents[ myInputSize - 1 ].setTo( calcOutgoingTangent( myInputSize - 1 ) );
            }

        return this;
        }

    public final void end()
        {
        }

    // From Interpolation

    public int getNumberOfInputPositions()
        {
        return myInputSize;
        }

    public final PositionF getInputPosition( final int aIdx )
        {
        return myInputPositions[ aIdx ];
        }

    public final PositionF getPosition( final int aSegmentIndex, final float aSegmentPos )
        {
        //#if DEBUG
        if ( aSegmentPos < 0 || aSegmentPos > 1f )
            throw new IllegalArgumentException();
        //#endif

        final float s = aSegmentPos;
        final float s2 = ( s * s );
        final float s3 = ( s * s2 );

        final float h1 = 2 * s3 - 3 * s2 + 1f;
        final float h2 = -2 * s3 + 3 * s2;
        final float h3 = s3 - 2 * s2 + s;
        final float h4 = s3 - s2;

        final PositionF p1 = myInputPositions[ aSegmentIndex ];
        final PositionF p2 = myInputPositions[ aSegmentIndex + 1 ];
        final PositionF t1 = myIncomingTangents[ aSegmentIndex ];
        final PositionF t2 = myOutgoingTangents[ aSegmentIndex + 1 ];

        final float x1 = ( h1 * p1.x );
        final float x2 = ( h2 * p2.x );
        final float x3 = ( h3 * t1.x );
        final float x4 = ( h4 * t2.x );
        final float y1 = ( h1 * p1.y );
        final float y2 = ( h2 * p2.y );
        final float y3 = ( h3 * t1.y );
        final float y4 = ( h4 * t2.y );

        myTempPosition.x = x1 + x2 + x3 + x4;
        myTempPosition.y = y1 + y2 + y3 + y4;
        return myTempPosition;
        }

    public final PositionF getDirection( final int aSegmentIndex, final float aSegmentPos )
        {
        //#if DEBUG
        if ( aSegmentPos < 0 || aSegmentPos > 1f )
            throw new IllegalArgumentException();
        //#endif

        if ( aSegmentPos < 0.5f )
            {
            myTempDir1.setTo( getPosition( aSegmentIndex, aSegmentPos ) );
            myTempDir2.setTo( getPosition( aSegmentIndex, aSegmentPos + 0.25f ) );
            }
        else
            {
            myTempDir1.setTo( getPosition( aSegmentIndex, aSegmentPos - 0.25f ) );
            myTempDir2.setTo( getPosition( aSegmentIndex, aSegmentPos ) );
            }

        final float dx = myTempDir2.x - myTempDir1.x;
        final float dy = myTempDir2.y - myTempDir1.y;
        final float length = MathExtended.length( dx, dy );
        myTempDirection.x = ( dx / length ) * 25;
        myTempDirection.y = ( dy / length ) * 25;
        return myTempDirection;
        }

    // Implementation

    private PositionF calcIncomingTangent( final int aSegmentIndex )
        {
        if ( myInputSize < 2 ) throw new IllegalStateException();

        if ( aSegmentIndex > 0 && aSegmentIndex < myInputSize - 1 )
            {
            return calcIncomingKochanekBartelsTangent( aSegmentIndex );
            }
        else if ( aSegmentIndex == 0 )
            {
            return calcIncomingCardinalTangent( aSegmentIndex );
            }
        else if ( aSegmentIndex == myInputSize - 1 )
                {
                return calcIncomingCardinalTangent( myInputSize - 2 );
                }
            else
                {
                throw new IllegalArgumentException();
                }
        }

    private PositionF calcOutgoingTangent( final int aSegmentIndex )
        {
        if ( myInputSize < 2 ) throw new IllegalStateException();

        if ( aSegmentIndex > 0 && aSegmentIndex < myInputSize - 1 )
            {
            return calcOutgoingKochanekBartelsTangent( aSegmentIndex );
            }
        else if ( aSegmentIndex == 0 )
            {
            return calcOutgoingCardinalTangent( aSegmentIndex );
            }
        else if ( aSegmentIndex == myInputSize - 1 )
                {
                return calcOutgoingCardinalTangent( myInputSize - 2 );
                }
            else
                {
                throw new IllegalArgumentException();
                }
        }

    private PositionF calcIncomingKochanekBartelsTangent( final int aSegmentIndex )
        {
        final float ts1 = ( tMinus * ( cMinus * bPlus ) ) / 2;
        final float ts2 = ( tMinus * ( cPlus * bMinus ) ) / 2;
        return calcKochanekBartelsTangent( aSegmentIndex, ts1, ts2 );
        }

    private PositionF calcOutgoingKochanekBartelsTangent( final int aSegmentIndex )
        {
        final float ts1 = ( tMinus * ( cPlus * bPlus ) ) / 2;
        final float ts2 = ( tMinus * ( cMinus * bMinus ) ) / 2;
        return calcKochanekBartelsTangent( aSegmentIndex, ts1, ts2 );
        }

    private PositionF calcKochanekBartelsTangent( final int aSegmentIndex, final float aTangent1, final float aTangent2 )
        {
        final PositionF before = getInputPosition( aSegmentIndex - 1 );
        final PositionF around = getInputPosition( aSegmentIndex );
        final PositionF after = getInputPosition( aSegmentIndex + 1 );

        final float x1 = ( aTangent1* ( around.x - before.x ) );
        final float x2 = ( aTangent2* ( after.x - around.x ) );
        final float y1 = ( aTangent1* ( around.y - before.y ) );
        final float y2 = ( aTangent2* ( after.y - around.y ) );

        myTempDirection.x = x1 + x2;
        myTempDirection.y = y1 + y2;
        //#if DEBUG
        if ( myTempDirection.x == 0 && myTempDirection.y == 0 ) throw new IllegalStateException();
        //#endif
        return myTempDirection;
        }

    private PositionF calcIncomingCardinalTangent( final int aSegmentIndex )
        {
        final PositionF from = getInputPosition( aSegmentIndex );
        final PositionF to = getInputPosition( aSegmentIndex + 1 );

        myTempDirection.x = ( cardinalFactorOutgoing * ( to.x - from.x ) );
        myTempDirection.y = ( cardinalFactorOutgoing * ( to.y - from.y ) );
        //#if DEBUG
        if ( myTempDirection.x == 0 && myTempDirection.y == 0 ) throw new IllegalStateException();
        //#endif
        return myTempDirection;
        }

    private PositionF calcOutgoingCardinalTangent( final int aSegmentIndex )
        {
        final PositionF from = getInputPosition( aSegmentIndex );
        final PositionF to = getInputPosition( aSegmentIndex + 1 );

        myTempDirection.x = ( cardinalFactorOutgoing * ( to.x - from.x ) );
        myTempDirection.y = ( cardinalFactorOutgoing * ( to.y - from.y ) );
        //#if DEBUG
        if ( myTempDirection.x == 0 && myTempDirection.y == 0 ) throw new IllegalStateException();
        //#endif
        return myTempDirection;
        }


    private int myInputSize;

    private final PositionF myTempDir1 = new PositionF();

    private final PositionF myTempDir2 = new PositionF();

    private final PositionF myTempDirection = new PositionF();

    private final PositionF myTempPosition = new PositionF();

    private final PositionF[] myInputPositions = new PositionF[MAX_INPUT_POINTS];

    private final PositionF[] myIncomingTangents = new PositionF[MAX_INPUT_POINTS];

    private final PositionF[] myOutgoingTangents = new PositionF[MAX_INPUT_POINTS];


    private static final float kbTension = 1f / 1000;

    private static final float kbContinuity = 1f / 1000;

    private static final float kbBias = 1f / 10;

    private static final float tMinus = 1f - kbTension;

    private static final float cMinus = 1f - kbContinuity;

    private static final float cPlus = 1f + kbContinuity;

    private static final float bMinus = 1f - kbBias;

    private static final float bPlus = 1f + kbBias;

    private static final float cardinalFactorOutgoing = 0.5f;
    }
