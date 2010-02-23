//#condition TOUCH

package net.intensicode.core;

public interface TouchEvent
    {
    boolean isPress();

    boolean isSwipe();

    boolean isRelease();

    int getX();

    int getY();
    }
