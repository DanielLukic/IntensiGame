//#condition TOUCH

package net.intensicode.touch.controls;

import net.intensicode.touch.TouchEvent;
import net.intensicode.util.Rectangle;

public final class StateIdle extends State
    {
    public final void processTouchEvent()
        {
        final TouchEvent event = shared.touchEvent;
        if ( event.isRelease() || !isInsideHotzone() ) return;
        if ( event.isPress() ) shared.startingState.transition();
        if ( event.isSwipe() && shared.configuration.swipeStartsAction ) shared.startingState.transition();
        }

    private boolean isInsideHotzone()
        {
        final TouchEvent event = shared.touchEvent;
        final Rectangle hotzone = shared.optionalHotzone;
        return hotzone == null || hotzone.contains( event.getX(), event.getY() );
        }
    }
