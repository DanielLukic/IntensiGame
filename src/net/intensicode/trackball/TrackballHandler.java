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

    // TODO: Move Internal API into internal class hidden from framework user.

    public synchronized final void onControlTick() throws Exception
        {
        while ( myQueuedEvents.size > 0 )
            {
            final TrackballEvent queuedEvent = (TrackballEvent) myQueuedEvents.remove( 0 );
            processQueuedEvent( queuedEvent );
            }
        }

    public synchronized final void onDrawFrame()
        {
        }

    // Protected API

    protected TrackballHandler()
        {
        }

    protected synchronized final void processTrackballEvent( final TrackballEvent aTrackballEvent )
        {
        // TODO: Use pool for these objects..
        if ( myQueuedEvents.size == MAX_QUEUED_EVENTS ) myQueuedEvents.remove( 0 );
        myQueuedEvents.add( new ClonedTrackballEvent( aTrackballEvent ) );
        }

    // Implementation

    private long myPreviousTimestamp;

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


    private final DynamicArray myListeners = new DynamicArray();

    private final DynamicArray myQueuedEvents = new DynamicArray( MAX_QUEUED_EVENTS, 0 );

    private static final int MAX_QUEUED_EVENTS = 32;
    }
