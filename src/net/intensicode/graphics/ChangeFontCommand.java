package net.intensicode.graphics;

import net.intensicode.core.*;

public final class ChangeFontCommand implements GraphicsCommand
    {
    public final ChangeFontCommand set( final FontResource aFontResource )
        {
        myFontResource = aFontResource;
        return this;
        }

    public final void execute( final DirectGraphics aGraphics )
        {
        aGraphics.setFont( myFontResource );
        }

    private FontResource myFontResource;
    }
