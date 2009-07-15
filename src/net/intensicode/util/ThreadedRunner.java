package net.intensicode.util;

public abstract class ThreadedRunner implements Runnable
    {
    protected ThreadedRunner( final String aName )
        {
        myName = aName;
        }

    public final synchronized void start()
        {
        if ( myThread != null ) return;
        myThread = new Thread( this, myName );
        myThread.start();
        }

    public final synchronized void stop()
        {
        if ( myThread != null ) myThread.interrupt();
        myThread = null;
        }

    // From Runnable

    public final void run()
        {
        try
            {
            runInterrupptible();
            }
        catch ( final InterruptedException e )
            {
            // We can simply leave here..
            }

        Log.debug( "ThreadedRunner {} terminated", myName );
        }

    // Abstract Interface

    protected abstract void runInterrupptible() throws InterruptedException;



    private Thread myThread;

    private final String myName;
    }
