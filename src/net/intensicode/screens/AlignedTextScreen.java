package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public final class AlignedTextScreen extends ScreenBase
    {
    public final Position position = new Position();

    public int alignment = FontGenerator.CENTER;

    public String text;



    public AlignedTextScreen( final FontGenerator aFontGen )
        {
        myFontGen = aFontGen;
        }

    public AlignedTextScreen( final FontGenerator aFontGen, final String aText )
        {
        this( aFontGen );
        text = aText;
        }

    public AlignedTextScreen( final FontGenerator aFontGen, final String aText, final int aX, final int aY, final int aAlignment )
        {
        this( aFontGen, aText );
        position.x = aX;
        position.y = aY;
        alignment = aAlignment;
        }

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        if ( text != null ) myFontGen.blitString( graphics(), text, position, alignment );
        }

    private final FontGenerator myFontGen;
    }
