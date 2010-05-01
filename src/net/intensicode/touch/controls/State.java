//#condition TOUCH

package net.intensicode.touch.controls;

import net.intensicode.core.DirectGraphics;

public abstract class State
    {
    public SharedState shared;

    //#if DEBUG_TOUCH

    public void onDrawFrame( final DirectGraphics aGraphics )
        {
        }

    //#endif

    public final State share( final SharedState aSharedState )
        {
        shared = aSharedState;
        return this;
        }

    public final void transition()
        {
        if ( shared.currentState != null ) shared.currentState.leave();
        shared.currentState = this;
        shared.currentState.enter();
        }

    public void enter()
        {
        }

    public void leave()
        {
        }

    public abstract void processTouchEvent();


    protected final boolean gestureConditionMet()
        {
        final long deltaInMillis = shared.touchEvent.timestamp() - shared.startTimestamp;
        if ( deltaInMillis < shared.configuration.gestureMinThresholdInMillis ) return false;
        if ( deltaInMillis > shared.configuration.gestureMaxThresholdInMillis ) return false;
        return shared.touchEvent.isRelease();
        }
    }
