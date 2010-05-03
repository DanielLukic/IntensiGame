package net.intensicode.util;

import net.intensicode.graphics.FontGenerator;

public final class StringUtils
    {
    public static final DynamicArray breakIntoLines( final String aText, final FontGenerator aFont, final int aBlockWidth )
        {
        final DynamicArray lines = new DynamicArray();

        final int textLength = aText.length();

        int start = 0;
        int end = start;
        do
            {
            final int nextSpaceOffset = aText.indexOf( " ", end + 1 );
            if ( nextSpaceOffset == -1 )
                {
                final int newWidth = aFont.substringWidth( aText, start, textLength - start );
                if ( newWidth <= aBlockWidth ) end = textLength;
                if ( start == end ) end = textLength;
                }
            else
                {
                final int newWidth = aFont.substringWidth( aText, start, nextSpaceOffset - start );
                if ( newWidth <= aBlockWidth )
                    {
                    end = nextSpaceOffset;
                    continue;
                    }
                }

            final int eolIndex = aText.indexOf( "\n", start );
            if ( eolIndex >= start && eolIndex < end ) end = eolIndex + 1;

            lines.add( aText.substring( start, end ) );

            while ( end < textLength && aText.charAt( end ) == ' ' ) end++;

            start = end;
            }
        while ( end < textLength );

        return lines;
        }

    public static DynamicArray splitString( final String aString, final boolean aTrimLines )
        {
        return splitString( aString, "\n\r", aTrimLines );
        }

    public static DynamicArray splitString( final String aString, final String aDelimiterList, final boolean aTrimLines )
        {
        int start = 0;
        int end = 0;

        final DynamicArray lines = new DynamicArray();

        final int length = aString.length();
        while ( end < length )
            {
            end = findDelimiter( aString, aDelimiterList, start );
            if ( end == -1 ) end = length;
            if ( end == start ) start++;
            if ( end < start ) continue;

            final String line = aString.substring( start, end );
            lines.add( aTrimLines ? line.trim() : line );

            start = end;
            }

        return lines;
        }

    public static int findDelimiter( final String aString, final String aDelimiterList, final int aStart )
        {
        final int numberOfDelimiters = aDelimiterList.length();

        int closestMatch = -1;
        for ( int idx = 0; idx < numberOfDelimiters; idx++ )
            {
            final int pos = aString.indexOf( aDelimiterList.charAt( idx ), aStart );
            if ( pos == -1 ) continue;
            if ( closestMatch == -1 ) closestMatch = pos;
            if ( pos < closestMatch ) closestMatch = pos;
            }

        return closestMatch;
        }

    public static StringBuffer format( final String aMessage, final Object[] aObjects )
        {
        if ( aMessage == null ) return new StringBuffer();

        final DynamicArray insertPositions = new DynamicArray( 5, 5 );
        findInsertPosition( aMessage, 0, insertPositions );

        final StringBuffer buffer = new StringBuffer( aMessage );
        final int valid = Math.min( aObjects.length, insertPositions.size );
        for ( int idx = valid - 1; idx >= 0; idx-- )
            {
            final int insertPos = ( (Integer) insertPositions.objects[ idx ] ).intValue();
            buffer.delete( insertPos, insertPos + 2 );
            buffer.insert( insertPos, aObjects[ idx ] );
            }

        return buffer;
        }

    // Implementation

    private static void findInsertPosition( final String aMessage, final int aStartIndex, final DynamicArray aOutputArray )
        {
        final int foundIndex = aMessage.indexOf( "{}", aStartIndex );
        if ( foundIndex == -1 ) return;

        aOutputArray.add( new Integer( foundIndex ) );
        findInsertPosition( aMessage, foundIndex + 1, aOutputArray );
        }

    private StringUtils()
        {
        }
    }
