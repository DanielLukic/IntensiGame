package net.intensicode.path;

import net.intensicode.util.*;

public interface Interpolation
    {
    int getNumberOfInputPositions();

    PositionF getInputPosition( int aIndex );

    PositionF getPosition( int aSegmentIndex, float aSegmentPos );

    PositionF getDirection( int aSegmentIndex, float aSegmentPos );
    }
