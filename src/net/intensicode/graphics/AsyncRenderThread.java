//#condition RENDER_ASYNC

package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;
import net.intensicode.util.DynamicArray;

public final class AsyncRenderThread implements Runnable
    {
    public AsyncRenderThread( final DynamicArray aRenderQueue, final DirectGraphics aGraphics )
        {
        myRenderQueue = aRenderQueue;
        myGraphics = aGraphics;
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
        }

    public final void run()
        {
        try
            {
            runInterruptible();
            }
        catch ( final InterruptedException e )
            {
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
            command.execute( myGraphics );
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
    }
