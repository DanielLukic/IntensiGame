/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.path;

import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public interface Path
{
    int getPathLength();

    Position getPosition( int aPathPosFixed );

    Position getDirection( int aPathPosFixed );
}
