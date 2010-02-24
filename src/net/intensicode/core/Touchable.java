//#condition TOUCH

package net.intensicode.core;

public abstract class Touchable
    {
    public static final int TRIGGER_ON_DOWN = 1;

    public static final int TRIGGER_ON_SWIPE = 2;

    public static final int TRIGGER_ON_UP = 4;

    public static final int RELEASE_IMMEDIATELY = 1;

    public static final int RELEASE_ON_UP = 2;

    public static final int RELEASE_ON_OUT = 4;

    public boolean cancelTriggerOnUpWhenMovedOut = true;

    public int triggerMode = TRIGGER_ON_UP;

    public int releaseMode = RELEASE_IMMEDIATELY;

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

    public boolean isActivatedBy( TouchEvent aTouchEvent )
        {
        return isInside( aTouchEvent );
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
        buffer.append( associatedObject );
        buffer.append( ",keyID=" );
        buffer.append( associatedKeyID );
        return buffer.toString();
        }

    //#endif

    // Protected Interface

    protected abstract boolean isInside( TouchEvent aTouchEvent );

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
        if ( isInside( aTouchEvent ) ) return true;
        return activated && !cancelTriggerOnUpWhenMovedOut;
        }
    }
