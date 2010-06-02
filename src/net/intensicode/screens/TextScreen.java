package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class TextScreen extends ScreenBase
    {
    public int alignment = FontGenerator.TOP_LEFT;

    public Rectangle optionalBoundingBox;

    public boolean updateRequired;

    public FontGenerator font;

    public String text;


    public void onInitEverytime() throws Exception
        {
        if ( myFullScreenRect == null ) return;
        myFullScreenRect.width = screen().width();
        myFullScreenRect.height = screen().height();
        updateRequired = true;
        }

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        if ( font == null || text == null || text.length() == 0 ) return;

        final Rectangle bounds = determineTextRect();
        updateTextLinesIfNecessary( bounds.width );

        final int textHeight = myLines.size * font.charHeight();

        myBlitPos.y = bounds.y;
        if ( ( alignment & FontGenerator.VCENTER ) != 0 ) myBlitPos.y += ( bounds.height - textHeight ) / 2;
        if ( ( alignment & FontGenerator.BOTTOM ) != 0 ) myBlitPos.y += bounds.height - textHeight;

        for ( int idx = 0; idx < myLines.size; idx++ )
            {
            final String line = (String) myLines.get( idx );
            final int lineWidth = font.stringWidth( line );

            myBlitPos.x = bounds.x;
            if ( ( alignment & FontGenerator.HCENTER ) != 0 ) myBlitPos.x += ( bounds.width - lineWidth ) / 2;
            if ( ( alignment & FontGenerator.RIGHT ) != 0 ) myBlitPos.x += bounds.width - lineWidth;

            font.blitString( graphics(), line, myBlitPos, FontGenerator.TOP_LEFT );
            myBlitPos.y += font.charHeight();
            }
        }

    private void updateTextLinesIfNecessary( final int aWidth )
        {
        if ( !updateRequired ) return;

        myFormatter.blockWidth = aWidth;
        myFormatter.font = font;
        myFormatter.text = text;

        myLines.clear();
        myFormatter.formatLinesInto( myLines );

        updateRequired = false;
        }

    private Rectangle determineTextRect()
        {
        if ( optionalBoundingBox != null ) return optionalBoundingBox;
        if ( myFullScreenRect != null ) return myFullScreenRect;
        myFullScreenRect = new Rectangle( 0, 0, screen().width(), screen().height() );
        return myFullScreenRect;
        }


    private Rectangle myFullScreenRect;

    private final Position myBlitPos = new Position();

    private final DynamicArray myLines = new DynamicArray();

    private final TextBlockFormatter myFormatter = new TextBlockFormatter();
    }
