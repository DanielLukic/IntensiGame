//#condition RENDER_ASYNC

package net.intensicode.graphics;

import net.intensicode.util.DynamicArray;
import net.intensicode.util.Log;

public final class AsyncRenderQueue
    {
    public AsyncRenderQueue( final int aNumberOfQueues )
        {
        myCompletedQueues = new DynamicArray();
        for ( int idx = 0; idx < aNumberOfQueues; idx++ )
            {
            myCompletedQueues.add( new DynamicArray() );
            }
        }

    public synchronized final void postCompletedQueue( final DynamicArray aCommandQueue )
        {
        myCompletedQueues.add( aCommandQueue );
        notify();
        }

    public synchronized final DynamicArray waitForCompletedQueue() throws InterruptedException
        {
        while ( myCompletedQueues.size == 0 )
            {
            wait( 1000 / 25 );
            if ( !myCompletedQueues.empty() ) Log.info( "NO COMPLETED QUEUE IN TIME" );
            }
        return (DynamicArray) myCompletedQueues.remove( 0 );
        }

    public synchronized final void postFilledQueue( final DynamicArray aCommandQueue )
        {
        myFilledQueues.add( aCommandQueue );
        notify();
        }

    public synchronized final DynamicArray waitForFilledQueue() throws InterruptedException
        {
        while ( myFilledQueues.size == 0 )
            {
            wait();
            }
        return (DynamicArray) myFilledQueues.remove( 0 );
        }

    private final DynamicArray myCompletedQueues;

    private final DynamicArray myFilledQueues = new DynamicArray();
    }
