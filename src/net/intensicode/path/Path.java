package net.intensicode.path;

import net.intensicode.util.Position;

public interface Path
    {
    int getPathLength();

    Position getPosition( int aPathPosFixed );

    Position getDirection( int aPathPosFixed );
    }
