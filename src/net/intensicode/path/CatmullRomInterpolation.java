/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.path;

import net.intensicode.util.FixedMath;
import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public final class CatmullRomInterpolation implements Interpolation, PositionList
    {
    public static final int MAX_INPUT_POINTS = 32;



    public CatmullRomInterpolation()
        {
        for ( int idx = 0; idx < MAX_INPUT_POINTS; idx++ )
            {
            myInputPositions[ idx ] = new Position();
            }
        }

    public final void close()
        {
        final Position start = myInputPositions[ 0 ];
        final Position end = myInputPositions[ myInputSize - 1 ];
        if ( start.distanceToFixed( end ) > FixedMath.FIXED_0_5 ) add( start );
        myClosedFlag = true;
        }

    public final boolean isClosed()
        {
        return myClosedFlag;
        }

    // From PositionList

    public final void clear()
        {
        myInputSize = 0;
        }

    public final PositionList add( final Position aPositionFixed )
        {
        myInputPositions[ myInputSize++ ].setTo( aPositionFixed );
        return this;
        }

    public final void end()
        {
        }

    // From Interpolation

    public final int getNumberOfInputPositions()
        {
        return myInputSize;
        }

    public final Position getInputPosition( final int aIndex )
        {
        if ( myClosedFlag )
            {
            final int realSize = myInputSize - 1;
            final int index = ( aIndex % realSize );
            final int goodIndex = index < 0 ? index + realSize : index;
            return myInputPositions[ goodIndex ];
            }

        if ( aIndex < 0 ) return myInputPositions[ 0 ];
        else if ( aIndex >= myInputSize ) return myInputPositions[ myInputSize - 1 ];
        return myInputPositions[ aIndex ];
        }

    public final Position getPosition( final int aSegmentIndex, final int aSegmentPosFixed )
        {
        //#if DEBUG
        if ( aSegmentPosFixed < 0 || aSegmentPosFixed > FixedMath.FIXED_1 )
            {
            throw new IllegalArgumentException();
            }
        //#endif

        final int s = aSegmentPosFixed;
        final int s2 = FixedMath.mul( s, s );
        final int s3 = FixedMath.mul( s, s2 );

        final int v1 = s3 / 2;
        final int v2 = s / 2;
        final int v3 = 3 * s3 / 2;

        final int h1 = -v1 + s2 - v2;
        final int h2 = v3 - 5 * s2 / 2 + FixedMath.FIXED_1;
        final int h3 = -v3 + 2 * s2 + v2;
        final int h4 = v1 - s2 / 2;

        final Position p1 = getInputPosition( aSegmentIndex - 1 );
        final Position p2 = getInputPosition( aSegmentIndex + 0 );
        final Position p3 = getInputPosition( aSegmentIndex + 1 );
        final Position p4 = getInputPosition( aSegmentIndex + 2 );

        final int x1 = FixedMath.mul( h1, p1.x );
        final int x2 = FixedMath.mul( h2, p2.x );
        final int x3 = FixedMath.mul( h3, p3.x );
        final int x4 = FixedMath.mul( h4, p4.x );
        final int y1 = FixedMath.mul( h1, p1.y );
        final int y2 = FixedMath.mul( h2, p2.y );
        final int y3 = FixedMath.mul( h3, p3.y );
        final int y4 = FixedMath.mul( h4, p4.y );

        myTempPosition.x = x1 + x2 + x3 + x4;
        myTempPosition.y = y1 + y2 + y3 + y4;
        return myTempPosition;
        }

    public final Position getDirection( final int aSegmentIndex, final int aSegmentPosFixed )
        {
        //#if DEBUG
        if ( aSegmentPosFixed < 0 || aSegmentPosFixed > FixedMath.FIXED_1 )
            {
            throw new IllegalArgumentException();
            }
        //#endif

        if ( aSegmentPosFixed < FixedMath.FIXED_0_5 )
            {
            myTempDir1.setTo( getPosition( aSegmentIndex - 1, aSegmentPosFixed + FixedMath.FIXED_0_5 ) );
            myTempDir3.setTo( getPosition( aSegmentIndex, aSegmentPosFixed + FixedMath.FIXED_0_5 ) );
            }
        else
            {
            myTempDir1.setTo( getPosition( aSegmentIndex, aSegmentPosFixed - FixedMath.FIXED_0_5 ) );
            myTempDir3.setTo( getPosition( aSegmentIndex + 1, aSegmentPosFixed - FixedMath.FIXED_0_5 ) );
            }
        myTempDir2.setTo( getPosition( aSegmentIndex, aSegmentPosFixed ) );

        final int dx1 = myTempDir2.x - myTempDir1.x;
        final int dy1 = myTempDir2.y - myTempDir1.y;
        final int dx2 = myTempDir3.x - myTempDir2.x;
        final int dy2 = myTempDir3.y - myTempDir2.y;

        myTempDirection.x = ( dx1 + dx2 ) / 5;
        myTempDirection.y = ( dy1 + dy2 ) / 5;
        return myTempDirection;
        }



    private int myInputSize;

    private boolean myClosedFlag;

    private final Position myTempDir1 = new Position();

    private final Position myTempDir2 = new Position();

    private final Position myTempDir3 = new Position();

    private final Position myTempPosition = new Position();

    private final Position myTempDirection = new Position();

    private final Position[] myInputPositions = new Position[MAX_INPUT_POINTS];
    }
