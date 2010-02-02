package net.intensicode.path;

import net.intensicode.util.FixedMath;
import net.intensicode.util.Position;

public class SmoothPath implements PathWithDirection, PositionList
    {
    public final void close()
        {
        myInterpolation.close();
        end();
        }

    // From PathWithDirection

    public final Position getStartPosition()
        {
        return myPositions.getPosition( 0 );
        }

    public final Position getStartDirection()
        {
        return getDirection( 0 );
        }

    public final Position getEndPosition()
        {
        return myPositions.getPosition( getPathLength() );
        }

    public final Position getEndDirection()
        {
        return myPositions.getDirection( getPathLength() );
        }

    public final PathWithDirection createConnectedPathTo( final Position aTargetPosition, final int aStepSizeFixed )
        {
        final SmoothPath newPath = new SmoothPath();

        final Position from = new Position();
        from.setTo( getEndPosition() );

        final Position before = getPosition( getPathLength() - aStepSizeFixed );

        final Position step = new Position();
        step.x = from.x - before.x;
        step.y = from.y - before.y;

        int connectStep = 0;

        final Position temp = new Position();
        while ( from.distanceToFixed( aTargetPosition ) > FixedMath.FIXED_5 )
            {
            newPath.add( from );

            temp.x = aTargetPosition.x - from.x;
            temp.y = aTargetPosition.y - from.y;

            final int b = FixedMath.toFixed( connectStep ) / CONNECTING_STEPS;
            final int a = FixedMath.FIXED_1 - b;

            temp.x = FixedMath.mul( step.x, a ) + FixedMath.mul( temp.x, b );
            temp.y = FixedMath.mul( step.y, a ) + FixedMath.mul( temp.y, b );

            from.translate( temp );

            if ( connectStep < CONNECTING_STEPS ) connectStep++;
            }

        newPath.add( aTargetPosition );
        newPath.end();

        return newPath;
        }

    // From PositionList

    public final void clear()
        {
        myInterpolation.clear();
        myTempPositions.clear();
        myPositions.clear();
        }

    public final PositionList add( final Position aWorldPosition )
        {
        myInterpolation.add( aWorldPosition );
        return this;
        }

    public final void end()
        {
        final int numberOfSegments = myInterpolation.getNumberOfInputPositions() - 1;
        for ( int segment = 0; segment < numberOfSegments; segment++ )
            {
            for ( int step = 0; step <= STEPS_PER_SEGMENT; step++ )
                {
                final int posFixed = FixedMath.toFixed( step ) / STEPS_PER_SEGMENT;
                final Position direction = myInterpolation.getDirection( segment, posFixed );
                final Position position = myInterpolation.getPosition( segment, posFixed );
                myTempPositions.add( position, direction );
                }
            }
        myTempPositions.end();

        final int pathLength = myTempPositions.getPathLength();
        for ( int idx = 0; idx <= PATH_STEPS; idx++ )
            {
            final int posFixed = idx * pathLength / PATH_STEPS;
            final Position direction = myTempPositions.getDirection( posFixed );
            final Position position = myTempPositions.getPosition( posFixed );
            myPositions.add( position, direction );
            }
        myPositions.end();
        }

    // From Path

    public final int getPathLength()
        {
        return myPositions.getPathLength();
        }

    public final Position getPosition( final int aPathPosFixed )
        {
        return myPositions.getPosition( aPathPosFixed );
        }

    public final Position getDirection( final int aPathPosFixed )
        {
        return myPositions.getDirection( aPathPosFixed );
        }



    private final LinearPath myPositions = new LinearPath();

    private final LinearPath myTempPositions = new LinearPath();

    private final CatmullRomInterpolation myInterpolation = new CatmullRomInterpolation();


    private static final int PATH_STEPS = 48;

    private static final int CONNECTING_STEPS = 12;

    private static final int STEPS_PER_SEGMENT = 5;
    }
