package net.intensicode.net.system;

import net.intensicode.net.Request;
import net.intensicode.util.DynamicArray;

final class TransportQueue
    {
    public synchronized final void clear()
        {
        myQueue.clear();
        }

    public final int size()
        {
        return myQueue.size;
        }

    public synchronized final void queue( final TransportEvent aNewEvent )
        {
        for ( int idx = 0; idx < myQueue.size; idx++ )
            {
            final TransportEvent event = (TransportEvent) myQueue.get( idx );
            if ( aNewEvent.priority >= event.priority ) continue;

            myQueue.insert( idx, aNewEvent );
            this.notify();
            return;
            }

        myQueue.add( aNewEvent );
        this.notify();
        }

    public synchronized final TransportEvent dequeue() throws InterruptedException
        {
        while ( myQueue.size == 0 ) this.wait();
        if ( myQueue.size == 0 ) return null;
        return (TransportEvent) myQueue.remove( 0 );
        }

    public synchronized final boolean remove( final Request aRequest )
        {
        for ( int idx = myQueue.size - 1; idx >= 0; idx-- )
            {
            final TransportEvent event = (TransportEvent) myQueue.get( idx );
            if ( event.request == aRequest )
                {
                myQueue.remove( idx );
                return true;
                }
            }
        return false;
        }

    public synchronized final void removeAll( final String aRequestId )
        {
        for ( int idx = myQueue.size - 1; idx >= 0; idx-- )
            {
            final TransportEvent event = (TransportEvent) myQueue.get( idx );
            if ( event.request.id.equals( aRequestId ) ) myQueue.remove( idx );
            }
        }

    public synchronized final void removeAllFrom( final TransportSystemCallback aCallback )
        {
        for ( int idx = myQueue.size - 1; idx >= 0; idx-- )
            {
            final TransportEvent event = (TransportEvent) myQueue.get( idx );
            if ( event.callback == aCallback ) myQueue.remove( idx );
            }
        }

    public final boolean isEmpty()
        {
        return myQueue.size == 0;
        }



    private final DynamicArray myQueue = new DynamicArray();
    }
