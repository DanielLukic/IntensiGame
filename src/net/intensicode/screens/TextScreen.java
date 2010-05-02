package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Rectangle;

public final class TextScreen extends ScreenBase
    {
    public Rectangle optionalBoundingBox;

    public FontGenerator font;

    public String text;


    public void onInitEverytime() throws Exception
        {
        if ( fullScreenRect == null ) return;
        fullScreenRect.width = screen().width();
        fullScreenRect.height = screen().height();
        }

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        if ( font == null || text == null || text.length() == 0 ) return;

        final Rectangle textRect = determineTextRect();
        font.blitText( graphics(), text, textRect );
        }

    private Rectangle determineTextRect()
        {
        if ( optionalBoundingBox != null ) return optionalBoundingBox;
        if ( fullScreenRect != null ) return fullScreenRect;
        fullScreenRect = new Rectangle( 0, 0, screen().width(), screen().height() );
        return fullScreenRect;
        }


    private Rectangle fullScreenRect;
    }
