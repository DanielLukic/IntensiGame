package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.graphics.SpriteGenerator;
import net.intensicode.touch.Touchable;
import net.intensicode.util.*;

public class ButtonStyleMenuEntry extends ScreenBase implements ButtonStyleEntry, BasicMenuEntry, PositionableEntry
    {
    public static int selectorColor = 0x807F0000;

    //#if TOUCH
    public final net.intensicode.touch.TouchableArea touchable = new net.intensicode.touch.TouchableArea();
    //#endif

    public final Position position = new Position();

    public SpriteGenerator imageGenerator;

    public FontGenerator fontGen;

    public boolean selected;

    public String text;

    public int width;

    public int id;


    public ButtonStyleMenuEntry()
        {
        //#if TOUCH
        touchable.associatedObject = this;
        //#endif
        }

    // From ButtonStyleEntry

    public final void setButtonGenerator( final SpriteGenerator aButtonGenerator )
        {
        imageGenerator = aButtonGenerator;
        }

    // From BasicMenuEntry

    public final int id()
        {
        return id;
        }

    public final void setSelected( final boolean aSelectedFlag )
        {
        selected = aSelectedFlag;
        }

    public final ScreenBase visual()
        {
        return this;
        }

    public final Touchable touchable()
        {
        return touchable;
        }

    // From PositionableEntry

    public final Position getPositionByReference()
        {
        return position;
        }

    public final void setAvailableWidth( final int aWidthInPixels )
        {
        width = aWidthInPixels;
        }

    //#if TOUCH

    public final void updateTouchable()
        {
        final Rectangle rectangle = touchable.rectangle;
        if ( imageGenerator != null )
            {
            rectangle.width = imageGenerator.getWidth();
            rectangle.height = imageGenerator.getHeight();
            }
        else
            {
            rectangle.width = fontGen.stringWidth( text );
            rectangle.height = fontGen.charHeight();
            }

        rectangle.x = position.x + ( width - rectangle.width ) / 2;
        rectangle.y = position.y - rectangle.height / 2;

        if ( imageGenerator == null )
            {
            rectangle.applyOutsets( 1 + rectangle.height / 4 );
            }
        }

    //#endif

    // From ScreenBase

    public void onControlTick() throws Exception
        {
        }

    public void onDrawFrame()
        {
        final DirectGraphics graphics = graphics();

        final int posX = position.x + width / 2;

        if ( imageGenerator != null && imageGenerator != SpriteGenerator.NULL )
            {
            final int x = posX - imageGenerator.getWidth() / 2;
            final int y = position.y - imageGenerator.getHeight() / 2;

            imageGenerator.paint( graphics, x, y, selected ? 1 : 0 );
            }
        else if ( selected )
            {
            final int x = 0;
            final int y = position.y - fontGen.charHeight() / 2;
            final int width = screen().width();
            final int height = fontGen.charHeight();
            graphics.setColorARGB32( selectorColor );
            graphics.fillRect( x, y, width, height );
            }

        blitEntry( graphics );
        }

    // Protected API

    protected void blitEntry( final DirectGraphics aGraphics )
        {
        myBlitPos.setTo( position );
        myBlitPos.x += width / 2;
        fontGen.blitString( aGraphics, text, myBlitPos, FontGenerator.CENTER );
        }


    protected final Position myBlitPos = new Position();
    }
