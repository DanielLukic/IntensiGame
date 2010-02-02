package net.intensicode.path;

import net.intensicode.util.Position;

public interface Interpolation
    {
    int getNumberOfInputPositions();

    Position getInputPosition( int aIndex );

    Position getPosition( int aSegmentIndex, int aSegmentPosFixed );

    Position getDirection( int aSegmentIndex, int aSegmentPosFixed );
    }
