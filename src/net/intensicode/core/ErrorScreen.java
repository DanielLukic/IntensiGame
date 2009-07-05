/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import net.intensicode.util.FontGen;
import net.intensicode.util.Position;
import net.intensicode.util.Rectangle;

import javax.microedition.lcdui.Graphics;
import java.util.Vector;



/**
 * TODO: Describe this!
 */
public final class ErrorScreen extends AbstractScreen
    {
    public ErrorScreen( final FontGen aFont )
        {
        myFont = aFont;
        }

    public final void reset()
        {
        myMessage = null;
        myCauses.removeAllElements();
        myAnimCounter = 0;
        }

    public final void setMessage( final String aMessage )
        {
        myMessage = aMessage;
        }

    public final void addCause( final Throwable aCause )
        {
        myCauses.addElement( aCause.toString() );
        }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        myAnimCounter++;
        if ( myAnimCounter == Engine.ticksPerSecond * 2 ) myAnimCounter = 0;

        final Keys keys = aEngine.keys;
        if ( keys.checkLeftSoftAndConsume() || keys.checkFire1AndConsume() )
            {
            aEngine.popScreen( this );
            }
        else if ( keys.checkRightSoftAndConsume() || keys.checkFire2AndConsume() )
            {
            aEngine.shutdownAndExit();
            }
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        final int screenWidth = aScreen.width();
        final int screenHeight = aScreen.height();

        final Graphics gc = aScreen.graphics();
        gc.setColor( 0x000000 );
        gc.fillRect( 0, 0, aScreen.width(), aScreen.height() );
        gc.setColor( 0xCC0000 );

        if ( myAnimCounter < Engine.ticksPerSecond )
            {
            gc.fillRect( 0, 0, screenWidth, BORDER_WIDTH );
            gc.fillRect( 0, screenHeight - BORDER_WIDTH, screenWidth, BORDER_WIDTH );
            gc.fillRect( 0, 0, BORDER_WIDTH, screenHeight );
            gc.fillRect( screenWidth - BORDER_WIDTH, 0, BORDER_WIDTH, screenHeight );
            }

        if ( myMessage != null && myMessage.length() > 0 )
            {
            myTextRect.x = 16;
            myTextRect.y = 16;
            myTextRect.width = screenWidth - 32;
            myTextRect.height = 64;
            myFont.blitText( gc, myMessage, myTextRect );
            }

        for ( int cause = 0; cause < myCauses.size(); cause++ )
            {
            myTextRect.x = 16;
            myTextRect.y = 80;
            myTextRect.width = screenWidth - 32;
            myTextRect.height = screenHeight - 80 - 16;
            myFont.blitText( gc, myCauses.elementAt( cause ).toString(), myTextRect );
            }

        myBlitPos.y = screenHeight - BORDER_WIDTH * 2;

        myBlitPos.x = BORDER_WIDTH * 2;
        myFont.blitString( gc, "CONTINUE", myBlitPos, FontGen.LEFT | FontGen.BOTTOM );

        myBlitPos.x = screenWidth - BORDER_WIDTH * 2;
        myFont.blitString( gc, "EXIT", myBlitPos, FontGen.RIGHT | FontGen.BOTTOM );
        }



    private String myMessage;

    private int myAnimCounter = 0;


    private final FontGen myFont;

    private final Vector myCauses = new Vector();

    private final Position myBlitPos = new Position();


    private static final int BORDER_WIDTH = 6;

    private final Rectangle myTextRect = new Rectangle();
    }
