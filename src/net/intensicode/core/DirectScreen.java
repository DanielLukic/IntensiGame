package net.intensicode.core;

import net.intensicode.util.Position;

public interface DirectScreen
    {
    int VIEWPORT_MODE_SYSTEM = 0;
    int VIEWPORT_MODE_FULLSCREEN = 1;

    /**
     * The width of the screen as defined by setTargetSize or the native resolution if no target size has been set.
     */
    int width();

    /**
     * The height of the screen as defined by setTargetSize or the native resolution if no target size has been set.
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

    // Internal API - TODO: Move the next methods to an internal API..

    int getNativeWidth();

    int getNativeHeight();

    void initialize() throws Exception;

    void beginFrame() throws InterruptedException;

    void endFrame();

    void cleanup();

    /**
     * This will translate a native x/y coordinate into the target coordinate space defined by setTargetSize. Used
     * primarly by IntensiGame itself to convert for example touch event coordinates.
     */
    Position toTarget( int aNativeX, int aNativeY );

    /**
     * This will translate a target x/y coordinate into the native coordinate space defined by the device. Used
     * primarly to position banner ad views properly ontop of the game screen.
     */
    Position toNative( int aTargetX, int aTargetY );
    }
