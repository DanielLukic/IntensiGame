package core;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;

import java.io.InputStream;

public final class FakeResourcesManager extends ResourcesManager
    {
    public FontGenerator getSmallDefaultFont()
        {
        throw new RuntimeException( "nyi" );
        }

    public int maxImageResourceSize()
        {
        throw new RuntimeException( "nyi" );
        }

    public ImageResource createImageResource( final int aWidth, final int aHeight )
        {
        throw new RuntimeException( "nyi" );
        }

    public ImageResource loadImageResourceDo( final String aResourcePath, final InputStream aStream )
        {
        throw new RuntimeException( "nyi" );
        }

    public InputStream openStreamDo( final String aResourcePath )
        {
        return getClass().getResourceAsStream( aResourcePath );
        }
    }
