package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.screens.SoftkeysScreen;

public final class AutohideSoftkeysScreen extends SoftkeysScreen
    {
    public AutohideSoftkeysScreen( final FontGenerator aFontGen )
        {
        super( aFontGen );
        }

    public final void setSoftkeys( final String aLeftButton, final String aRightButton, final boolean aAutoHide )
        {
        setSoftkeys( aLeftButton, aRightButton );
        resetAnimation();
        myAutoHide = true;
        }

    // From ScreenBase

    public final void onControlTick()
        {
        super.onControlTick();
        if ( myVisibleTicks > 0 ) myVisibleTicks--;
        else if ( myHideTick > 0 && myAutoHide ) myHideTick--;
        }

    // Protected API

    protected void setPosition( final int aHorizontalPosition, final String aText )
        {
        final int xOffset = getAnimatedOffset( aText );
        final int offset = aHorizontalPosition == 0 ? -xOffset : +xOffset;
        super.setPosition( aHorizontalPosition + offset, aText );
        }

    // Implementation

    private void resetAnimation()
        {
        final int tps = timing().ticksPerSecond;
        myVisibleTicks = tps * 3 / 2;
        myHideTick = myHideTicks = tps * 2 / 3;
        }

    private int getAnimatedOffset( final String aText )
        {
        if ( !myAutoHide ) return 0;

        final int maxOffset = getAlignWidth( aText );
        if ( isCompletetlyHidden() ) return 0;

        return maxOffset / 2 - myHideTick * maxOffset / myHideTicks / 2;
        }

    private boolean isCompletetlyHidden()
        {
        return myHideTicks == 0;
        }


    private int myHideTick;

    private int myHideTicks;

    private int myVisibleTicks;

    private boolean myAutoHide;
    }
