//#condition TOUCH

package net.intensicode.core;

public final class QueuedTouchEvent
    {
    public static final int NO_KEY_ID = 0;

    public static final int NO_ACTION = 0;

    public static final int TRIGGERED = 1;

    public static final int RELEASED = 2;

    public int action = NO_ACTION;

    public TouchableHandler handler;

    public Object object;

    public int keyID = NO_KEY_ID;


    public final void copyFrom( final Touchable aTouchable )
        {
        handler = aTouchable.associatedHandler;
        object = aTouchable.associatedObject;
        keyID = aTouchable.associatedKeyID;
        if ( object == null ) object = aTouchable;
        }
    }
