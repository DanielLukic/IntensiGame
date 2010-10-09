package net.intensicode.graphics;

import net.intensicode.core.DirectGraphics;

public final class ChangeColorCommand implements GraphicsCommand
    {
    public final ChangeColorCommand set( final int aColorARGB32 )
        {
        myColorARGB32 = aColorARGB32;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.setColorARGB32( myColorARGB32 );
        }

    private int myColorARGB32;
    }
