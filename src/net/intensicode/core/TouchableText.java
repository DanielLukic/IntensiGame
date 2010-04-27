//#condition TOUCH

package net.intensicode.core;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class TouchableText extends Touchable
    {
    public final Position position = new Position();

    public int alignment = FontGenerator.CENTER;

    public FontGenerator font;

    public String text;


    public final void updateTouchableRect()
        {
        final int width = font.stringWidth( text );
        final int height = font.charHeight();
        final Position aligned = FontGenerator.getAlignedPosition( position, width, height, alignment );
        myTouchableRect.x = aligned.x;
        myTouchableRect.y = aligned.y;
        myTouchableRect.width = width;
        myTouchableRect.height = height;
        }

    // From Touchable

    public final void onDraw( final DirectGraphics aGraphics )
        {
        if ( activated ) onDrawActivated( aGraphics, myTouchableRect );
        drawText( aGraphics );
        //#if DEBUG_TOUCH
        onDrawDebug( aGraphics, myTouchableRect );
        //#endif
        }

    private void drawText( final DirectGraphics aGraphics )
        {
        if ( alpha256 == DirectGraphics.FULLY_TRANSPARENT ) return;
        if ( alpha256 == DirectGraphics.FULLY_OPAQUE ) font.blitString( aGraphics, text, position, alignment );
        else font.blendString( aGraphics, text, position, alignment, alpha256 );
        }

    // From Object

    //#if DEBUG

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( ",rect=" );
        buffer.append( myTouchableRect );
        return buffer.toString();
        }

    //#endif

    // Protected Interface

    protected boolean isInside( final TouchEvent aTouchEvent )
        {
        return myTouchableRect.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        }


    private final Rectangle myTouchableRect = new Rectangle();
    }
