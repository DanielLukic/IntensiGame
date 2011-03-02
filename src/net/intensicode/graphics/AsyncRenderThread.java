//#condition RENDER_ASYNC

package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;
import net.intensicode.util.DynamicArray;
import net.intensicode.PlatformContext;

public final class AsyncRenderThread implements Runnable
    {
    public AsyncRenderThread( final DynamicArray aRenderQueue, final DirectGraphics aGraphics, final PlatformContext aPlatformContext )
        {
        myRenderQueue = aRenderQueue;
        myGraphics = aGraphics;
        //#if RENDER_STATS
        myStats = new AsyncRenderStats( aPlatformContext,  GraphicsCommand.NUMBER_OF_IDS );
        //#endif
        }

    private Thread myThreadOrNull;

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
            waitForRenderData();
            myGraphics.beginFrame();
            renderQueuedData();
            clearRenderQueue();
            myGraphics.endFrame();
            }
        }

    private void waitForRenderData() throws InterruptedException
        {
        synchronized ( myRenderQueue ) { while ( myRenderQueue.empty() ) myRenderQueue.wait(); }
        }

    private void renderQueuedData()
        {
        for ( int idx = 0; idx < myRenderQueue.size; idx++ )
            {
            final GraphicsCommand command = (GraphicsCommand) myRenderQueue.get( idx );
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

    private void clearRenderQueue()
        {
        synchronized ( myRenderQueue )
            {
            myRenderQueue.clear();
            myRenderQueue.notify();
            }
        }

    private final DynamicArray myRenderQueue;

    private final DirectGraphics myGraphics;

    //#if RENDER_STATS

    private final AsyncRenderStats myStats;

    //#endif
    }
