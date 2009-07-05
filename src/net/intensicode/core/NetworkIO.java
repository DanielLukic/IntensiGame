package net.intensicode.core;

import net.intensicode.util.DynamicArray;
import net.intensicode.util.Log;

import java.util.Hashtable;



/**
 * TODO: Describe this!
 */
public final class NetworkIO implements Runnable
{
    public static final int SHARED_BUFFER_SIZE = 4096;

    public static final byte[] theBuffer = new byte[ SHARED_BUFFER_SIZE ];

    public static String theStatus = null;



    NetworkIO()
    {
    }

    final void start()
    {
        synchronized ( myPendingRequests )
        {
            if ( myThread != null ) return;
            myThread = new Thread( this );
            myThread.setPriority( Thread.MAX_PRIORITY );
            myThread.start();

            myPendingRequests.notifyAll();
        }
    }

    final void stop()
    {
        synchronized ( myPendingRequests )
        {
            if ( myThread == null ) return;
            myThread = null;

            myPendingRequests.notifyAll();
        }
    }

    // From Runnable

    public final void run()
    {
        try
        {
            out:
            while ( myThread != null )
            {
                final NetworkRequest request;
                synchronized ( myPendingRequests )
                {
                    while ( myPendingRequests.size == 0 )
                    {
                        myPendingRequests.wait();
                        if ( myThread == null ) break out;
                    }
                    request = ( NetworkRequest ) myPendingRequests.remove( 0 );

                    //#if DEBUG
                    Log.debug( "Pending requests: {}", myPendingRequests.size );
                    //#endif
                }
                try
                {
                    request.execute();
                }
                catch ( final Throwable aThrowable )
                {
                    request.callback().onError( aThrowable );
                }
            }
        }
        catch ( final InterruptedException e )
        {
        }
    }

    public final void sendAndReceive( final String aURL, final byte[] aBody, final NetworkCallback aCallback )
    {
        //#if DEBUG
        Log.debug( "Queuing request for {}", aURL );
        //#endif

        synchronized ( myPendingRequests )
        {
            myPendingRequests.add( new BinaryPostRequest( aURL, aBody, aCallback ) );
            myPendingRequests.notifyAll();
        }
    }

    private Thread myThread;

    private final DynamicArray myPendingRequests = new DynamicArray();
}
