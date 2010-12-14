package net.intensicode.path;

import net.intensicode.util.*;

public interface PathWithDirection extends Path
    {
    PositionF getStartPosition();

    PositionF getStartDirection();

    PositionF getEndPosition();

    PositionF getEndDirection();

    PathWithDirection createConnectedPathTo( PositionF aTargetPosition, float aStepSize );
    }
