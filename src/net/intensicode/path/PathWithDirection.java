package net.intensicode.path;

import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public interface PathWithDirection extends Path
    {
    Position getStartPosition();

    Position getStartDirection();

    Position getEndPosition();

    Position getEndDirection();

    PathWithDirection createConnectedPathTo( Position aTargetPosition, int aStepSizeFixed );
    }
