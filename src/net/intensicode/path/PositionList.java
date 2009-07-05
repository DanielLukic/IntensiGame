/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.path;

import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public interface PositionList
    {
    void clear();

    PositionList add( final Position aPositionFixed );

    void end();
    }
