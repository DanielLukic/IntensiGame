package net.intensicode.path;

import net.intensicode.util.*;


public final class LinearPath implements Path, PositionList
    {
    public LinearPath()
        {
        }

    public final PositionList add( final PositionF aPosition, final PositionF aDirection )
        {
        myInputPositions.add( new DirectedPosition( aPosition, aDirection ) );
        return this;
        }

    // From PositionList

    public final void clear()
        {
        myAccumulatedSegmentLengths = null;
        myInputPositions.clear();
        myPathLength = 0;
        }

    public final PositionList add( final PositionF aPosition )
        {
        final DirectedPosition previousPosition = (DirectedPosition) myInputPositions.last();
        myInputPositions.add( new DirectedPosition( aPosition ) );

        if ( myInputPositions.size > 1 )
            {
            final PositionF previousDirection = previousPosition.direction;
            if ( previousDirection.validDirection() == false )
                {
                previousDirection.x = aPosition.x - previousPosition.position.x;
                previousDirection.y = aPosition.y - previousPosition.position.y;
                }
            }
        return this;
        }

    public final void end()
        {
        myPathLength = 0;
        myAccumulatedSegmentLengths = new int[myInputPositions.size];

        final Object[] positions = myInputPositions.objects;
        for ( int idx = 1; idx < myAccumulatedSegmentLengths.length; idx++ )
            {
            final DirectedPosition before = (DirectedPosition) positions[ idx - 1 ];
            final DirectedPosition after = (DirectedPosition) positions[ idx ];
            myPathLength += length( before.position, after.position );

            myAccumulatedSegmentLengths[ idx ] = myPathLength;
            }
        }

    // From Path

    public final int getPathLength()
        {
        return myPathLength;
        }

    public final PositionF getPosition( final float aPathPos )
        {
        for ( int idx = 1; idx < myInputPositions.size; idx++ )
            {
            final int pathLengthSoFar = myAccumulatedSegmentLengths[ idx ];
            if ( aPathPos >= pathLengthSoFar ) continue;

            final float pathLengthBefore = myAccumulatedSegmentLengths[ idx - 1 ];
            final float segmentLength = pathLengthSoFar - pathLengthBefore;
            final float segmentPos = aPathPos - pathLengthBefore;
            final float relativePos = segmentPos / segmentLength;
            return getInterpolatedPosition( idx, relativePos );
            }

        return getInputPosition( myInputPositions.size - 1 ).position;
        }

    public final PositionF getDirection( final float aPathPos )
        {
        for ( int idx = 1; idx < myInputPositions.size; idx++ )
            {
            final int pathLengthSoFar = myAccumulatedSegmentLengths[ idx ];
            if ( aPathPos >= pathLengthSoFar ) continue;

            final float pathLengthBefore = myAccumulatedSegmentLengths[ idx - 1 ];
            final float segmentLength = pathLengthSoFar - pathLengthBefore;
            final float segmentPos = aPathPos - pathLengthBefore;
            final float relativePos = segmentPos / segmentLength;
            return getInterpolatedDirection( idx, relativePos );
            }

        return getInterpolatedDirection( myInputPositions.size - 2, 1 );
        }

    // Implementation

    private final PositionF getInterpolatedPosition( final int aSegmentIndex, final float aSegmentPosBetween1_0 )
        {
        //#if DEBUG
        if ( aSegmentPosBetween1_0 < 0 ) throw new IllegalArgumentException();
        if ( aSegmentPosBetween1_0 > 1 ) throw new IllegalArgumentException();
        //#endif

        final DirectedPosition before = (DirectedPosition) myInputPositions.objects[ aSegmentIndex - 1 ];
        final DirectedPosition after = (DirectedPosition) myInputPositions.objects[ aSegmentIndex ];

        final float b = aSegmentPosBetween1_0;
        final float a = 1f - b;

        final float x1 = ( before.position.x * a );
        final float y1 = ( before.position.y * a );
        final float x2 = ( after.position.x * b );
        final float y2 = ( after.position.y * b );

        myTempPosition.x = x1 + x2;
        myTempPosition.y = y1 + y2;
        return myTempPosition;
        }

    private PositionF getInterpolatedDirection( final int aSegmentIndex, final float aSegmentPosBetween1_0 )
        {
        final DirectedPosition before = (DirectedPosition) myInputPositions.objects[ aSegmentIndex - 1 ];
        final DirectedPosition after = (DirectedPosition) myInputPositions.objects[ aSegmentIndex ];

        final float dx = after.direction.x - before.direction.x;
        final float dy = after.direction.y - before.direction.y;

        myTempDirection.x = before.direction.x + ( dx * aSegmentPosBetween1_0 );
        myTempDirection.y = before.direction.y + ( dy * aSegmentPosBetween1_0 );
        return myTempDirection;
        }

    private final float length( final PositionF aFromPos, final PositionF aToPos )
        {
        final float dx = aToPos.x - aFromPos.x;
        final float dy = aToPos.y - aFromPos.y;
        return MathExtended.length( dx, dy );
        }

    private DirectedPosition getInputPosition( final int aLastIndex )
        {
        return (DirectedPosition) myInputPositions.objects[ aLastIndex ];
        }


    private int myPathLength;

    private int[] myAccumulatedSegmentLengths;


    private final PositionF myTempPosition = new PositionF();

    private final PositionF myTempDirection = new PositionF();

    private final DynamicArray myInputPositions = new DynamicArray();
    }
