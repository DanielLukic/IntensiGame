//#condition TOUCH_SUPPORTED

package net.intensicode.core;

public abstract class TouchHandler
    {
    public boolean globalControlsActive = true;


    public final void setGlobalControlsBlending( final int aAlpha256 )
        {
        myGlobalControls.setBlending( aAlpha256 );
        }

    public final void addLocalControl( final Touchable aTouchable )
        {
        myLocalControls.add( aTouchable );
        }

    public final void removeLocalControl( final Touchable aTouchable )
        {
        myLocalControls.remove( aTouchable );
        }

    public final void addGlobalControl( final Touchable aTouchable )
        {
        myGlobalControls.add( aTouchable );
        }

    public final void removeGlobalControl( final Touchable aTouchable )
        {
        myGlobalControls.remove( aTouchable );
        }

    public final void purgePendingEvents()
        {
        myGlobalControls.purgePendingEvents();
        myLocalControls.purgePendingEvents();
        }

    // Internal API

    // TODO: Move Internal API into internal class hidden from framework user.

    public final synchronized void onControlTick() throws Exception
        {
        if ( globalControlsActive ) myGlobalControls.processQueuedTouchEvents();
        myLocalControls.processQueuedTouchEvents();
        }

    public final void onDrawFrame()
        {
        if ( globalControlsActive ) myGlobalControls.drawAllTouchables();
        myLocalControls.drawAllTouchables();
        }

    // Protected API

    protected TouchHandler( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        myLocalControls = new TouchControlsManager( aGameSystem );
        myGlobalControls = new TouchControlsManager( aGameSystem );
        }

    protected final void processTouchEvent( final TouchEvent aTouchEvent )
        {
        passEventToControlsManager( myLocalControls, aTouchEvent );
        if ( globalControlsActive ) passEventToControlsManager( myGlobalControls, aTouchEvent );
        }

    // Implementation

    private void passEventToControlsManager( final TouchControlsManager aTouchControlsManager, final TouchEvent aTouchEvent )
        {
        aTouchControlsManager.setCurrentTouchEvent( aTouchEvent );
        aTouchControlsManager.checkForTriggeredTouchables();
        aTouchControlsManager.checkForActivatedTouchables();
        aTouchControlsManager.checkForReleasedTouchables();
        if ( aTouchControlsManager.isReleaseEvent() ) aTouchControlsManager.resetAllTouchables();
        }

    protected final GameSystem myGameSystem;

    private final TouchControlsManager myLocalControls;

    private final TouchControlsManager myGlobalControls;
    }
