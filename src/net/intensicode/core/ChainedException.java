package net.intensicode.core;

public final class ChainedException extends RuntimeException
    {
    public ChainedException( final Throwable aCause )
        {
        myCause = aCause;
        }

    public ChainedException( final String aMessage, final Throwable aCause )
        {
        super( aMessage );
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
