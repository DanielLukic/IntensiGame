package net.intensicode.path;

import net.intensicode.util.Position;

public interface PositionList
    {
    void clear();

    PositionList add( final Position aPositionFixed );

    void end();
    }
