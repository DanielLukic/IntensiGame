package net.intensicode.util;

public interface PositionableEntry
    {
    Position getPositionByReference();

    void setAvailableWidth( int aWidthInPixels );

    //#if TOUCH

    void updateTouchable();

    //#endif
    }
