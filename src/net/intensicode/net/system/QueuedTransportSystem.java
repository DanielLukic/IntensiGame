package net.intensicode.net.system;

import net.intensicode.net.*;
import net.intensicode.net.connection.TransportConnection;
import net.intensicode.util.Log;

public final class QueuedTransportSystem implements TransportSystem, Runnable
    {
    public String name = "QueuedTransportSystem";

    public QueuedTransportSystem( final String aDefaultBaseUrl, final TransportConnection aConnection )
        {
        myDefaultBaseUrl = aDefaultBaseUrl;
        myConnection = aConnection;
        }

    // From TransportSystem

    public synchronized final void start()
        {
        stopIfNecessary();
        startNewThread();
        }

    public synchronized final void stop()
        {
        clearEventQueue();
        enqueueEvent( PRIORITY_SYSTEM, STOP, null );
        stopThread();
        }

    public final void request( final Request aRequest, final TransportSystemCallback aCallback )
        {
        request( aCallback.getTransportPriority(), aRequest, aCallback );
        }

    public final void request( final int aPriority, final Request aRequest, final TransportSystemCallback aCallback )
        {
        startThreadIfNecessary();
        enqueueEvent( aPriority, aRequest, aCallback );
        }

    public final void cancel( final Request aRequest )
        {
        if ( dequeuePendingEvent( aRequest ) ) return;
        cancelActiveEvent(); // TODO: Valid assumption that the request is the active one?
        }

    public void cancelAll()
        {
        clearEventQueue();
        cancelActiveEvent();
        }

    public final void cancelAll( final String aRequestId )
        {
        myEventQueue.removeAll( aRequestId );
        }

    // From Runnable

    public final void run()
        {
        try
            {
            processEventsUntilInterrupted();
            }
        finally
            {
            markTransportSystemStopped();
            }
        }

    // Implementation

    private synchronized void stopIfNecessary()
        {
        if ( myThread != null ) stop();
        }

    private void startNewThread()
        {
        myThread = new Thread( this, "QueuedTransportSystem" );
        myThread.start();
        }

    private void clearEventQueue()
        {
        myEventQueue.clear();
        }

    private void stopThread()
        {
        if ( myThread != null && myThread.isAlive() ) myThread.interrupt();
        markTransportSystemStopped();
        }

    private void markTransportSystemStopped()
        {
        if ( myThread != null ) Log.debug( "QueuedTransportSystem stopped" );
        myThread = null;
        }

    private synchronized void startThreadIfNecessary()
        {
        if ( myThread == null ) start();
        }

    private void enqueueEvent( final int aPriority, final Request aRequest, final TransportSystemCallback aCallback )
        {
        myEventQueue.queue( new TransportEvent( aPriority, aRequest, aCallback ) );
        }

    private boolean dequeuePendingEvent( final Request aRequest )
        {
        return myEventQueue.remove( aRequest );
        }

    private void cancelActiveEvent()
        {
        myConnection.cancel();
        }

    private void processEventsUntilInterrupted()
        {
        try
            {
            processEvents();
            }
        catch ( InterruptedException e )
            {
            // Simply bail out here. The transport system has been stopped!
            }
        }

    private void processEvents() throws InterruptedException
        {
        while ( true )
            {
            final TransportEvent event = dequeueNextEvent();
            event.setBaseUrlIfNecessary( myDefaultBaseUrl );
            Log.debug( "QueuedTransportSystem processing {}", event );
            new TransportEventProcessor( myConnection, event ).process();
            }
        }

    private TransportEvent dequeueNextEvent() throws InterruptedException
        {
        final TransportEvent event = myEventQueue.dequeue();
        if ( event.request == STOP ) throw new InterruptedException();
        return event;
        }

    private Thread myThread;

    private final String myDefaultBaseUrl;

    private final TransportConnection myConnection;

    private final TransportQueue myEventQueue = new TransportQueue();

    private static final Request STOP = new RequestGet( "STOP" );
    }
