//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.*;
import net.intensicode.screens.ScreenBase;
import net.intensicode.core.*;

public abstract class TouchHandler extends ScreenBase
    {
    public boolean globalControlsActive = true;


    public synchronized final void removeAllControls()
        {
        myGlobalControls.removeAll();
        myLocalControls.removeAll();
        }

    public synchronized final void setGlobalControlsBlending( final int aAlpha256 )
        {
        myGlobalControls.setBlending( aAlpha256 );
        }

    public synchronized final void addLocalControl( final Touchable aTouchable )
        {
        myLocalControls.add( aTouchable );
        }

    public synchronized final void removeLocalControl( final Touchable aTouchable )
        {
        myLocalControls.remove( aTouchable );
        }

    public synchronized final void addGlobalControl( final Touchable aTouchable )
        {
        myGlobalControls.add( aTouchable );
        }

    public synchronized final void removeGlobalControl( final Touchable aTouchable )
        {
        myGlobalControls.remove( aTouchable );
        }

    public synchronized final void purgePendingEvents()
        {
        myGlobalControls.purgePendingEvents();
        myLocalControls.purgePendingEvents();
        }

    public synchronized final void addListener( final TouchEventListener aListener )
        {
        myListeners.add( aListener );
        }

    // Internal API

    // TODO: Move Internal API into internal class hidden from framework user.

    public synchronized final void onControlTick() throws Exception
        {
        while ( myQueuedEvents.size > 0 )
            {
            final TouchEvent queuedEvent = (TouchEvent) myQueuedEvents.remove( 0 );
            processQueuedEvent( queuedEvent );
            }

        myLocalControls.removeAll();
        }

    public synchronized final void onDrawFrame()
        {
        if ( globalControlsActive ) myGlobalControls.drawAllTouchables();
        myLocalControls.drawAllTouchables();
        }

    // Protected API

    protected TouchHandler( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        myLocalControls = new GuardedTouchControlsManager( aGameSystem );
        myGlobalControls = new TouchControlsManager( aGameSystem );
        }

    protected synchronized final void processTouchEvent( final TouchEvent aTouchEvent )
        {
        if ( isSameEventAgain( aTouchEvent ) ) return;

        // TODO: Use pool for these objects..
        if ( myQueuedEvents.size == MAX_QUEUED_EVENTS ) myQueuedEvents.remove( 0 );
        myQueuedEvents.add( new ClonedTouchEvent( aTouchEvent ) );
        }

    // Implementation

    private long myPreviousTimestamp;

    private void processQueuedEvent( final TouchEvent aQueuedEvent )
        {
        //#if DEBUG_TOUCH
        final long delta = aQueuedEvent.timestamp() - myPreviousTimestamp;
        Log.info( "TOUCHEVENT {} delta=" + delta, aQueuedEvent );
        myPreviousTimestamp = aQueuedEvent.timestamp();
        //#endif

        broadcastEvent( aQueuedEvent );

        if ( globalControlsActive ) updateControls( aQueuedEvent, myGlobalControls );
        updateControls( aQueuedEvent, myLocalControls );
        }

    private void broadcastEvent( final TouchEvent aQueuedEvent )
        {
        for ( int idx = 0; idx < myListeners.size; idx++ )
            {
            final TouchEventListener listener = (TouchEventListener) myListeners.get( idx );
            listener.onTouchEvent( aQueuedEvent );
            }
        }

    private void updateControls( final TouchEvent aQueuedEvent, final TouchControlsManager aControls )
        {
        passEventToControlsManager( aControls, aQueuedEvent );
        aControls.processQueuedTouchEvents();
        }

    private void passEventToControlsManager( final TouchControlsManager aTouchControlsManager, final TouchEvent aTouchEvent )
        {
        aTouchControlsManager.setCurrentTouchEvent( aTouchEvent );
        aTouchControlsManager.checkForTriggeredTouchables();
        aTouchControlsManager.checkForActivatedTouchables();
        aTouchControlsManager.checkForDeactivatedTouchables();
        aTouchControlsManager.checkForReleasedTouchables();
        if ( aTouchControlsManager.isReleaseEvent() ) aTouchControlsManager.resetAllTouchables();
        }

    private boolean isSameEventAgain( final TouchEvent aTouchEvent )
        {
        if ( myQueuedEvents.size == 0 ) return false;

        final Object lastEvent = myQueuedEvents.last();
        if ( !lastEvent.equals( aTouchEvent ) ) return false;

        return true;
        }


    protected final GameSystem myGameSystem;

    private final TouchControlsManager myGlobalControls;

    private final GuardedTouchControlsManager myLocalControls;

    private final DynamicArray myListeners = new DynamicArray();

    private final DynamicArray myQueuedEvents = new DynamicArray( MAX_QUEUED_EVENTS, 0 );

    private static final int MAX_QUEUED_EVENTS = 32;
    }
