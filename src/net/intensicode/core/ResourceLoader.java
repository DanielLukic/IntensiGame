/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import net.intensicode.util.Log;

import javax.microedition.lcdui.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * TODO: Describe this!
 */
public final class ResourceLoader
    {
    public ResourceLoader( final Class aReferenceClass )
        {
        myReferenceClass = aReferenceClass;
        }

    public final InputStream openChecked( final String aResourcePath ) throws IOException
        {
        final InputStream resource = openStream( aResourcePath );
        if ( resource == null ) throw new IOException( aResourcePath );
        return resource;
        }

    public final InputStream openStream( final String aResourcePath )
        {
        return myReferenceClass.getResourceAsStream( aResourcePath );
        }

    public final byte[] loadData( final String aResourcePath ) throws IOException
        {
        return loadStream( openChecked( aResourcePath ) );
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

    public final Image loadImage( final String aResourcePath ) throws IOException
        {
        //#if DEBUG
        Log.debug( "Loading image {}", aResourcePath );
        //#endif
        return Image.createImage( openStream( aResourcePath ) );
        }

    public static final byte[] loadStream( final InputStream aStream ) throws IOException
        {
        if ( aStream == null ) return null;

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[4096];
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



    private final Class myReferenceClass;
    }
