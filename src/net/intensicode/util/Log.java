package net.intensicode.util;

public class Log
    {
    public static Log theLog = new Log();


    public static void trace()
        {
        //#if TRACE
        theLog.doTrace();
        //#endif
        }




    public static void info( final String aMessage )
        {
        //#if INFO
        info( aMessage, NO_PARAMETERS );
        //#endif
        }

    public static void info( final String aMessage, final long aValue1 )
        {
        //#if INFO
        info( aMessage, new Object[]{ new Long( aValue1 ) } );
        //#endif
        }

    public static void info( final String aMessage, final Object aValue1 )
        {
        //#if INFO
        info( aMessage, new Object[]{ aValue1 } );
        //#endif
        }

    public static void info( final String aMessage, final Object aValue1, final Object aValue2 )
        {
        //#if INFO
        info( aMessage, new Object[]{ aValue1, aValue2 } );
        //#endif
        }

    public static void info( final String aMessage, final int aValue1, final int aValue2 )
        {
        //#if INFO
        info( aMessage, new Object[]{ new Integer( aValue1 ), new Integer( aValue2 ) } );
        //#endif
        }

    public static void info( final String aMessage, final long aValue1, final long aValue2 )
        {
        //#if INFO
        info( aMessage, new Object[]{ new Long( aValue1 ), new Long( aValue2 ) } );
        //#endif
        }

    //#if !CLDC10
    public static void info( final String aMessage, final double aValue1, final double aValue2 )
        {
        //#if INFO
        info( aMessage, new Object[]{ new Double( aValue1 ), new Double( aValue2 ) } );
        //#endif
        }
    //#endif

    public static void info( final String aMessage, final Object[] aObjects )
        {
        //#if INFO
        final StringBuffer buffer = StringUtils.format( aMessage, aObjects );
        theLog.doInfo( buffer );
        //#endif
        }




    public static void debug( final String aMessage )
        {
        //#if DEBUG
        debug( aMessage, NO_PARAMETERS );
        //#endif
        }

    public static void debug( final String aMessage, final long aValue1 )
        {
        //#if DEBUG
        debug( aMessage, new Object[]{ new Long( aValue1 ) } );
        //#endif
        }

    public static void debug( final String aMessage, final Object aValue1 )
        {
        //#if DEBUG
        debug( aMessage, new Object[]{ aValue1 } );
        //#endif
        }

    public static void debug( final String aMessage, final Object aValue1, final Object aValue2 )
        {
        //#if DEBUG
        debug( aMessage, new Object[]{ aValue1, aValue2 } );
        //#endif
        }

    public static void debug( final String aMessage, final int aValue1, final int aValue2 )
        {
        //#if DEBUG
        debug( aMessage, new Object[]{ new Integer( aValue1 ), new Integer( aValue2 ) } );
        //#endif
        }

    public static void debug( final String aMessage, final long aValue1, final long aValue2 )
        {
        //#if DEBUG
        debug( aMessage, new Object[]{ new Long( aValue1 ), new Long( aValue2 ) } );
        //#endif
        }

    //#if !CLDC10
    public static void debug( final String aMessage, final double aValue1, final double aValue2 )
        {
        //#if DEBUG
        debug( aMessage, new Object[]{ new Double( aValue1 ), new Double( aValue2 ) } );
        //#endif
        }
    //#endif

    public static void debug( final String aMessage, final Object[] aObjects )
        {
        //#if DEBUG
        final StringBuffer buffer = StringUtils.format( aMessage, aObjects );
        theLog.doDebug( buffer );
        //#endif
        }


    public static void error( final Throwable aThrowable )
        {
        error( null, null, aThrowable );
        }

    public static void error( final String aMessage, final Throwable aThrowable )
        {
        error( aMessage, new Object[]{ }, aThrowable );
        }

    public static void error( final String aMessage, final Object aObject, final Throwable aThrowable )
        {
        error( aMessage, new Object[]{ aObject }, aThrowable );
        }

    public static void error( final String aMessage, final Object[] aObjects, final Throwable aThrowable )
        {
        final StringBuffer buffer = StringUtils.format( aMessage, aObjects );
        theLog.doError( buffer, aThrowable );
        }

    // Protected API

    protected Log()
        {
        }

    //#if TRACE

    protected void doTrace()
        {
        // Not possible with J2ME..
        }

    //#endif

    //#if INFO

    protected void doInfo( final StringBuffer aBufferWithMessage )
        {
        aBufferWithMessage.insert( 0, "INFO " );
        System.out.println( aBufferWithMessage.toString() );
        }

    //#endif

    //#if DEBUG

    protected void doDebug( final StringBuffer aBufferWithMessage )
        {
        aBufferWithMessage.insert( 0, "DEBUG " );
        System.out.println( aBufferWithMessage.toString() );
        }

    //#endif

    protected void doError( final StringBuffer aBufferWithMessage, final Throwable aThrowable )
        {
        aBufferWithMessage.insert( 0, "ERROR " );
        System.out.println( aBufferWithMessage.toString() );

        if ( aThrowable != null ) aThrowable.printStackTrace();
        }


    private static Object[] NO_PARAMETERS = new Object[0];
    }
