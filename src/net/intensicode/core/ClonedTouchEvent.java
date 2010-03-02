package net.intensicode.core;

final class ClonedTouchEvent extends TouchEvent
    {
    public boolean press;

    public boolean swipe;

    public boolean release;

    public int x;

    public int y;


    public ClonedTouchEvent( final TouchEvent aTouchEvent )
        {
        press = aTouchEvent.isPress();
        swipe = aTouchEvent.isSwipe();
        release = aTouchEvent.isRelease();
        x = aTouchEvent.getX();
        y = aTouchEvent.getY();
        }

    // From TouchEvent

    public final boolean isPress()
        {
        return press;
        }

    public final boolean isSwipe()
        {
        return swipe;
        }

    public final boolean isRelease()
        {
        return release;
        }

    public final int getX()
        {
        return x;
        }

    public final int getY()
        {
        return y;
        }
    }
