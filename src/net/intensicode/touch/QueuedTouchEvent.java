//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.touch.*;

public final class QueuedTouchEvent
    {
    public static final int NO_KEY_ID = 0;

    public static final int NO_KEY_CODE = 0;

    public static final int NO_ACTION = 0;

    public static final int TRIGGERED = 1;

    public static final int RELEASED = 2;

    public int action = NO_ACTION;

    public TouchableHandler handler;

    public Object object;

    public int keyID = NO_KEY_ID;

    public int keyCode = NO_KEY_CODE;


    public final void copyFrom( final Touchable aTouchable )
        {
        handler = aTouchable.associatedHandler;
        object = aTouchable.associatedObject;
        keyID = aTouchable.associatedKeyID;
        keyCode = aTouchable.associatedKeyCode;
        if ( object == null ) object = aTouchable;
        }
    }
