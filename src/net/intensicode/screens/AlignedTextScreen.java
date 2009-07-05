package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public final class AlignedTextScreen extends AbstractScreen
    {
    public final Position position = new Position();

    public int alignment = FontGen.CENTER;

    public String text;



    public AlignedTextScreen( final FontGen aFontGen )
        {
        myFontGen = aFontGen;
        }

    public AlignedTextScreen( final FontGen aFontGen, final String aText )
        {
        this( aFontGen );
        text = aText;
        }

    public AlignedTextScreen( final FontGen aFontGen, final String aText, final int aX, final int aY, final int aAlignment )
        {
        this( aFontGen, aText );
        position.x = aX;
        position.y = aY;
        alignment = aAlignment;
        }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        if ( text != null ) myFontGen.blitString( aScreen.graphics(), text, position, alignment );
        }

    private final FontGen myFontGen;
    }
