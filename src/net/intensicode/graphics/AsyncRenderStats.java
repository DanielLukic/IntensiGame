//#condition RENDER_ASYNC && RENDER_STATS

package net.intensicode.graphics;

import net.intensicode.PlatformContext;

public final class AsyncRenderStats
    {
    public AsyncRenderStats( final PlatformContext aPlatformContext, final int aNumberOfIds )
        {
        myEntries = new AsyncRenderStatsEntry[aNumberOfIds];
        for ( int idx = 0; idx < myEntries.length; idx++ )
            {
            myEntries[ idx ] = new AsyncRenderStatsEntry(aPlatformContext, idx );
            }
        }

    public final void start( final int aId )
        {
        myEntries[ aId ].start();
        }

    public final void end( final int aId )
        {
        myEntries[ aId ].end();
        }

    public final void fail( final int aId, final Exception aException )
        {
        myEntries[ aId ].fail( aException );
        }

    public final void dump()
        {
        for ( int idx = 0; idx < myEntries.length; idx++ )
            {
            myEntries[idx].dump();
            }
        }

    private final AsyncRenderStatsEntry[] myEntries;
    }
