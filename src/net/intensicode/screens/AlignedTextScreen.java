package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public final class AlignedTextScreen extends ScreenBase
    {
    public final Position position = new Position();

    public int alignment = FontGenerator.CENTER;

    public FontGenerator font;

    public String text;


    public AlignedTextScreen()
        {
        }

    public AlignedTextScreen( final FontGenerator aFontGen )
        {
        font = aFontGen;
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

    public final AlignedTextScreen font( final FontGenerator aFontGenerator )
        {
        font = aFontGenerator;
        return this;
        }

    public final AlignedTextScreen text( final String aText )
        {
        text = aText;
        return this;
        }

    public final AlignedTextScreen align( final int aAlignment )
        {
        alignment = aAlignment;
        return this;
        }

    public final AlignedTextScreen position( final Position aPosition )
        {
        position.setTo( aPosition );
        return this;
        }

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        if ( text != null ) font.blitString( graphics(), text, position, alignment );
        }
    }
