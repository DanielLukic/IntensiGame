package net.intensicode.path;

import net.intensicode.util.*;

public interface Path
    {
    float getPathLength();

    PositionF getPosition( float aPathPos );

    PositionF getDirection( float aPathPos );
    }
