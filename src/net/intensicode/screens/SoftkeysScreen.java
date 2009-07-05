/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;

import javax.microedition.lcdui.Graphics;



public class SoftkeysScreen extends AbstractScreen
    {
    public SoftkeysScreen( final FontGen aFont )
        {
        myFontGen = aFont;
        }

    public final void setSoftkeys( final String aLeftButton, final String aRightButton )
        {
        myLeftButton = aLeftButton;
        myRightButton = aRightButton;
        }

    public final void setInsets( final int aX, final int aY )
        {
        myOffsetX = aX;
        myOffsetY = aY;
        }

    // From AbstractScreen

    public void onControlTick( final Engine aEngine )
        {
        }

    public void onDrawFrame( final DirectScreen aScreen )
        {
        if ( myLeftButton != null )
            {
            myBlitPosition.x = myOffsetX;
            myBlitPosition.y = aScreen.height() - myOffsetY;

            final Graphics gc = aScreen.graphics();
            myFontGen.blitString( gc, myLeftButton, myBlitPosition, FontGen.LEFT | FontGen.BOTTOM );
            }

        if ( myRightButton != null )
            {
            myBlitPosition.x = aScreen.width() - myOffsetX;
            myBlitPosition.y = aScreen.height() - myOffsetY;

            final Graphics gc = aScreen.graphics();
            myFontGen.blitString( gc, myRightButton, myBlitPosition, FontGen.RIGHT | FontGen.BOTTOM );
            }
        }



    protected int myOffsetX = 4;

    protected int myOffsetY = 2;

    protected String myLeftButton;

    protected String myRightButton;

    protected final FontGen myFontGen;

    protected final Position myBlitPosition = new Position();
    }
