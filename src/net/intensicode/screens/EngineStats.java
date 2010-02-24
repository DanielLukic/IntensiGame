package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;
import net.intensicode.core.DirectGraphics;

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

        myBlitPos.y = 0;

        myBlitPos.x = width - myFontGen.maxCharWidth() * 3;
        myFontGen.blitNumber( graphics, myBlitPos, timing().measuredFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - myFontGen.maxCharWidth() * 3;
        myFontGen.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width;
        myFontGen.blitNumber( graphics, myBlitPos, timing().maxFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.y = myFontGen.charHeight();

        myBlitPos.x = width - myFontGen.maxCharWidth() * 3;
        myFontGen.blitNumber( graphics, myBlitPos, timing().measuredTicksPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = width - myFontGen.maxCharWidth() * 3;
        myFontGen.blitChar( graphics, myBlitPos.x, myBlitPos.y, '/' );

        myBlitPos.x = width;
        myFontGen.blitNumber( graphics, myBlitPos, timing().ticksPerSecond, FontGenerator.RIGHT );
        }


    private final FontGenerator myFontGen;

    private final Position myBlitPos = new Position();
    }
