package net.intensicode.util;

import java.util.Enumeration;
import java.util.Hashtable;

public final class TimingEntry
    {
    public final String tag;

    public long accumulated;

    public long minimum;

    public long maximum;

    public long count;



    public TimingEntry( final String aTag )
        {
        tag = aTag;
        }

    public final void reset()
        {
        accumulated = minimum = maximum = count = myStartTime = 0;
        myStartCount = 0;
        myChildEntries.clear();
        }

    public final TimingEntry getOrCreateChild( final String aTag )
        {
        final TimingEntry entry = (TimingEntry) myChildEntries.get( aTag );
        if ( entry != null ) return entry;

        final TimingEntry newEntry = new TimingEntry( aTag );
        myChildEntries.put( aTag, newEntry );
        return newEntry;
        }

    public final void start()
        {
        if ( myStartCount == 0 ) myStartTime = System.currentTimeMillis();
        myStartCount++;
        }

    public final void end()
        {
        myStartCount--;
        if ( myStartCount > 0 ) return;

        if ( myStartCount < 0 ) throw new IllegalStateException( tag );

        final long endTime = System.currentTimeMillis();
        final long delta = endTime - myStartTime;

        if ( count == 0 )
            {
            accumulated = minimum = maximum = delta;
            }
        else
            {
            accumulated += delta;
            minimum = Math.min( minimum, delta );
            maximum = Math.max( maximum, delta );
            }

        count++;
        myStartTime = 0;
        }

    public void dumpInto( final StringBuffer aBuffer, final String aIndent, final int aIndentLevel )
        {
        for ( int idx = 0; idx < aIndentLevel; idx++ )
            {
            aBuffer.append( aIndent );
            }

        long childrenAccumulated = 0;

        {
        final Enumeration children = myChildEntries.elements();
        while ( children.hasMoreElements() )
            {
            final TimingEntry childEntry = (TimingEntry) children.nextElement();
            if ( childEntry == null ) continue;
            childrenAccumulated += childEntry.accumulated;
            }
        }

        final long self = accumulated - childrenAccumulated;

        aBuffer.append( tag );

        if ( count > 0 )
            {
            final int indentFix = Timing.INDENT_OFFSET - aIndent.length() * aIndentLevel - tag.length();
            for ( int idx = 0; idx < indentFix; idx++ ) aBuffer.append( ' ' );

            Timing.insertFixed( aBuffer, count );
            Timing.insertFixed( aBuffer, accumulated );
            Timing.insertFixed( aBuffer, minimum );
            Timing.insertFixed( aBuffer, maximum );
            Timing.insertFixed( aBuffer, accumulated / count );
            Timing.insertFixed( aBuffer, self );
            Timing.insertFixed( aBuffer, self / count );
            }

        aBuffer.append( "\n" );

        final Enumeration children = myChildEntries.elements();
        while ( children.hasMoreElements() )
            {
            final TimingEntry childEntry = (TimingEntry) children.nextElement();
            if ( childEntry == null ) continue;
            if ( childEntry.accumulated > 10 ) childEntry.dumpInto( aBuffer, aIndent, aIndentLevel + 1 );
            }
        }

    // From Object

    //#if LOGGING || DEBUG

    public final String toString()
        {
        return tag + "(0x" + Integer.toHexString( hashCode() ) + ")";
        }

    //#endif


    private int myStartCount;

    private long myStartTime;

    private final Hashtable myChildEntries = new Hashtable();
    }
