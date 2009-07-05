/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

/**
 * TODO: Describe this!
 */
public abstract class AbstractScreen
    {
    protected AbstractScreen()
        {
        }

    // From FrameHandler

    public void onInit( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        final boolean alreadyInitialized = myEngine != null && myScreen != null;
        myEngine = aEngine;
        myScreen = aScreen;
        if ( alreadyInitialized == false ) onInitOnce( aEngine, aScreen );
        onInitEverytime( aEngine, aScreen );
        }

    public void onInitOnce( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        }

    public void onInitEverytime( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        }

    public void onPop( final Engine aEngine )
        {
        }

    public abstract void onControlTick( final Engine aEngine ) throws Exception;

    public abstract void onDrawFrame( final DirectScreen aScreen );

    // Protected Interface

    protected final boolean isInitialized()
        {
        return myEngine != null && myScreen != null;
        }

    protected final Engine engine()
        {
        if ( myEngine == null ) throw new IllegalStateException();
        return myEngine;
        }

    protected final DirectScreen screen()
        {
        if ( myScreen == null ) throw new IllegalStateException();
        return myScreen;
        }



    private Engine myEngine;

    private DirectScreen myScreen;
    }
