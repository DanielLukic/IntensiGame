//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchableImage extends Touchable
    {
    public final Position position = new Position();

    public int imageAlignment = DirectGraphics.ALIGN_CENTER;

    public ImageResource image;


    public final void updateTouchableRect()
        {
        final int x = position.x;
        final int y = position.y;
        final int width = image.getWidth();
        final int height = image.getHeight();
        final Position aligned = DirectGraphics.getAlignedPosition( x, y, width, height, imageAlignment );
        myTouchableRect.x = aligned.x;
        myTouchableRect.y = aligned.y;
        myTouchableRect.width = width;
        myTouchableRect.height = height;
        }

    // From Touchable

    public final void onDraw( final DirectGraphics aGraphics )
        {
        aGraphics.blendImage( image, myTouchableRect.x, myTouchableRect.y, alpha256 );

        //#if DEBUG
        aGraphics.setColorARGB32( 0x3000FF00 );
        aGraphics.fillRect( myTouchableRect.x, myTouchableRect.y, myTouchableRect.width, myTouchableRect.height );
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
