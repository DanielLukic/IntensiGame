package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;

public final class AutohideSoftkeysScreen extends SoftkeysScreen
    {
    public AutohideSoftkeysScreen( final FontGenerator aFontGen )
        {
        super( aFontGen );
        }

    public final void setSoftkeys( final String aLeftButton, final String aRightButton, final boolean aAutoHide )
        {
        setSoftkeys( aLeftButton, aRightButton );
        myMakeVisibleFlag = true;
        myAutoHide = aAutoHide;
        }

    // From ScreenBase

    public final void onControlTick()
        {
        super.onControlTick();
        if ( myMakeVisibleFlag )
            {
            final int tps = system().timing.ticksPerSecond;
            myVisibleTicks = tps * 3 / 2;
            myHideTick = myHideTicks = tps * 2 / 3;
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

    public final void onDrawFrame()
        {
        final int maxOffset = ( myOffsetY + myFontGen.charHeight() );
        final int yOffset = myHideTicks == 0 ? 0 : maxOffset - myHideTick * maxOffset / myHideTicks;

        myOffsetY -= yOffset;
        super.onDrawFrame();
        myOffsetY += yOffset;
        }



    private int myHideTick;

    private int myHideTicks;

    private int myVisibleTicks;

    private boolean myAutoHide;

    private boolean myMakeVisibleFlag;
    }
