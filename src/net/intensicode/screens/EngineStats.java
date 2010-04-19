package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.graphics.*;
import net.intensicode.util.Position;

public final class EngineStats extends ScreenBase
    {
    //#if FALSE
    public static boolean show = true;
    //#else
    //# public static boolean show = false;
    //#endif

    public FontGenerator font;


    public EngineStats( final FontGenerator aFontGen )
        {
        font = aFontGen;
        }

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        if ( !show ) return;

        //#if ANDROID
        if ( font instanceof SystemFontGenerator ) return;
        //#endif

        final DirectGraphics graphics = graphics();

        final int width = screen().width();

        graphics.setColorARGB32( 0x80000000 );
        graphics.fillRect( width - font.maxCharWidth() * 6, 0, font.maxCharWidth() * 6, font.charHeight() * 3 );

        myBlitPos.y = font.charHeight() / 2;

        myBlitPos.x = width - font.maxCharWidth() * 4 + font.maxCharWidth() / 2;
        font.blitNumber( graphics, myBlitPos, timing().measuredFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - font.maxCharWidth() * 4 + font.maxCharWidth() / 2;
        font.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width - font.maxCharWidth() + font.maxCharWidth() / 2;
        font.blitNumber( graphics, myBlitPos, timing().maxFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.y = font.charHeight() * 3 / 2;

        myBlitPos.x = width - font.maxCharWidth() * 4 + font.maxCharWidth() / 2;
        font.blitNumber( graphics, myBlitPos, timing().measuredTicksPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - font.maxCharWidth() * 4 + font.maxCharWidth() / 2;
        font.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width - font.maxCharWidth() + font.maxCharWidth() / 2;
        font.blitNumber( graphics, myBlitPos, timing().ticksPerSecond, FontGenerator.RIGHT );
        }


    private final Position myBlitPos = new Position();
    }
