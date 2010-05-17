//#condition TRACKBALL

package net.intensicode.trackball;

import net.intensicode.screens.ScreenBase;
import net.intensicode.util.*;

public abstract class TrackballHandler extends ScreenBase
    {
    public synchronized final void addListener( final TrackballEventListener aListener )
        {
        myListeners.add( aListener );
        }

    // Internal API

    public synchronized final void onControlTick() throws Exception
        {
        while ( myQueuedEvents.size > 0 )
            {
            final TrackballEvent queuedEvent = (TrackballEvent) myQueuedEvents.remove( 0 );
            processQueuedEvent( queuedEvent );
            myClonedEventPool.addReleasedInstance( queuedEvent );
            }
        }

    public synchronized final void onDrawFrame()
        {
        }

    // Protected API

    protected TrackballHandler() throws Exception
        {
        myClonedEventPool = new ObjectPool( "net.intensicode.trackball.ClonedTrackballEvent" );
        }

    protected synchronized final void processTrackballEvent( final TrackballEvent aTrackballEvent )
        {
        if ( myQueuedEvents.size == MAX_QUEUED_EVENTS )
            {
            final Object removedInstance = myQueuedEvents.remove( 0 );
            myClonedEventPool.addReleasedInstance( removedInstance );
            }

        final ClonedTrackballEvent clonedEvent = (ClonedTrackballEvent) myClonedEventPool.getOrCreateInstance();
        myQueuedEvents.add( clonedEvent.reinitializeWith( aTrackballEvent ) );
        }

    // Implementation

    private void processQueuedEvent( final TrackballEvent aQueuedEvent )
        {
        //#if DEBUG_TRACKBALL
        final long delta = aQueuedEvent.timestamp() - myPreviousTimestamp;
        Log.info( "TRACKBALL {} delta=" + delta, aQueuedEvent );
        myPreviousTimestamp = aQueuedEvent.timestamp();
        //#endif

        broadcastEvent( aQueuedEvent );
        }

    private void broadcastEvent( final TrackballEvent aQueuedEvent )
        {
        for ( int idx = 0; idx < myListeners.size; idx++ )
            {
            final TrackballEventListener listener = (TrackballEventListener) myListeners.get( idx );
            listener.onTrackballEvent( aQueuedEvent );
            }
        }


    private long myPreviousTimestamp;

    private final DynamicArray myListeners = new DynamicArray();

    private final DynamicArray myQueuedEvents = new DynamicArray( MAX_QUEUED_EVENTS, 0 );

    private final ObjectPool myClonedEventPool;

    private static final int MAX_QUEUED_EVENTS = 32;
    }
