/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import javax.microedition.lcdui.Display;

/**
 * TODO: Describe this!
 */
public interface SystemContext
    {
    void exit();

    void pause();

    Display getDisplay();

    ResourceLoader getResourceLoader();

    AbstractScreen initMainController() throws Exception;
    }
