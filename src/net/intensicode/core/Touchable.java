//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public abstract class Touchable
    {
    public static final int TRIGGER_ON_DOWN = 1;

    public static final int TRIGGER_ON_SWIPE = 2;

    public static final int TRIGGER_ON_UP = 4;

    public static final int RELEASE_IMMEDIATELY = 1;

    public static final int RELEASE_ON_UP = 2;

    public static final int RELEASE_ON_OUT = 4;

    public static final int ACTIVATE_ON_TOUCH = 1;

    public static final int ACTIVATE_ONLY_ON_DOWN = 2;

    public static final int DEACTIVATE_ON_RELEASE = 1;

    public static final int DEACTIVATE_ON_OUT = 2;

    public boolean cancelTriggerOnUpWhenMovedOut = true;

    public int triggerMode = TRIGGER_ON_UP;

    public int releaseMode = RELEASE_IMMEDIATELY;

    public int activateMode = ACTIVATE_ON_TOUCH;

    public int deactivateMode = DEACTIVATE_ON_RELEASE | DEACTIVATE_ON_OUT;

    public int alpha256 = DirectGraphics.FULLY_OPAQUE;

    public TouchableHandler associatedHandler;

    public Object associatedObject;

    public int associatedKeyID;

    public boolean triggered;

    public boolean activated;


    public final void resetState()
        {
        triggered = activated = false;
        }

    public boolean isTriggeredBy( final TouchEvent aTouchEvent )
        {
        if ( triggeredOnDown( aTouchEvent ) ) return true;
        if ( triggeredOnSwipe( aTouchEvent ) ) return true;
        if ( triggeredOnUp( aTouchEvent ) ) return true;
        return false;
        }

    public boolean isActivatedBy( final TouchEvent aTouchEvent )
        {
        if ( !isInside( aTouchEvent ) ) return false;
        if ( activateOnTouch() ) return true;
        if ( activateOnlyOnDown() ) return aTouchEvent.isPress();
        return false;
        }

    private boolean activateOnTouch()
        {
        return ( activateMode & ACTIVATE_ON_TOUCH ) != 0;
        }

    private boolean activateOnlyOnDown()
        {
        return ( activateMode & ACTIVATE_ONLY_ON_DOWN ) != 0;
        }

    public boolean isDeactivatedBy( final TouchEvent aTouchEvent )
        {
        if ( deactivateOnRelease() && aTouchEvent.isRelease() ) return true;
        if ( deactivateOnOut() && !isInside( aTouchEvent ) ) return true;
        return false;
        }

    private boolean deactivateOnRelease()
        {
        return ( deactivateMode & DEACTIVATE_ON_RELEASE ) != 0;
        }

    private boolean deactivateOnOut()
        {
        return ( deactivateMode & DEACTIVATE_ON_OUT ) != 0;
        }

    public boolean isReleasedBy( final TouchEvent aTouchEvent )
        {
        if ( triggered && releaseImmediately() ) return true;
        if ( triggered && releaseOnOut() && !isInside( aTouchEvent ) ) return true;
        if ( triggered && releaseOnUp() && aTouchEvent.isRelease() ) return true;
        return false;
        }

    private boolean releaseImmediately()
        {
        return ( releaseMode & RELEASE_IMMEDIATELY ) != 0;
        }

    private boolean releaseOnOut()
        {
        return ( releaseMode & RELEASE_ON_OUT ) != 0;
        }

    private boolean releaseOnUp()
        {
        return ( releaseMode & RELEASE_ON_UP ) != 0;
        }

    public abstract void onDraw( DirectGraphics aGraphics );

    // From Object

    //#if DEBUG

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "object=" );
        if ( associatedObject == this ) buffer.append( "this" );
        else buffer.append( associatedObject );
        buffer.append( ",keyID=" );
        buffer.append( associatedKeyID );
        return buffer.toString();
        }

    //#endif

    // Protected Interface

    protected abstract boolean isInside( TouchEvent aTouchEvent );

    protected void onDrawActivated( final DirectGraphics aGraphics, final Rectangle aRectangle )
        {
        aGraphics.setColorARGB32( 0x60FFFFFF );
        aGraphics.fillRect( aRectangle.x, aRectangle.y, aRectangle.width, aRectangle.height );
        }

    protected void onDrawDebug( final DirectGraphics aGraphics, final Rectangle aRectangle )
        {
        //#if DEBUG && DEBUG_TOUCH
        //# aGraphics.setColorARGB32( 0x3000FF00 );
        //# aGraphics.fillRect( aRectangle.x, aRectangle.y, 4, aRectangle.height );
        //# aGraphics.fillRect( aRectangle.x, aRectangle.y, aRectangle.width, 4 );
        //# aGraphics.fillRect( aRectangle.x + aRectangle.width - 4, aRectangle.y, 4, aRectangle.height );
        //# aGraphics.fillRect( aRectangle.x, aRectangle.y + aRectangle.height - 4, aRectangle.width, 4 );
        //# aGraphics.setColorARGB32( 0xFF000000 );
        //#endif
        }

    // Implementation

    private boolean triggeredOnDown( final TouchEvent aTouchEvent )
        {
        if ( ( triggerMode & TRIGGER_ON_DOWN ) == 0 ) return false;
        if ( !aTouchEvent.isPress() ) return false;
        return isInside( aTouchEvent );
        }

    private boolean triggeredOnSwipe( final TouchEvent aTouchEvent )
        {
        if ( ( triggerMode & TRIGGER_ON_SWIPE ) == 0 ) return false;
        if ( !aTouchEvent.isSwipe() ) return false;
        return isInside( aTouchEvent );
        }

    private boolean triggeredOnUp( final TouchEvent aTouchEvent )
        {
        if ( ( triggerMode & TRIGGER_ON_UP ) == 0 ) return false;
        if ( !aTouchEvent.isRelease() ) return false;
        if ( isInside( aTouchEvent ) ) return activated;
        return activated && !cancelTriggerOnUpWhenMovedOut;
        }
    }
