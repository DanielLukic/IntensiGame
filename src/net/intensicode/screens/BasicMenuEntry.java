package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.*;
import net.intensicode.util.*;

public class BasicMenuEntry extends ScreenBase implements PositionableEntry
    {
    public static int selectorColor = 0x807F0000;

    //#if TOUCH
    public final net.intensicode.touch.TouchableArea touchable = new net.intensicode.touch.TouchableArea();
    //#endif

    public final Position position = new Position();

    public SpriteGenerator imageGenerator;

    public FontGenerator fontGen;

    public ImageResource image;

    public boolean selected;

    public String text;

    public int width;

    public int id;


    public BasicMenuEntry()
        {
        //#if TOUCH
        touchable.associatedObject = this;
        //#endif
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
        else if ( image != null )
            {
            rectangle.width = image.getWidth();
            rectangle.height = image.getHeight();
            }
        else
            {
            rectangle.width = fontGen.stringWidth( text );
            rectangle.height = fontGen.charHeight();
            }

        rectangle.x = position.x + ( width - rectangle.width ) / 2;
        rectangle.y = position.y - rectangle.height / 2;

        if ( imageGenerator == null && image == null )
            {
            rectangle.applyOutsets( 1 + rectangle.height / 4 );
            }
        }

    //#endif

    // From PositionableEntry

    public final Position getPositionByReference()
        {
        return position;
        }

    public final void setAvailableWidth( final int aWidthInPixels )
        {
        width = aWidthInPixels;
        }

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
        else if ( image != null )
            {
            final int x = posX - image.getWidth() / 2;
            final int y = position.y - image.getHeight() / 2;

            graphics.drawImage( image, x, y, DirectGraphics.ALIGN_CENTER );
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

        blitText( graphics );
        }

    // Protected API

    protected void blitText( final DirectGraphics aGraphics )
        {
        myBlitPos.setTo( position );
        myBlitPos.x += width / 2;
        fontGen.blitString( aGraphics, text, myBlitPos, FontGenerator.CENTER );
        }


    protected final Position myBlitPos = new Position();
    }
