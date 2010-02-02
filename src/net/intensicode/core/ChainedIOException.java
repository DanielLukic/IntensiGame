package net.intensicode.core;

import java.io.IOException;

public final class ChainedIOException extends IOException
    {
    public ChainedIOException( final Throwable aCause )
        {
        myCause = aCause;
        }

    // From Throwable

    public final String toString()
        {
        if ( myCause == null ) return super.toString();

        final StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( "\n\ncaused by:\n" );
        buffer.append( myCause.toString() );
        return buffer.toString();
        }

    public final void printStackTrace()
        {
        super.printStackTrace();
        if ( myCause == null ) return;

        System.out.println( "\n\ncaused by:\n" );
        myCause.printStackTrace();
        }

    private final Throwable myCause;
    }
