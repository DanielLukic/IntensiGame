package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.util.Position;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TODO: Describe this!
 */
public class ImageScreen extends AbstractScreen
    {
    public static final int MODE_ABSOLUTE = 0;

    public static final int MODE_RELATIVE = 1;

    public final Position position = new Position();

    public int alignment = Graphics.TOP | Graphics.LEFT;

    public boolean clearOutside = false;

    public Image image;

    public int mode;



    public ImageScreen( final Image aImage )
        {
        this( aImage, 50, 50, MODE_RELATIVE );
        }

    public ImageScreen( final Image aImage, final int aX, final int aY, final int aMode )
        {
        image = aImage;
        position.x = aX;
        position.y = aY;
        mode = aMode;
        }

    // From AbstractScreen

    public void onControlTick( final Engine aEngine )
        {
        }

    public void onDrawFrame( final DirectScreen aScreen )
        {
        if ( image == null ) return;

        final Position position = getPosition();
        final Graphics gc = aScreen.graphics();

        if ( clearOutside )
            {
            final int screenWidth = aScreen.width();
            final int screenHeight = aScreen.height();

            final int bottom = position.y + image.getHeight();
            final int right = position.x + image.getWidth();

            gc.setColor( 0 );
            gc.fillRect( 0, 0, screenWidth, position.y );
            gc.fillRect( 0, 0, position.x, screenHeight );
            gc.fillRect( 0, bottom, position.x, screenHeight - bottom );
            gc.fillRect( right, 0, screenWidth - right, screenHeight );
            }

        gc.drawImage( image, position.x, position.y, alignment );
        }

    // Protected Interface

    protected Position getPosition()
        {
        switch ( mode )
            {
            case MODE_ABSOLUTE:
                myBlitPosition.x = position.x;
                myBlitPosition.y = position.y;
                break;
            case MODE_RELATIVE:
                myBlitPosition.x = (screen().width() - image.getWidth()) * position.x / 100;
                myBlitPosition.y = (screen().height() - image.getHeight()) * position.y / 100;
                break;
            default:
                throw new IllegalStateException();
            }
        return myBlitPosition;
        }



    private final Position myBlitPosition = new Position();
    }
