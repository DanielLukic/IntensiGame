package net.intensicode.util;

/**
 * TODO: Describe this!
 */
public final class Log
    {
    public static String format( final String aMessage, final Object[] aObjects )
        {
        return preFormat( aMessage, aObjects ).toString();
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

    //#if CLDC11
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
        final StringBuffer buffer = preFormat( aMessage, aObjects );
        buffer.insert( 0, "[DEBUG] " );
        System.out.println( buffer.toString() );
        //#endif
        }

    public static void error( final Throwable aThrowable )
        {
        //#if DEBUG
        error( null, null, aThrowable );
        //#endif
        }

    public static void error( final String aMessage, final Throwable aThrowable )
        {
        //#if DEBUG
        error( aMessage, new Object[]{ }, aThrowable );
        //#endif
        }

    public static void error( final String aMessage, final Object aObject, final Throwable aThrowable )
        {
        //#if DEBUG
        error( aMessage, new Object[]{ aObject }, aThrowable );
        //#endif
        }

    public static void error( final String aMessage, final Object[] aObjects, final Throwable aThrowable )
        {
        //#if DEBUG
        final StringBuffer buffer = preFormat( aMessage, aObjects );
        buffer.insert( 0, "[ERROR] " );
        System.err.println( buffer.toString() );
        if ( aThrowable != null ) aThrowable.printStackTrace();
        //#endif
        }

    // Implementation

    private static StringBuffer preFormat( final String aMessage, final Object[] aObjects )
        {
        if ( aMessage == null ) return new StringBuffer();

        final DynamicArray insertPositions = new DynamicArray( 5, 5 );
        findInsertPosition( aMessage, 0, insertPositions );

        final StringBuffer buffer = new StringBuffer( aMessage );
        final int valid = Math.min( aObjects.length, insertPositions.size );
        for ( int idx = valid - 1; idx >= 0; idx-- )
            {
            final int insertPos = ((Integer) insertPositions.objects[ idx ]).intValue();
            buffer.delete( insertPos, insertPos + 2 );
            buffer.insert( insertPos, aObjects[ idx ] );
            }

        return buffer;
        }

    private static void findInsertPosition( final String aMessage, final int aStartIndex, final DynamicArray aOutputArray )
        {
        final int foundIndex = aMessage.indexOf( "{}", aStartIndex );
        if ( foundIndex == -1 ) return;

        aOutputArray.add( new Integer( foundIndex ) );
        findInsertPosition( aMessage, foundIndex + 1, aOutputArray );
        }

    private static Object[] NO_PARAMETERS = new Object[0];
    }
