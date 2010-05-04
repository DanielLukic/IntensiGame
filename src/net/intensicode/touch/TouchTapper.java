//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.PlatformContext;
import net.intensicode.util.*;

public final class TouchTapper implements TouchEventListener
    {
    public static final int NO_TAP = 0;


    public TouchTapper( final PlatformContext aPlatformContext )
        {
        myPlatformContext = aPlatformContext;
        }

    public final boolean hasTaps()
        {
        return myTaps != NO_TAP;
        }

    public final int getAndResetTaps()
        {
        final int taps = myTaps;
        myTaps = NO_TAP;
        return taps;
        }

    public final void setOptionalHotzoneByReference( final Rectangle aHotzone )
        {
        myOptionalHotzone = aHotzone;
        }

    public final void onControlTick()
        {
        if ( myTapCount == 0 ) return;
        if ( isTapTimeOut() || isMaxTaps() ) emitTaps();
        }

    private boolean isTapTimeOut()
        {
        final long now = myPlatformContext.compatibleTimeInMillis();
        return now - myTapCountTimestamp > TIME_OUT_IN_MILLIS;
        }

    private boolean isMaxTaps()
        {
        return myTapCount == MAX_TAPS;
        }

    private void emitTaps()
        {
        myTaps = myTapCount;
        myTapCount = 0;
        myTapCountTimestamp = 0;
        }

    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        updateTouchEvent( aTouchEvent );
        if ( !isInsideHotzone() ) return;
        if ( isTooMuchMovement() ) resetTapState();
        if ( isTap() ) addTap();
        if ( isPress() ) startTapState();
        if ( isRelease() ) resetTapState();
        }

    private void updateTouchEvent( final TouchEvent aTouchEvent )
        {
        myEventPosition.setTo( aTouchEvent.getX(), aTouchEvent.getY() );
        myEventAction = aTouchEvent.isPress() ? ACTION_PRESS : aTouchEvent.isRelease() ? ACTION_RELEASE : ACTION_NONE;
        myEventTimestamp = aTouchEvent.timestamp();
        }

    private boolean isInsideHotzone()
        {
        if ( myOptionalHotzone == null ) return true;
        return myOptionalHotzone.contains( myEventPosition );
        }

    private boolean isTooMuchMovement()
        {
        if ( !myPressActionSeen ) return false;
        final int deltaX = Math.abs( myStartPosition.x - myEventPosition.x );
        final int deltaY = Math.abs( myStartPosition.y - myEventPosition.y );
        return deltaX > THRESHOLD_IN_PIXELS || deltaY > THRESHOLD_IN_PIXELS;
        }

    private boolean isTap()
        {
        final long now = myPlatformContext.compatibleTimeInMillis();
        if ( now - myTapStateTimestamp > TIME_OUT_IN_MILLIS ) return false;
        return myPressActionSeen && myEventAction == ACTION_RELEASE;
        }

    private void addTap()
        {
        myTapCount++;
        myTapCountTimestamp = myEventTimestamp;
        }

    private boolean isPress()
        {
        return myEventAction == ACTION_PRESS;
        }

    private void startTapState()
        {
        myPressActionSeen = true;
        myStartPosition.setTo( myEventPosition );
        myTapStateTimestamp = myEventTimestamp;
        }

    private boolean isRelease()
        {
        return myEventAction == ACTION_RELEASE;
        }

    private void resetTapState()
        {
        myPressActionSeen = false;
        }


    private int myTaps = NO_TAP;

    private int myTapCount;

    private int myEventAction;

    private long myEventTimestamp;

    private long myTapStateTimestamp;

    private long myTapCountTimestamp;

    private boolean myPressActionSeen;

    private Rectangle myOptionalHotzone;

    private final PlatformContext myPlatformContext;

    private final Position myStartPosition = new Position();

    private final Position myEventPosition = new Position();

    private static final int ACTION_NONE = 0;

    private static final int ACTION_PRESS = 1;

    private static final int ACTION_RELEASE = 2;

    // TOOO: Move these to a configuration object:

    private static final int MAX_TAPS = 3;

    private static final int TIME_OUT_IN_MILLIS = 200;

    private static final int THRESHOLD_IN_PIXELS = 10;
    }
