package net.intensicode.core;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Log;

import java.io.*;

public abstract class ResourcesManager
    {
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

    public abstract ImageResource loadImageResource( String aResourcePath ) throws IOException;

    public abstract InputStream openStream( final String aResourcePath );

    public final InputStream openStreamChecked( final String aResourcePath ) throws IOException
        {
        final InputStream resource = openStream( aResourcePath );
        if ( resource == null ) throw new IOException( aResourcePath );
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
        return new Configuration( data );
        }

    public final Configuration loadConfigurationOrUseDefaults( final String aResourcePath )
        {
        try
            {
            return loadConfiguration( aResourcePath );
            }
        catch ( final IOException e )
            {
            Log.error( "Failed loading {} configuration. Returning empty configuration.", aResourcePath, e );
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

    private static final int STREAM_COPY_BUFFER_SIZE_IN_BYTES = 4096;
    }
