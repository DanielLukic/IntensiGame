package net.intensicode.screens;

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

        myBlitPos.x = screen().width();
        myBlitPos.y = 0;
        myFontGen.blitNumber( graphics(), myBlitPos, timing().measuredFramesPerSecond, FontGenerator.RIGHT );

        myBlitPos.x = screen().width();
        myBlitPos.y = myFontGen.charHeight();
        myFontGen.blitNumber( graphics(), myBlitPos, timing().measuredTicksPerSecond, FontGenerator.RIGHT );
        }


    private final FontGenerator myFontGen;

    private final Position myBlitPos = new Position();
    }
