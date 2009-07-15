package net.intensicode.util;

public final class StorageException extends Exception
    {
    public final Throwable cause;



    public StorageException( final String aMessage )
        {
        this( aMessage, null );
        }

    public StorageException( final Throwable aCause )
        {
        this( aCause.getMessage(), aCause );
        }

    // From Throwable

    public String getMessage()
        {
        final String message = super.getMessage();
        if ( message != null ) return message;
        return cause.getMessage();
        }

    private StorageException( final String aMessage, final Throwable aCause )
        {
        super( aMessage );
        cause = aCause;
        }
    }
