package net.intensicode.util;

public final class Format
    {
    public static String _( final String aMessage, final Object aObject )
        {
        return _( aMessage, new Object[]{ aObject } );
        }

    public static String _( final String aMessage, final Object[] aObjects )
        {
        if ( aMessage == null ) return EMPTY_STRING;

        return StringUtils.format( aMessage, aObjects ).toString();
        }

    // Implementation

    private Format()
        {
        }

    private static final String EMPTY_STRING = "";
    }
