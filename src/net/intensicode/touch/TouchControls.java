//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.core.DirectGraphics;
import net.intensicode.util.*;
import net.intensicode.touch.controls.*;

public final class TouchControls implements TouchEventListener
    {
    public TouchControls( final TouchControlsConfiguration aConfiguration )
        {
        mySharedState.configuration = aConfiguration;

        mySharedState.idleState = new StateIdle().share( mySharedState );
        mySharedState.startingState = new StateStarting().share( mySharedState );
        mySharedState.draggingState = new StateDragging().share( mySharedState );
        mySharedState.gestureState = new StateGesture().share( mySharedState );

        mySharedState.idleState.transition();
        }

    public final boolean hasGesture()
        {
        return mySharedState.recognizedGesture != null;
        }

    public final String getAndResetGesture()
        {
        final String gesture = mySharedState.recognizedGesture;
        mySharedState.recognizedGesture = null;
        return gesture;
        }

    public final boolean hasDragSteps()
        {
        return mySharedState.dragSteps.validDirection();
        }

    public final Position getAndResetDragSteps()
        {
        myDragSteps.setTo( mySharedState.dragSteps );
        mySharedState.dragSteps.setTo( 0, 0 );
        return myDragSteps;
        }

    public final void setOptionalHotzoneByReference( final Rectangle aHotzone )
        {
        mySharedState.optionalHotzone = aHotzone;
        }

    //#if DEBUG_TOUCH

    public final void onDrawFrame( final DirectGraphics aGraphics )
        {
        mySharedState.onDrawFrame( aGraphics );
        }

    //#endif

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        mySharedState.updateTouchEvent( aTouchEvent );
        mySharedState.currentState.processTouchEvent();
        }


    private final Position myDragSteps = new Position();

    private final SharedState mySharedState = new SharedState();
    }
