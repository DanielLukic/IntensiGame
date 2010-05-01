package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;
import net.intensicode.touch.TouchableArea;

public final class MenuEntry extends ScreenBase
    {
    public static int selectorColor = 0x807F0000;

    //#if TOUCH
    public final TouchableArea touchable = new TouchableArea();
    //#endif

    public final FontGenerator fontGen;

    public final Position position;

    public final String text;

    public boolean selectedState;

    public int id;


    public MenuEntry( final FontGenerator aCharGen, final String aText, final Position aPosition )
        {
        fontGen = aCharGen;
        text = aText;
        position = aPosition;
        //#if TOUCH
        touchable.associatedObject = this;
        updateTouchable();
        //#endif
        }

    public final void setSelected( boolean aSelectedFlag )
        {
        selectedState = aSelectedFlag;
        }

    //#if TOUCH

    public final void updateTouchable()
        {
        final Rectangle rectangle = touchable.rectangle;
        rectangle.width = fontGen.stringWidth( text );
        rectangle.height = fontGen.charHeight();
        rectangle.x = position.x - rectangle.width / 2;
        rectangle.y = position.y - rectangle.height / 2;
        rectangle.applyOutsets( 1 + rectangle.height / 4 );
        }

    //#endif

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        final DirectGraphics graphics = graphics();

        if ( selectedState )
            {
            final int x = 0;
            final int y = position.y - fontGen.charHeight() / 2;
            final int width = screen().width();
            final int height = fontGen.charHeight();
            graphics.setColorARGB32( selectorColor );
            graphics.fillRect( x, y, width, height );
            }

        fontGen.blitString( graphics, text, position, FontGenerator.CENTER );
        }
    }
