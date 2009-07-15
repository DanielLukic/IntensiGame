package net.intensicode.util;

import java.util.Enumeration;
import java.util.Hashtable;

public final class Timing
    {
    public static final int INDENT_OFFSET = 60;

    public static final String INSERT_STRING = "        ";

    public static final int INSERT_WIDTH = INSERT_STRING.length();



    public static void insertFixed( final StringBuffer aBuffer )
        {
        //#if TIMING
        aBuffer.append( INSERT_STRING );
        //#endif
        }

    public static void insertFixed( final StringBuffer aBuffer, final String aString )
        {
        //#if TIMING
        final String string = aString.length() <= INSERT_WIDTH ? aString : aString.substring( 0, INSERT_WIDTH );
        aBuffer.append( INSERT_STRING.substring( 0, INSERT_WIDTH - string.length() ) );
        aBuffer.append( string );
        //#endif
        }

    public static void insertFixed( final StringBuffer aBuffer, final long aValue )
        {
        //#if TIMING
        insertFixed( aBuffer, Long.toString( aValue ) );
        //#endif
        }

    public static void start( final String aTag )
        {
        //#if TIMING
        synchronized ( theGlobalRootEntry )
            {
            theGlobalRootEntry.getOrCreateChild( aTag ).start();
            }
        getTimingByThread().doStart( aTag );
        //#endif
        }

    public static void end( final String aTag )
        {
        //#if TIMING
        getTimingByThread().doEnd( aTag );
        synchronized ( theGlobalRootEntry )
            {
            theGlobalRootEntry.getOrCreateChild( aTag ).end();
            }
        //#endif
        }

    public static void dumpInto( final StringBuffer aBuffer )
        {
        //#if TIMING
        final Enumeration threads = theTimingsByThread.keys();
        while ( threads.hasMoreElements() )
            {
            final Thread thread = (Thread) threads.nextElement();
            final Timing timing = (Timing) theTimingsByThread.get( thread );
            if ( timing == null ) continue;

            final String name = thread.getName();
            dumpTimingInto( aBuffer, timing.myRootEntry, name );
            }
        dumpTimingInto( aBuffer, theGlobalRootEntry, "GLOBAL" );
        //#endif
        }

    public Timing()
        {
        //#if TIMING
        myEntryStack.add( myRootEntry );
        //#endif
        }

    public final void doStart( final String aTag )
        {
        //#if TIMING
        final TimingEntry currentEntry = (TimingEntry) myEntryStack.last();

        final TimingEntry childEntry = currentEntry.getOrCreateChild( aTag );
        childEntry.start();

        myEntryStack.add( childEntry );
        //#endif
        }

    public final void doEnd( final String aTag )
        {
        //#if TIMING
        final TimingEntry currentEntry = (TimingEntry) myEntryStack.removeLast();
        currentEntry.end();

        if ( currentEntry.tag.equals( aTag ) ) return;

        Log.debug( "Timing::doEnd {} != {}", currentEntry.tag, aTag );
        Log.debug( "Timing::myEntryStack {}", myEntryStack );

        //#if FALSE
        System.exit( 10 );
        //#endif

        throw new RuntimeException();
        //#endif
        }

    public final void doDumpInto( final StringBuffer aBuffer )
        {
        //#if TIMING
        myRootEntry.dumpInto( aBuffer, " ", 1 );
        //#endif
        }

    // Implementation

    //#if TIMING

    private static Timing getTimingByThread()
        {
        final Thread thread = Thread.currentThread();
        final Timing timing = (Timing) theTimingsByThread.get( thread );
        if ( timing != null ) return timing;

        final Timing newTiming = new Timing();
        theTimingsByThread.put( thread, newTiming );
        return newTiming;
        }

    private static void dumpTimingInto( final StringBuffer aBuffer, final TimingEntry aTimingEntry, final String aName )
        {
        aBuffer.append( "\n" );
        aBuffer.append( "Timing(" );
        aBuffer.append( aName );
        aBuffer.append( "):\n" );

        for ( int idx = 0; idx < INDENT_OFFSET; idx++ ) aBuffer.append( ' ' );

        insertFixed( aBuffer, "cnt" );
        insertFixed( aBuffer, "acc" );
        insertFixed( aBuffer, "min" );
        insertFixed( aBuffer, "max" );
        insertFixed( aBuffer, "avg" );
        insertFixed( aBuffer, "(acc)" );
        insertFixed( aBuffer, "(avg)" );

        aBuffer.append( "\n" );

        aTimingEntry.dumpInto( aBuffer, " ", 1 );
        }



    private DynamicArray myEntryStack = new DynamicArray();

    private final TimingEntry myRootEntry = new TimingEntry( "ROOT" );

    private static final String ID_GLOBAL = "GLOBAL";

    private static final Hashtable theTimingsByThread = new Hashtable();

    private static final TimingEntry theGlobalRootEntry = new TimingEntry( ID_GLOBAL );

    //#endif
    }
