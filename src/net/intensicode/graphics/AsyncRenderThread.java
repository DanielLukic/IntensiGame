package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;
import net.intensicode.util.DynamicArray;

public final class AsyncRenderThread extends Thread
    {
    public AsyncRenderThread( final DynamicArray aRenderQueue, final DirectGraphics aGraphics )
        {
        super( "AsyncRenderThread" );
        myRenderQueue = aRenderQueue;
        myGraphics = aGraphics;
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
            renderQueuedData();
            clearRenderQueue();
            }
        }

    private void waitForRenderData() throws InterruptedException
        {
        synchronized ( myRenderQueue ) { while ( myRenderQueue.empty() ) myRenderQueue.wait(); }
        }

    private void renderQueuedData()
        {
        myGraphics.beginFrame();
        for ( int idx = 0; idx < myRenderQueue.size; idx++ )
            {
            final GraphicsCommand command = (GraphicsCommand) myRenderQueue.get( idx );
            command.execute( myGraphics );
            }
        myGraphics.endFrame();
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
