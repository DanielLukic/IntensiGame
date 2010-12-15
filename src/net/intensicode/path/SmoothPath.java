package net.intensicode.path;

import net.intensicode.util.*;

public class SmoothPath implements PathWithDirection, PositionList
    {
    public final void close()
        {
        myInterpolation.close();
        end();
        }

    // From PathWithDirection

    public final PositionF getStartPosition()
        {
        return myPositions.getPosition( 0 );
        }

    public final PositionF getStartDirection()
        {
        return getDirection( 0 );
        }

    public final PositionF getEndPosition()
        {
        return myPositions.getPosition( getPathLength() );
        }

    public final PositionF getEndDirection()
        {
        return myPositions.getDirection( getPathLength() );
        }

    public final PathWithDirection createConnectedPathTo( final PositionF aTargetPosition, final float aStepSize )
        {
        final SmoothPath newPath = new SmoothPath();

        final PositionF from = new PositionF();
        from.setTo( getEndPosition() );

        final PositionF before = getPosition( getPathLength() - aStepSize );

        final PositionF step = new PositionF();
        step.x = from.x - before.x;
        step.y = from.y - before.y;

        int connectStep = 0;

        final PositionF temp = new PositionF();
        while ( from.distanceTo( aTargetPosition ) > 5 )
            {
            newPath.add( from );

            temp.x = aTargetPosition.x - from.x;
            temp.y = aTargetPosition.y - from.y;

            final float b = ( connectStep ) / CONNECTING_STEPS;
            final float a = 1f - b;

            temp.x = ( step.x * a ) + ( temp.x * b );
            temp.y = ( step.y * a ) + ( temp.y * b );

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

    public final PositionList add( final PositionF aWorldPosition )
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
                final float pos = ( step ) / STEPS_PER_SEGMENT;
                final PositionF direction = myInterpolation.getDirection( segment, pos );
                final PositionF position = myInterpolation.getPosition( segment, pos );
                myTempPositions.add( position, direction );
                }
            }
        myTempPositions.end();

        final float pathLength = myTempPositions.getPathLength();
        for ( int idx = 0; idx <= PATH_STEPS; idx++ )
            {
            final float pos = idx * pathLength / PATH_STEPS;
            final PositionF direction = myTempPositions.getDirection( pos );
            final PositionF position = myTempPositions.getPosition( pos );
            myPositions.add( position, direction );
            }
        myPositions.end();
        }

    // From Path

    public final float getPathLength()
        {
        return myPositions.getPathLength();
        }

    public final PositionF getPosition( final float aPathPos )
        {
        return myPositions.getPosition( aPathPos );
        }

    public final PositionF getDirection( final float aPathPos )
        {
        return myPositions.getDirection( aPathPos );
        }



    private final LinearPath myPositions = new LinearPath();

    private final LinearPath myTempPositions = new LinearPath();

    private final CatmullRomInterpolation myInterpolation = new CatmullRomInterpolation();


    private static final float PATH_STEPS = 48;

    private static final float CONNECTING_STEPS = 12;

    private static final float STEPS_PER_SEGMENT = 5f;
    }
