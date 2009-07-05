/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.path;

import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public final class InterpolatedPath implements Path
    {
    public InterpolatedPath( final Interpolation aInterpolation )
        {
        myInterpolation = aInterpolation;

        final int size = myInterpolation.getNumberOfInputPositions();

        myAccumulatedSegmentLengths = new int[size];

        for ( int idx = 1; idx < size; idx++ )
            {
            final Position from = myInterpolation.getInputPosition( idx - 1 );
            final Position to = myInterpolation.getInputPosition( idx );
            myPathLength += from.distanceToFixed( to );
            myAccumulatedSegmentLengths[ idx ] = myPathLength;
            }
        }

    // From Path

    public final int getPathLength()
        {
        return myPathLength;
        }

    public final Position getPosition( final int aPathPosFixed )
        {
        //for ( int idx = 1; idx < myInputPositions.size; idx++ )
        //    {
        //    final int pathLengthSoFar = myAccumulatedSegmentLengths[ idx ];
        //    if ( aPathPosFixed >= pathLengthSoFar ) continue;
        //
        //    final int pathLengthBefore = myAccumulatedSegmentLengths[ idx - 1 ];
        //    final int segmentLength = pathLengthSoFar - pathLengthBefore;
        //    final int segmentPos = aPathPosFixed - pathLengthBefore;
        //    final int relativePos = FixedMath.div( segmentPos, segmentLength );
        //    return getInterpolatedPosition( idx, relativePos );
        //    }
        //
        //return getInputPosition( myInputPositions.size - 1 ).positionFixed;
        return null;
        }

    public final Position getDirection( final int aPathPosFixed )
        {
        //for ( int idx = 1; idx < myInputPositions.size; idx++ )
        //    {
        //    final int pathLengthSoFar = myAccumulatedSegmentLengths[ idx ];
        //    if ( aPathPosFixed >= pathLengthSoFar ) continue;
        //
        //    final int pathLengthBefore = myAccumulatedSegmentLengths[ idx - 1 ];
        //    final int segmentLength = pathLengthSoFar - pathLengthBefore;
        //    final int segmentPos = aPathPosFixed - pathLengthBefore;
        //    final int relativePos = FixedMath.div( segmentPos, segmentLength );
        //    return getInterpolatedDirection( idx, relativePos );
        //    }
        //
        //return getInterpolatedDirection( myInputPositions.size - 2, FixedMath.FIXED_1 );
        return null;
        }

    // Implementation

    //private final Position getInterpolatedPosition( final int aSegmentIndex, final int aSegmentPosBetween1_0 )
    //    {
    //    //#if DEBUG
    //    if ( aSegmentPosBetween1_0 < 0 ) throw new IllegalArgumentException();
    //    if ( aSegmentPosBetween1_0 > FixedMath.FIXED_1 ) throw new IllegalArgumentException();
    //    //#endif
    //
    //    final DirectedPosition before = (DirectedPosition) myInputPositions.objects[ aSegmentIndex - 1 ];
    //    final DirectedPosition after = (DirectedPosition) myInputPositions.objects[ aSegmentIndex ];
    //
    //    final int b = aSegmentPosBetween1_0;
    //    final int a = FixedMath.FIXED_1 - b;
    //
    //    final int x1 = FixedMath.mul( before.positionFixed.x, a );
    //    final int y1 = FixedMath.mul( before.positionFixed.y, a );
    //    final int x2 = FixedMath.mul( after.positionFixed.x, b );
    //    final int y2 = FixedMath.mul( after.positionFixed.y, b );
    //
    //    myTempPosition.x = x1 + x2;
    //    myTempPosition.y = y1 + y2;
    //    return myTempPosition;
    //    }
    //
    //private Position getInterpolatedDirection( final int aSegmentIndex, final int aSegmentPosBetween1_0 )
    //    {
    //    final DirectedPosition before = (DirectedPosition) myInputPositions.objects[ aSegmentIndex - 1 ];
    //    final DirectedPosition after = (DirectedPosition) myInputPositions.objects[ aSegmentIndex ];
    //
    //    final int dx = after.directionFixed.x - before.directionFixed.x;
    //    final int dy = after.directionFixed.y - before.directionFixed.y;
    //
    //    myTempDirection.x = before.directionFixed.x + FixedMath.mul( dx, aSegmentPosBetween1_0 );
    //    myTempDirection.y = before.directionFixed.y + FixedMath.mul( dy, aSegmentPosBetween1_0 );
    //    return myTempDirection;
    //    }
    //
    //private final int length( final Position aFromPos, final Position aToPos )
    //    {
    //    final int dx = aToPos.x - aFromPos.x;
    //    final int dy = aToPos.y - aFromPos.y;
    //    return FixedMath.length( dx, dy );
    //    }
    //
    //private DirectedPosition getInputPosition( final int aLastIndex )
    //    {
    //    return (DirectedPosition) myInputPositions.objects[ aLastIndex ];
    //    }



    private int myPathLength;

    private int[] myAccumulatedSegmentLengths;


    private final Position myTempPosition = new Position();

    private final Position myTempDirection = new Position();

    private final Interpolation myInterpolation;
    }