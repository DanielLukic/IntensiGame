package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.util.Position;



public class ImageScreen extends ScreenBase
    {
    public static final int MODE_ABSOLUTE = 0;

    public static final int MODE_RELATIVE = 1;

    public final Position position = new Position();

    public int alignment = DirectGraphics.ALIGN_DEFAULT;

    public boolean clearOutside;

    public int clearColorRGB24;

    public ImageResource image;

    public int positionMode;



    public ImageScreen( final ImageResource aImage )
        {
        this( aImage, 50, 50, MODE_RELATIVE );
        }

    public ImageScreen( final ImageResource aImage, final int aX, final int aY, final int aMode )
        {
        image = aImage;
        position.x = aX;
        position.y = aY;
        positionMode = aMode;
        }

    // From ScreenBase

    public void onControlTick()
        {
        }

    public void onDrawFrame()
        {
        if ( image == null || image == NullImageResource.NULL ) return;

        final Position position = getPosition();
        final DirectGraphics gc = graphics();

        if ( clearOutside )
            {
            final int screenWidth = screen().width();
            final int screenHeight = screen().height();

            final int bottom = position.y + image.getHeight();
            final int right = position.x + image.getWidth();

            gc.setColorRGB24( clearColorRGB24 );
            gc.fillRect( 0, 0, screenWidth, position.y );
            gc.fillRect( 0, position.y, position.x, image.getHeight() );
            gc.fillRect( right, position.y, screenWidth - right, image.getHeight() );
            gc.fillRect( 0, bottom, screenWidth, screenHeight - bottom );
            }

        gc.drawImage( image, position.x, position.y, alignment );
        }

    // Protected Interface

    protected Position getPosition()
        {
        switch ( positionMode )
            {
            case MODE_ABSOLUTE:
                myBlitPosition.x = position.x;
                myBlitPosition.y = position.y;
                break;
            case MODE_RELATIVE:
                myBlitPosition.x = ( screen().width() - image.getWidth() ) * position.x / 100;
                myBlitPosition.y = ( screen().height() - image.getHeight() ) * position.y / 100;
                break;
            default:
                throw new IllegalStateException();
            }
        return myBlitPosition;
        }



    private final Position myBlitPosition = new Position();
    }
