package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public final class EngineStats extends ScreenBase
    {
    public static boolean show = true;


    public EngineStats( final FontGenerator aFontGen )
        {
        myFontGen = aFontGen;
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
        graphics.fillRect( width - myFontGen.maxCharWidth() * 7, 0, myFontGen.maxCharWidth() * 7, myFontGen.charHeight() * 4 );

        myBlitPos.y = myFontGen.charHeight();

        myBlitPos.x = width - myFontGen.maxCharWidth() * 4;
        myFontGen.blitNumber( graphics, myBlitPos, timing().measuredFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - myFontGen.maxCharWidth() * 4;
        myFontGen.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width - myFontGen.maxCharWidth();
        myFontGen.blitNumber( graphics, myBlitPos, timing().maxFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.y = myFontGen.charHeight() * 2;

        myBlitPos.x = width - myFontGen.maxCharWidth() * 4;
        myFontGen.blitNumber( graphics, myBlitPos, timing().measuredTicksPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - myFontGen.maxCharWidth() * 4;
        myFontGen.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width - myFontGen.maxCharWidth();
        myFontGen.blitNumber( graphics, myBlitPos, timing().ticksPerSecond, FontGenerator.RIGHT );
        }


    private final FontGenerator myFontGen;

    private final Position myBlitPos = new Position();
    }
