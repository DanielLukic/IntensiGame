package net.intensicode.core;

import net.intensicode.util.*;

import java.util.Hashtable;

public final class Configuration
    {
    public static final Configuration NULL_CONFIGURATION = new Configuration();

    public Configuration()
        {
        }

    public Configuration( final String aConfigurationData )
        {
        final DynamicArray lines = StringUtils.splitString( aConfigurationData, true );
        for ( int idx = 0; idx < lines.size; idx++ )
            {
            final String line = ( (String) lines.objects[ idx ] );
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
        final DynamicArray entries = StringUtils.splitString( value, aDelimiterList, true );
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
            Log.debug( "readHex {} => {}", value, "" + Long.parseLong( value, 16 ) );
            if ( value == null ) return aDefault;
            return (int) Long.parseLong( value, 16 );
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

    public String makeKey( final String aPrefix, final int aIndex )
        {
        mySharedBuffer.setLength( 0 );
        mySharedBuffer.append( aPrefix );
        mySharedBuffer.append( '.' );
        mySharedBuffer.append( aIndex );
        return mySharedBuffer.toString();
        }

    // Implementation

    private void consume( final String aLine )
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

    private String extractValue( final String aEntry, final String aSubEntry )
        {
        if ( aSubEntry == null ) return (String) myEntries.get( aEntry );
        mySharedBuffer.setLength( 0 );
        mySharedBuffer.append( aEntry );
        mySharedBuffer.append( '.' );
        mySharedBuffer.append( aSubEntry );
        return (String) myEntries.get( mySharedBuffer.toString() );
        }


    private final Hashtable myEntries = new Hashtable();

    private final StringBuffer mySharedBuffer = new StringBuffer();
    }
