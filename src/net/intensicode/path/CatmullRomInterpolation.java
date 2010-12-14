package net.intensicode.path;

import net.intensicode.util.*;


public final class CatmullRomInterpolation implements Interpolation, PositionList
    {
    public static final int MAX_INPUT_POINTS = 32;



    public CatmullRomInterpolation()
        {
        for ( int idx = 0; idx < MAX_INPUT_POINTS; idx++ )
            {
            myInputPositions[ idx ] = new PositionF();
            }
        }

    public final void close()
        {
        final PositionF start = myInputPositions[ 0 ];
        final PositionF end = myInputPositions[ myInputSize - 1 ];
        if ( start.distanceTo( end ) > 0.5f ) add( start );
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

    public final PositionList add( final PositionF aPosition )
        {
        myInputPositions[ myInputSize++ ].setTo( aPosition );
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

    public final PositionF getInputPosition( final int aIndex )
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

    public final PositionF getPosition( final int aSegmentIndex, final float aSegmentPos )
        {
        //#if DEBUG
        if ( aSegmentPos < 0 || aSegmentPos > 1 )
            {
            throw new IllegalArgumentException();
            }
        //#endif

        final float s = aSegmentPos;
        final float s2 = s * s;
        final float s3 = s * s2;

        final float v1 = s3 / 2;
        final float v2 = s / 2;
        final float v3 = 3 * s3 / 2;

        final float h1 = -v1 + s2 - v2;
        final float h2 = v3 - 5 * s2 / 2 + 1;
        final float h3 = -v3 + 2 * s2 + v2;
        final float h4 = v1 - s2 / 2;

        final PositionF p1 = getInputPosition( aSegmentIndex - 1 );
        final PositionF p2 = getInputPosition( aSegmentIndex + 0 );
        final PositionF p3 = getInputPosition( aSegmentIndex + 1 );
        final PositionF p4 = getInputPosition( aSegmentIndex + 2 );

        final float x1 = ( h1* p1.x );
        final float x2 = ( h2* p2.x );
        final float x3 = ( h3* p3.x );
        final float x4 = ( h4* p4.x );
        final float y1 = ( h1* p1.y );
        final float y2 = ( h2* p2.y );
        final float y3 = ( h3* p3.y );
        final float y4 = ( h4* p4.y );

        myTempPosition.x = x1 + x2 + x3 + x4;
        myTempPosition.y = y1 + y2 + y3 + y4;
        return myTempPosition;
        }

    public final PositionF getDirection( final int aSegmentIndex, final float aSegmentPos )
        {
        //#if DEBUG
        if ( aSegmentPos < 0 || aSegmentPos > 1 )
            {
            throw new IllegalArgumentException();
            }
        //#endif

        if ( aSegmentPos < 0.5 )
            {
            myTempDir1.setTo( getPosition( aSegmentIndex - 1, aSegmentPos + 0.5f ) );
            myTempDir3.setTo( getPosition( aSegmentIndex, aSegmentPos + 0.5f ) );
            }
        else
            {
            myTempDir1.setTo( getPosition( aSegmentIndex, aSegmentPos - 0.5f ) );
            myTempDir3.setTo( getPosition( aSegmentIndex + 1, aSegmentPos - 0.5f ) );
            }
        myTempDir2.setTo( getPosition( aSegmentIndex, aSegmentPos ) );

        final float dx1 = myTempDir2.x - myTempDir1.x;
        final float dy1 = myTempDir2.y - myTempDir1.y;
        final float dx2 = myTempDir3.x - myTempDir2.x;
        final float dy2 = myTempDir3.y - myTempDir2.y;

        myTempDirection.x = ( dx1 + dx2 ) / 5;
        myTempDirection.y = ( dy1 + dy2 ) / 5;
        return myTempDirection;
        }



    private int myInputSize;

    private boolean myClosedFlag;

    private final PositionF myTempDir1 = new PositionF();

    private final PositionF myTempDir2 = new PositionF();

    private final PositionF myTempDir3 = new PositionF();

    private final PositionF myTempPosition = new PositionF();

    private final PositionF myTempDirection = new PositionF();

    private final PositionF[] myInputPositions = new PositionF[MAX_INPUT_POINTS];
    }
