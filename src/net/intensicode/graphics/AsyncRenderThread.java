//#condition RENDER_ASYNC

package net.intensicode.graphics;

import net.intensicode.PlatformContext;
import net.intensicode.core.DirectGraphics;
import net.intensicode.util.DynamicArray;

public final class AsyncRenderThread implements Runnable
    {
    public AsyncRenderThread( final AsyncRenderQueue aRenderQueue, final DirectGraphics aGraphics, final PlatformContext aPlatformContext )
        {
        myRenderQueue = aRenderQueue;
        myGraphics = aGraphics;
        //#if RENDER_STATS
        myStats = new AsyncRenderStats( aPlatformContext, GraphicsCommand.NUMBER_OF_IDS );
        //#endif
        }

    public synchronized final void start()
        {
        if ( myThreadOrNull == null ) myThreadOrNull = new Thread( this, "AsyncRenderThread" );
        if ( !myThreadOrNull.isAlive() ) myThreadOrNull.start();
        }

    public synchronized final void stop()
        {
        if ( myThreadOrNull == null ) return;
        if ( myThreadOrNull.isAlive() ) myThreadOrNull.interrupt();
        myThreadOrNull = null;

        //#if RENDER_STATS
        dumpStats();
        //#endif
        }

    //#if RENDER_STATS

    private void dumpStats()
        {
        myStats.dump();
        }

    //#endif

    public final void run()
        {
        try
            {
            runInterruptible();
            }
        catch ( final InterruptedException e )
            {
            //#if RENDER_STATS
            dumpStats();
            //#endif
            }
        }

    private void runInterruptible() throws InterruptedException
        {
        while ( true )
            {
            myCommandQueue = myRenderQueue.waitForFilledQueue();
            renderQueuedData();
            myRenderQueue.postCompletedQueue( myCommandQueue );
            }
        }

    private void renderQueuedData()
        {
        for ( int idx = 0; idx < myCommandQueue.size; idx++ )
            {
            final GraphicsCommand command = (GraphicsCommand) myCommandQueue.get( idx );
            try
                {
                //#if RENDER_STATS
                myStats.start( command.id );
                //#endif
                command.execute( myGraphics );
                //#if RENDER_STATS
                myStats.end( command.id );
                //#endif
                }
            catch ( final Exception e )
                {
                //#if RENDER_STATS
                myStats.fail( command.id, e );
                //#endif
                }
            }
        }


    private Thread myThreadOrNull;

    private DynamicArray myCommandQueue;

    //#if RENDER_STATS

    private final AsyncRenderStats myStats;

    //#endif

    private final DirectGraphics myGraphics;

    private final AsyncRenderQueue myRenderQueue;
    }
