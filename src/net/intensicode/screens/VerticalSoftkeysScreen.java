package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.screens.*;
import net.intensicode.util.*;

public class VerticalSoftkeysScreen extends SoftkeysScreen
    {
    public VerticalSoftkeysScreen( final FontGenerator aFont )
        {
        super( aFont );
        setPositionInPercent( 25 );
        }

    // Protected API

    protected int getAlignWidth( final String aText )
        {
        if ( myButtonImage != null ) return myButtonImage.getWidth();
        return myFontGen.maxCharWidth( aText ) + myInsetX * 2;
        }

    protected int getAlignHeight( final String aText )
        {
        if ( myButtonImage != null ) return myButtonImage.getHeight();
        else return getTextHeight( aText ) + myInsetY * 2;
        }

    protected int getTextHeight( final String aText )
        {
        return myFontGen.charHeight() * aText.length();
        }

    protected void blitTextString( final String aText, final int aAlignment )
        {
        final int alignWidth = getAlignWidth( aText );
        final int charHeight = myFontGen.charHeight();
        final int x = myPosition.x;
        final int y = myPosition.y;
        final Position aligned = DirectGraphics.getAlignedPosition( x, y, alignWidth, charHeight, aAlignment );

        final DirectGraphics graphics = graphics();
        for ( int idx = 0; idx < aText.length(); idx++ )
            {
            final int yPos = aligned.y + myInsetY + charHeight * idx;

            final char charCode = aText.charAt( idx );

            final int xCentered = aligned.x + ( alignWidth - myFontGen.charWidth( charCode ) ) / 2;
            myFontGen.blitChar( graphics, xCentered, yPos, charCode );
            }
        }
    }
