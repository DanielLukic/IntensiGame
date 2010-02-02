package net.intensicode.core;

import net.intensicode.util.Position;

public interface DirectScreen
    {
    /**
     * The width of the screen as defined by setTargetSize (or the native resolution if no target size has been set).
     */
    int width();

    /**
     * The height of the screen as defined by setTargetSize (or the native resolution if no target size has been set).
     */
    int height();

    /**
     * The width given to setTargetSize. Zero if the latter has not been called.
     */
    int getTargetWidth();

    /**
     * The height given to setTargetSize. Zero if the latter has not been called.
     */
    int getTargetHeight();

    /**
     * Set a target size for rendering. The DirectScreen will scale this to the native resolution if it should differ.
     */
    void setTargetSize( int aWidth, int aHeight );

    /**
     * This will translate a native x/y coordinate into the target coordinate space defined by setTargetSize. Used
     * primarly by IntensiGame itself to convert for example touch event coordinates.
     */
    Position toTarget( int aNativeX, int aNativeY );

    void beginFrame();

    void endFrame();
    }
