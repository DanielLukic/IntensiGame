/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.screens;

import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.util.FontGen;

import javax.microedition.lcdui.Graphics;



public final class AutoHideSoftkeysScreen extends SoftkeysScreen
    {
    public AutoHideSoftkeysScreen( final FontGen aFontGen )
        {
        super( aFontGen );
        }

    public final void setSoftkeys( final String aLeftButton, final String aRightButton, final boolean aAutoHide )
        {
        setSoftkeys( aLeftButton, aRightButton );
        myMakeVisibleFlag = true;
        myAutoHide = aAutoHide;
        }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine )
        {
        super.onControlTick( aEngine );
        if ( myMakeVisibleFlag )
            {
            myVisibleTicks = Engine.ticksPerSecond * 3 / 2;
            myHideTick = myHideTicks = Engine.ticksPerSecond * 2 / 3;
            myMakeVisibleFlag = false;
            }
        else if ( myVisibleTicks > 0 )
            {
            myVisibleTicks--;
            }
        else if ( myHideTick > 0 && myAutoHide )
            {
            myHideTick--;
            }
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        final int maxOffset = ( myOffsetY + myFontGen.charHeight() );
        final int yOffset = myHideTicks == 0 ? 0 : maxOffset - myHideTick * maxOffset / myHideTicks;

        myOffsetY -= yOffset;
        super.onDrawFrame( aScreen );
        myOffsetY += yOffset;
        }



    private int myHideTick;

    private int myHideTicks;

    private int myVisibleTicks;

    private boolean myAutoHide;

    private boolean myMakeVisibleFlag;
    }
