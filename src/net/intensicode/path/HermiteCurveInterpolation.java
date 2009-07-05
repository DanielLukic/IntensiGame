package net.intensicode.path;

import net.intensicode.util.FixedMath;
import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 * <p/>
 * http://www.cubic.org/docs/hermite.htm
 */
public final class HermiteCurveInterpolation implements Interpolation, PositionList
    {
    public static final int MAX_INPUT_POINTS = 32;



    public HermiteCurveInterpolation()
        {
        for ( int idx = 0; idx < MAX_INPUT_POINTS; idx++ )
            {
            myInputPositions[ idx ] = new Position();
            myIncomingTangents[ idx ] = new Position();
            myOutgoingTangents[ idx ] = new Position();
            }
        }
    
    // From PositionList

    public final void clear()
        {
        myInputSize = 0;
        }

    public final PositionList add( final Position aPositionFixed )
        {
        if ( myInputSize > 0 )
            {
            final Position previous = myInputPositions[ myInputSize - 1 ];
            //#if DEBUG
            if ( previous.x == aPositionFixed.x && previous.y == aPositionFixed.y )
                {
                throw new IllegalArgumentException();
                }
            //#endif
            if ( previous.x == aPositionFixed.x && previous.y == aPositionFixed.y ) return this;
            }
        myInputPositions[ myInputSize++ ].setTo( aPositionFixed );

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

    public final Position getInputPosition( final int aIdx )
        {
        return myInputPositions[ aIdx ];
        }

    public final Position getPosition( final int aSegmentIndex, final int aSegmentPosFixed )
        {
        //#if DEBUG
        if ( aSegmentPosFixed < 0 || aSegmentPosFixed > FixedMath.FIXED_1 )
            throw new IllegalArgumentException();
        //#endif

        final int s = aSegmentPosFixed;
        final int s2 = FixedMath.mul( s, s );
        final int s3 = FixedMath.mul( s, s2 );

        final int h1 = 2 * s3 - 3 * s2 + FixedMath.FIXED_1;
        final int h2 = -2 * s3 + 3 * s2;
        final int h3 = s3 - 2 * s2 + s;
        final int h4 = s3 - s2;

        final Position p1 = myInputPositions[ aSegmentIndex ];
        final Position p2 = myInputPositions[ aSegmentIndex + 1 ];
        final Position t1 = myIncomingTangents[ aSegmentIndex ];
        final Position t2 = myOutgoingTangents[ aSegmentIndex + 1 ];

        final int x1 = FixedMath.mul( h1, p1.x );
        final int x2 = FixedMath.mul( h2, p2.x );
        final int x3 = FixedMath.mul( h3, t1.x );
        final int x4 = FixedMath.mul( h4, t2.x );
        final int y1 = FixedMath.mul( h1, p1.y );
        final int y2 = FixedMath.mul( h2, p2.y );
        final int y3 = FixedMath.mul( h3, t1.y );
        final int y4 = FixedMath.mul( h4, t2.y );

        myTempPosition.x = x1 + x2 + x3 + x4;
        myTempPosition.y = y1 + y2 + y3 + y4;
        return myTempPosition;
        }

    public final Position getDirection( final int aSegmentIndex, final int aSegmentPosFixed )
        {
        //#if DEBUG
        if ( aSegmentPosFixed < 0 || aSegmentPosFixed > FixedMath.FIXED_1 )
            throw new IllegalArgumentException();
        //#endif

        if ( aSegmentPosFixed < FixedMath.FIXED_0_5 )
            {
            myTempDir1.setTo( getPosition( aSegmentIndex, aSegmentPosFixed ) );
            myTempDir2.setTo( getPosition( aSegmentIndex, aSegmentPosFixed + FixedMath.FIXED_0_25 ) );
            }
        else
            {
            myTempDir1.setTo( getPosition( aSegmentIndex, aSegmentPosFixed - FixedMath.FIXED_0_25 ) );
            myTempDir2.setTo( getPosition( aSegmentIndex, aSegmentPosFixed ) );
            }

        final int dx = myTempDir2.x - myTempDir1.x;
        final int dy = myTempDir2.y - myTempDir1.y;
        final int length = FixedMath.length( dx, dy );
        myTempDirection.x = FixedMath.div( dx, length ) * 25;
        myTempDirection.y = FixedMath.div( dy, length ) * 25;
        return myTempDirection;
        }

    // Implementation

    private final Position calcIncomingTangent( final int aSegmentIndex )
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

    private final Position calcOutgoingTangent( final int aSegmentIndex )
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

    private final Position calcIncomingKochanekBartelsTangent( final int aSegmentIndex )
        {
        final int ts1 = FixedMath.mul( tMinus, FixedMath.mul( cMinus, bPlus ) ) / 2;
        final int ts2 = FixedMath.mul( tMinus, FixedMath.mul( cPlus, bMinus ) ) / 2;
        return calcKochanekBartelsTangent( aSegmentIndex, ts1, ts2 );
        }

    private final Position calcOutgoingKochanekBartelsTangent( final int aSegmentIndex )
        {
        final int ts1 = FixedMath.mul( tMinus, FixedMath.mul( cPlus, bPlus ) ) / 2;
        final int ts2 = FixedMath.mul( tMinus, FixedMath.mul( cMinus, bMinus ) ) / 2;
        return calcKochanekBartelsTangent( aSegmentIndex, ts1, ts2 );
        }

    private final Position calcKochanekBartelsTangent( final int aSegmentIndex, final int aTangent1, final int aTangent2 )
        {
        final Position before = getInputPosition( aSegmentIndex - 1 );
        final Position around = getInputPosition( aSegmentIndex );
        final Position after = getInputPosition( aSegmentIndex + 1 );

        final int x1 = FixedMath.mul( aTangent1, ( around.x - before.x ) );
        final int x2 = FixedMath.mul( aTangent2, ( after.x - around.x ) );
        final int y1 = FixedMath.mul( aTangent1, ( around.y - before.y ) );
        final int y2 = FixedMath.mul( aTangent2, ( after.y - around.y ) );

        myTempDirection.x = x1 + x2;
        myTempDirection.y = y1 + y2;
        //#if DEBUG
        if ( myTempDirection.x == 0 && myTempDirection.y == 0 ) throw new IllegalStateException();
        //#endif
        return myTempDirection;
        }

    private final Position calcIncomingCardinalTangent( final int aSegmentIndex )
        {
        final Position from = getInputPosition( aSegmentIndex );
        final Position to = getInputPosition( aSegmentIndex + 1 );

        myTempDirection.x = FixedMath.mul( cardinalFactorOutgoingFixed, ( to.x - from.x ) );
        myTempDirection.y = FixedMath.mul( cardinalFactorOutgoingFixed, ( to.y - from.y ) );
        //#if DEBUG
        if ( myTempDirection.x == 0 && myTempDirection.y == 0 ) throw new IllegalStateException();
        //#endif
        return myTempDirection;
        }

    private final Position calcOutgoingCardinalTangent( final int aSegmentIndex )
        {
        final Position from = getInputPosition( aSegmentIndex );
        final Position to = getInputPosition( aSegmentIndex + 1 );

        myTempDirection.x = FixedMath.mul( cardinalFactorOutgoingFixed, ( to.x - from.x ) );
        myTempDirection.y = FixedMath.mul( cardinalFactorOutgoingFixed, ( to.y - from.y ) );
        //#if DEBUG
        if ( myTempDirection.x == 0 && myTempDirection.y == 0 ) throw new IllegalStateException();
        //#endif
        return myTempDirection;
        }



    private int myInputSize;

    private final Position myTempDir1 = new Position();

    private final Position myTempDir2 = new Position();

    private final Position myTempDirection = new Position();

    private final Position myTempPosition = new Position();

    private final Position[] myInputPositions = new Position[MAX_INPUT_POINTS];

    private final Position[] myIncomingTangents = new Position[MAX_INPUT_POINTS];

    private final Position[] myOutgoingTangents = new Position[MAX_INPUT_POINTS];


    private static final int kbTensionFixed = FixedMath.FIXED_1 / 1000;

    private static final int kbContinuityFixed = FixedMath.FIXED_1 / 1000;

    private static final int kbBiasFixed = FixedMath.FIXED_1 / 10;

    private static final int tMinus = FixedMath.FIXED_1 - kbTensionFixed;

    private static final int cMinus = FixedMath.FIXED_1 - kbContinuityFixed;

    private static final int cPlus = FixedMath.FIXED_1 + kbContinuityFixed;

    private static final int bMinus = FixedMath.FIXED_1 - kbBiasFixed;

    private static final int bPlus = FixedMath.FIXED_1 + kbBiasFixed;

    private static final int cardinalFactorOutgoingFixed = FixedMath.FIXED_0_5;
    }
