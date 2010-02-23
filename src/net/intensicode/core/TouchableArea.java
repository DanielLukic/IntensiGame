//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchableArea extends Touchable
    {
    public final Rectangle rectangle = new Rectangle();

    // From Touchable

    public final void onDraw( final DirectGraphics aGraphics )
        {
        // Not visible..

        //#if DEBUG
        aGraphics.setColorARGB32( 0x3000FF00 );
        aGraphics.fillRect( rectangle.x, rectangle.y, rectangle.width, rectangle.height );
        aGraphics.setColorARGB32( 0xFF000000 );
        //#endif
        }

    // From Object

    //#if DEBUG

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( ",rect=" );
        buffer.append( rectangle );
        return buffer.toString();
        }

    //#endif

    // Protected Interface

    protected boolean isInside( final TouchEvent aTouchEvent )
        {
        return rectangle.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        }
    }
