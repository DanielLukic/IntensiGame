package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

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
        final int xOffset = getOffsetX( aText );
        final int yOffset = getOffsetY( aText );
        final Position aligned = DirectGraphics.getAlignedPosition( x, y, alignWidth, charHeight, aAlignment );

        final int maxCharWidth = myFontGen.maxCharWidth( aText );

        final DirectGraphics graphics = graphics();
        for ( int idx = 0; idx < aText.length(); idx++ )
            {
            final int yPos = aligned.y + yOffset + charHeight * idx;

            final char charCode = aText.charAt( idx );
            final int charWidth = myFontGen.charWidth( charCode );

            final int xCentered = aligned.x + xOffset + ( maxCharWidth - charWidth ) / 2;
            myFontGen.blitChar( graphics, xCentered, yPos, charCode );
            }
        }

    protected int getOffsetX( final String aText )
        {
        if ( myButtonImage != null ) return ( myButtonImage.getWidth() - myFontGen.maxCharWidth( aText )) / 2;
        else return myInsetX;
        }

    protected int getOffsetY( final String aText )
        {
        if ( myButtonImage != null ) return ( myButtonImage.getHeight() - getTextHeight( aText ) ) / 2;
        else return myInsetY;
        }
    }
