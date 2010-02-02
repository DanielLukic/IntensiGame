package net.intensicode.path;

import net.intensicode.util.Position;

public interface PathWithDirection extends Path
    {
    Position getStartPosition();

    Position getStartDirection();

    Position getEndPosition();

    Position getEndDirection();

    PathWithDirection createConnectedPathTo( Position aTargetPosition, int aStepSizeFixed );
    }
