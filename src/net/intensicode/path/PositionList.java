package net.intensicode.path;

import net.intensicode.util.*;

public interface PositionList
    {
    void clear();

    PositionList add( final PositionF aPositionFixed );

    void end();
    }
