//#condition RENDER_ASYNC && RENDER_STATS

package net.intensicode.graphics;

import net.intensicode.PlatformContext;
import net.intensicode.util.Log;

public final class AsyncRenderStatsEntry
    {
    public AsyncRenderStatsEntry( final PlatformContext aPlatformContext, final int aId )
        {
        myPlatformContext = aPlatformContext;
        myId = aId;
        myMinDelta = Long.MAX_VALUE;
        myMaxDelta = 0;
        }

    public final void start()
        {
        myStartTimestamp = myPlatformContext.compatibleTimeInMillis();
        }

    public final void end()
        {
        final long endTimestamp = myPlatformContext.compatibleTimeInMillis();
        final long delta = endTimestamp - myStartTimestamp;
        myAccumulatedTimeInMillis += delta;
        myDataCount++;
        myMinDelta = Math.min( myMinDelta, delta );
        myMaxDelta = Math.max( myMaxDelta, delta );
        }

    public final void fail( final Exception aException )
        {
        // TODO: Track TOP5 exceptions?
        myLastExceptionOrNull = aException;
        }

    public final void dump()
        {
        if ( myDataCount == 0 ) return;

        final double average = myAccumulatedTimeInMillis * 1.0 / myDataCount;
        final long median = ( myMaxDelta + myMinDelta ) / 2;

        final StringBuffer buffer = new StringBuffer();
        buffer.append( "id: " );
        buffer.append( myId );
        buffer.append( " acc: " );
        buffer.append( myAccumulatedTimeInMillis );
        buffer.append( " count: " );
        buffer.append( myDataCount );
        buffer.append( " min: " );
        buffer.append( myMinDelta );
        buffer.append( " max: " );
        buffer.append( myMaxDelta );
        buffer.append( " avg: " );
        buffer.append( average );
        buffer.append( " median: " );
        buffer.append( median );
        Log.info( buffer.toString() );
        if ( myLastExceptionOrNull != null ) Log.info( "exception: {}", myLastExceptionOrNull );
        }

    private long myMinDelta;

    private long myMaxDelta;

    private long myDataCount;

    private long myStartTimestamp;

    private long myAccumulatedTimeInMillis;

    private Exception myLastExceptionOrNull;

    private final int myId;

    private final PlatformContext myPlatformContext;
    }
