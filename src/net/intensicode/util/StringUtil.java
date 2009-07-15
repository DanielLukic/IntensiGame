package net.intensicode.util;

public class StringUtil
    {
    public static String getAsStringOrNull( final Object aObject )
        {
        if ( aObject == null ) return null;
        if ( aObject instanceof String ) return (String) aObject;
        return aObject.toString();
        }

    public static String createMessage( final Object[] aArray, final int aSize )
        {
        if ( aArray == null || aArray.length == 0 ) return "";

        final StringBuffer message = new StringBuffer();
        for ( int idx = 0; idx < aSize; idx++ )
            {
            message.append( aArray[ idx ] );
            message.append( ' ' );
            }
        if ( message.length() > 0 ) message.setLength( message.length() - 1 );

        return message.toString();
        }

    public static String format( String pattern, Object[] args )
        {
        Timing.start( "StringUtil:format" );
        try
            {
            if ( pattern == null || args == null ) return pattern;
            if ( pattern.indexOf( '{' ) == -1 ) return pattern;
            if ( pattern.indexOf( '}' ) == -1 ) return pattern;

            StringBuffer toAppendTo = new StringBuffer();
            int l = pattern.length();
            int n = 0, lidx = -1, lastidx = 0;
            for ( int i = 0; i < l; i++ )
                {
                if ( pattern.charAt( i ) == '{' )
                    {
                    n++;
                    if ( n == 1 )
                        {
                        lidx = i;
                        toAppendTo.append( pattern.substring( lastidx, i ) );
                        lastidx = i;
                        }
                    }
                if ( pattern.charAt( i ) == '}' )
                    {
                    if ( n == 1 )
                        {
                        toAppendTo.append( processPattern( pattern.substring( lidx + 1, i ), args ) );
                        lidx = -1;
                        lastidx = i + 1;
                        }
                    n--;
                    }
                }
            if ( n > 0 )
                {
                toAppendTo.append( processPattern( pattern.substring( lidx + 1 ), args ) );
                }
            else
                {
                toAppendTo.append( pattern.substring( lastidx ) );
                }
            return toAppendTo.toString();
            }
        finally
            {
            Timing.end( "StringUtil:format" );
            }
        }

    private static String processPattern( String indexString, Object[] args )
        {
        try
            {
            int index = Integer.parseInt( indexString );
            if ( ( args != null ) && ( index >= 0 ) && ( index < args.length ) )
                {
                if ( args[ index ] != null ) return args[ index ].toString();
                }
            }
        catch ( NumberFormatException nfe )
            {
            // NFE - nothing bad basically - the argument is not a number
            // swallow it for the time being and show default string
            }
        return "?";
        }

    public static void splitLines( final String aStringWithLineSeparators, final DynamicArray aLinesBuffer, final int aStartIndex )
        {
        throw new RuntimeException( "nyi" );
        }

    public static void splitWords( final String aStringWithSpaces, final DynamicArray aWordsBuffer, final int aScanIndex )
        {
        if ( aScanIndex >= aStringWithSpaces.length() ) return;
        final int nextSpace = aStringWithSpaces.indexOf( ' ', aScanIndex );

        // Space at beginning?
        if ( nextSpace == aScanIndex )
            {
            splitWords( aStringWithSpaces, aWordsBuffer, aScanIndex + 1 );
            return;
            }

        final int nextQuote = aStringWithSpaces.indexOf( '"', aScanIndex );

        // Quote before next space?
        if ( nextQuote != -1 && ( nextQuote < nextSpace || nextSpace == -1 ) )
            {
            final int closingQuote = aStringWithSpaces.indexOf( '"', nextQuote + 1 );

            // Valid end quote position?
            if ( closingQuote != -1 )
                {
                final String nextWord = aStringWithSpaces.substring( aScanIndex + 1, closingQuote );
                aWordsBuffer.add( nextWord );
                splitWords( aStringWithSpaces, aWordsBuffer, closingQuote + 1 );
                return;
                }
            }

        // No more spaces before end of string?
        if ( nextSpace == -1 )
            {
            aWordsBuffer.add( aStringWithSpaces.substring( aScanIndex ) );
            return;
            }

        // No quote before next space..

        final String nextWord = aStringWithSpaces.substring( aScanIndex, nextSpace );
        aWordsBuffer.add( nextWord );
        splitWords( aStringWithSpaces, aWordsBuffer, nextSpace + 1 );
        }

    public static int lastIndexOf( final String aToken, final String aStringToBeSearched, final int aStartIndex )
        {
        if ( aStartIndex >= aStringToBeSearched.length() ) return -1;

        final int foundAt = aStringToBeSearched.indexOf( aToken, aStartIndex );
        if ( foundAt == -1 ) return -1;

        final int behindIndex = lastIndexOf( aToken, aStringToBeSearched, foundAt + aToken.length() );
        if ( behindIndex == -1 ) return foundAt;

        return behindIndex;
        }
    }
