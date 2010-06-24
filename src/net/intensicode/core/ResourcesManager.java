package net.intensicode.core;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

import java.io.*;

public abstract class ResourcesManager
    {
    public final void clearSubfolders()
        {
        mySubfolders.clear();
        }

    public final void addSubfolder( final String aSubfolderPath )
        {
        mySubfolders.add( aSubfolderPath );
        }

    public final boolean doesResourceExist( final String aResourcePath )
        {
        boolean exists = false;
        try
            {
            final InputStream resourceStream = openStream( aResourcePath );
            if ( resourceStream != null )
                {
                exists = true;
                resourceStream.close();
                }
            }
        catch ( final IOException e )
            {
            // Ignore this here..
            }
        return exists;
        }

    public abstract FontGenerator getSmallDefaultFont();

    public abstract int maxImageResourceSize();

    public abstract ImageResource createImageResource( int aWidth, int aHeight );

    public final ImageResource loadImageResource( final String aResourcePath ) throws IOException
        {
        final InputStream stream = openStream( aResourcePath );
        if ( stream == null ) throw new IOException( "image resource not found: " + aResourcePath );
        return loadImageResourceDo( aResourcePath, stream );
        }

    public final InputStream openStream( final String aResourcePath )
        {
        for ( int idx = 0; idx < mySubfolders.size; idx++ )
            {
            final String subfolderPath = makeSubfolderPath( mySubfolders.get( idx ), aResourcePath );
            //#if DEBUG_RESOURCES
            Log.info( "trying to load resource from {}", subfolderPath );
            //#endif
            final InputStream stream = openStreamDo( subfolderPath );
            if ( stream != null ) return stream;
            }
        return openStreamDo( aResourcePath );
        }

    public final InputStream openStreamChecked( final String aResourcePath ) throws IOException
        {
        final InputStream resource = openStream( aResourcePath );
        if ( resource == null ) throw new IOException( "resource does not exist: " + aResourcePath );
        return resource;
        }

    public final byte[] loadData( final String aResourcePath ) throws IOException
        {
        return loadStream( openStreamChecked( aResourcePath ) );
        }

    public final String loadString( final String aResourcePath ) throws IOException
        {
        return new String( loadData( aResourcePath ) );
        }

    public final Configuration loadConfiguration( final String aResourcePath ) throws IOException
        {
        final String data = loadString( aResourcePath );
        return new Configuration( this ).consume( data );
        }

    public final Configuration loadConfigurationOrUseDefaults( final String aResourcePath )
        {
        try
            {
            return loadConfiguration( aResourcePath );
            }
        catch ( final IOException e )
            {
            Log.error( "Failed loading {} configuration. Returning empty configuration.", aResourcePath, null );
            return Configuration.NULL_CONFIGURATION;
            }
        }

    public static byte[] loadStream( final InputStream aStream ) throws IOException
        {
        if ( aStream == null ) throw new NullPointerException();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[STREAM_COPY_BUFFER_SIZE_IN_BYTES];
        while ( true )
            {
            final int newBytes = aStream.read( buffer );
            if ( newBytes == -1 ) break;
            output.write( buffer, 0, newBytes );
            }
        aStream.close();
        output.close();

        return output.toByteArray();
        }

    // Protected API

    protected abstract ImageResource loadImageResourceDo( String aResourcePath, final InputStream aStream ) throws IOException;

    protected abstract InputStream openStreamDo( final String aResourcePath );

    // Implementation

    private String makeSubfolderPath( final Object aSubfolderPath, final String aResourcePath )
        {
        myFolderBuffer.setLength( 0 );
        myFolderBuffer.append( aSubfolderPath );
        myFolderBuffer.append( "/" );
        myFolderBuffer.append( aResourcePath );
        return myFolderBuffer.toString();
        }


    private final DynamicArray mySubfolders = new DynamicArray();

    private final StringBuffer myFolderBuffer = new StringBuffer();

    private static final int STREAM_COPY_BUFFER_SIZE_IN_BYTES = 4096;
    }
