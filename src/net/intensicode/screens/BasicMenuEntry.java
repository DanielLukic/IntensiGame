package net.intensicode.screens;

import net.intensicode.touch.Touchable;
import net.intensicode.util.PositionableEntry;

public interface BasicMenuEntry extends PositionableEntry
    {
    boolean selectable();

    void setSelected( boolean aSelectedFlag );

    int id();

    ScreenBase visual();

    //#if TOUCH

    Touchable touchable();

    //#endif
    }
