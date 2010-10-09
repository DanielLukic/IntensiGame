package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class ClearCommand implements GraphicsCommand
    {
    public final ClearCommand set( final int aColorARGB32 )
        {
        myColorARGB32 = aColorARGB32;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.clearARGB32( myColorARGB32 );
        }

    private int myColorARGB32;
    }
