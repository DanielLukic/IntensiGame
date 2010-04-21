package core;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;

import java.io.*;

public final class FakeResourcesManager extends ResourcesManager
    {
    public FontGenerator getSmallDefaultFont()
        {
        throw new RuntimeException( "nyi" );
        }

    public ImageResource createImageResource( final int aWidth, final int aHeight )
        {
        throw new RuntimeException( "nyi" );
        }

    public ImageResource loadImageResource( final String aResourcePath ) throws IOException
        {
        throw new RuntimeException( "nyi" );
        }

    public InputStream openStream( final String aResourcePath )
        {
        return getClass().getResourceAsStream( aResourcePath );
        }
    }
