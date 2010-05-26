package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.*;
import net.intensicode.util.*;

public final class BasicMenuEntry extends ScreenBase
    {
    public static int selectorColor = 0x807F0000;

    //#if TOUCH
    public final net.intensicode.touch.TouchableArea touchable = new net.intensicode.touch.TouchableArea();
    //#endif

    public final Position position = new Position();

    public final FontGenerator fontGen;

    public final String text;


    public SpriteGenerator imageGenerator;

    public ImageResource image;

    public boolean selectedState;

    public int id;


    public BasicMenuEntry( final int aId, final String aText, final FontGenerator aCharGen )
        {
        id = aId;
        text = aText;
        fontGen = aCharGen;
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

        rectangle.x = position.x - rectangle.width / 2;
        rectangle.y = position.y - rectangle.height / 2;

        if ( imageGenerator == null && image == null )
            {
            rectangle.applyOutsets( 1 + rectangle.height / 4 );
            }
        }

    //#endif

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        }

    public final void onDrawFrame()
        {
        final DirectGraphics graphics = graphics();

        if ( imageGenerator != null && imageGenerator != SpriteGenerator.NULL )
            {
            imageGenerator.paint( graphics, position.x, position.y, selectedState ? 1 : 0 );
            }
        else if ( image != null )
            {
            graphics.drawImage( image, position.x, position.y, DirectGraphics.ALIGN_CENTER );
            }
        else if ( selectedState )
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
