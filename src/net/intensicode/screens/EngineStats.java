package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public final class EngineStats extends ScreenBase
    {
    public static boolean show = true;

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

        final DirectGraphics graphics = graphics();

        final int width = screen().width();

        graphics.setColorARGB32( 0x80000000 );
        graphics.fillRect( width - font.maxCharWidth() * 7, 0, font.maxCharWidth() * 7, font.charHeight() * 4 );

        myBlitPos.y = font.charHeight();

        myBlitPos.x = width - font.maxCharWidth() * 4;
        font.blitNumber( graphics, myBlitPos, timing().measuredFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - font.maxCharWidth() * 4;
        font.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width - font.maxCharWidth();
        font.blitNumber( graphics, myBlitPos, timing().maxFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.y = font.charHeight() * 2;

        myBlitPos.x = width - font.maxCharWidth() * 4;
        font.blitNumber( graphics, myBlitPos, timing().measuredTicksPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - font.maxCharWidth() * 4;
        font.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width - font.maxCharWidth();
        font.blitNumber( graphics, myBlitPos, timing().ticksPerSecond, FontGenerator.RIGHT );
        }


    private final Position myBlitPos = new Position();
    }
