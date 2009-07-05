package net.intensicode.core;

import net.intensicode.util.DynamicArray;
import net.intensicode.util.Log;

import java.util.Hashtable;



/**
 * TODO: Describe this!
 */
public final class Configuration
    {
    public Configuration()
        {
        }

    public Configuration( final String aConfigurationData )
        {
        final DynamicArray lines = splitString( aConfigurationData, true );
        for ( int idx = 0; idx < lines.size; idx++ )
            {
            final String line = ((String) lines.objects[ idx ]);
            if ( line.length() < 3 ) continue;
            if ( line.startsWith( "//" ) ) continue;
            if ( line.startsWith( "#" ) ) continue;
            consume( line );
            }
        }

    public final boolean isEmpty()
        {
        return myEntries.isEmpty();
        }

    public final void store( final String aKey, final String aValue )
        {
        myEntries.put( aKey, aValue );
        }

    public final String[] readList( final String aEntry, final String aDefault, final String aDelimiterList )
        {
        final String value = readString( aEntry, aDefault );
        final DynamicArray entries = splitString( value, aDelimiterList, true );
        final String[] strings = new String[entries.size];
        for ( int idx = 0; idx < entries.size; idx++ )
            {
            strings[ idx ] = (String) entries.objects[ idx ];
            }
        return strings;
        }

    public final String readString( final String aEntry, final String aDefault )
        {
        return readString( aEntry, null, aDefault );
        }

    public final String readString( final String aEntry, final String aSubEntry, final String aDefault )
        {
        final String value = extractValue( aEntry, aSubEntry );
        if ( value == null ) return aDefault;
        return value;
        }

    public final boolean readBoolean( final String aEntry, final boolean aDefault )
        {
        return readBoolean( aEntry, null, aDefault );
        }

    public final boolean readBoolean( final String aEntry, final String aSubEntry, final boolean aDefault )
        {
        final String value = extractValue( aEntry, aSubEntry );
        if ( value == null ) return aDefault;
        if ( value.equals( "true" ) ) return true;
        if ( value.equals( "false" ) ) return false;
        return aDefault;
        }

    public final int readHex( final String aEntry, final int aDefault )
        {
        try
            {
            final String value = extractValue( aEntry, null );
            if ( value == null ) return aDefault;
            return Integer.parseInt( value, 16 );
            }
        catch ( final NumberFormatException e )
            {
            return aDefault;
            }
        }

    public final int readInt( final String aEntry, final int aDefault )
        {
        return readInt( aEntry, null, aDefault );
        }

    public final int readInt( final String aEntry, final String aSubEntry, final int aDefault )
        {
        try
            {
            final String value = extractValue( aEntry, aSubEntry );
            if ( value == null ) return aDefault;
            return Integer.parseInt( value );
            }
        catch ( final NumberFormatException e )
            {
            return aDefault;
            }
        }

    public static final DynamicArray splitString( final String aString, final boolean aTrimLines )
        {
        return splitString( aString, "\n\r", aTrimLines );
        }

    public static final DynamicArray splitString( final String aString, final String aDelimiterList, final boolean aTrimLines )
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

    public static final int findDelimiter( final String aString, final String aDelimiterList, final int aStart )
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

    public static final String makeKey( final String aPrefix, final int aIndex )
        {
        SHARED_BUFFER.setLength( 0 );
        SHARED_BUFFER.append( aPrefix );
        SHARED_BUFFER.append( '.' );
        SHARED_BUFFER.append( aIndex );
        return SHARED_BUFFER.toString();
        }

    // Implementation

    private final void consume( final String aLine )
        {
        final int assignmentIndex = aLine.indexOf( '=' );

        //#if DEBUG
        if ( assignmentIndex == -1 )
            {
            Log.debug( "Bad input line: {}", aLine );
            throw new IllegalArgumentException();
            }
        //#endif
        if ( assignmentIndex == -1 ) return;

        final String key = aLine.substring( 0, assignmentIndex ).trim();
        final String value = aLine.substring( assignmentIndex + 1 ).trim();

        //#if DEBUG
        if ( key.length() == -1 ) Log.debug( "Bad key: {}", key );
        if ( value.length() == -1 ) Log.debug( "Bad value: {}", value );
        //#endif

        if ( key.length() > 0 && value.length() > 0 ) myEntries.put( key, value );
        }

    private final String extractValue( final String aEntry, final String aSubEntry )
        {
        if ( aSubEntry == null ) return (String) myEntries.get( aEntry );
        SHARED_BUFFER.setLength( 0 );
        SHARED_BUFFER.append( aEntry );
        SHARED_BUFFER.append( '.' );
        SHARED_BUFFER.append( aSubEntry );
        return (String) myEntries.get( SHARED_BUFFER.toString() );
        }



    private final Hashtable myEntries = new Hashtable();

    private static final StringBuffer SHARED_BUFFER = new StringBuffer();
    }
