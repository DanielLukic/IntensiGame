package net.intensicode.path;

import net.intensicode.util.*;

public interface Path
    {
    int getPathLength();

    PositionF getPosition( float aPathPos );

    PositionF getDirection( float aPathPos );
    }
