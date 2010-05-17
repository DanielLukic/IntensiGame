//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.core.*;
import net.intensicode.util.*;

public class TouchControlsManager
    {
    public TouchControlsManager( final GameSystem aGameSystem ) throws Exception
        {
        myGameSystem = aGameSystem;
        myPooledEvents = new ObjectPool( "net.intensicode.touch.QueuedTouchEvent" );
        }

    public void setBlending( final int aAlpha256 )
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            touchable.alpha256 = aAlpha256;
            }
        }

    public void removeAll()
        {
        myTouchables.clear();
        }

    public void remove( final Touchable aTouchable )
        {
        Assert.isTrue( "really present", myTouchables.contains( aTouchable ) );
        myTouchables.removeAll( aTouchable );
        }

    public void add( final Touchable aTouchable )
        {
        Assert.isFalse( "added once only", myTouchables.contains( aTouchable ) );
        myTouchables.add( aTouchable );
        }

    public void drawAllTouchables()
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            touchable.onDraw( myGameSystem.graphics );
            }
        }

    public final void purgePendingEvents()
        {
        //#if DEBUG
        if ( myQueuedTouchEvents.size > 0 ) Log.debug( "purging {} pending touch events", myQueuedTouchEvents.size );
        //#endif
        while ( myQueuedTouchEvents.size > 0 )
            {
            myPooledEvents.addReleasedInstance( myQueuedTouchEvents.removeLast() );
            }
        }

    public final void processQueuedTouchEvents()
        {
        for ( int idx = 0; idx < myQueuedTouchEvents.size; idx++ )
            {
            final QueuedTouchEvent event = (QueuedTouchEvent) myQueuedTouchEvents.get( idx );
            trigger( event );
            myPooledEvents.addReleasedInstance( event );
            }
        myQueuedTouchEvents.clear();
        }

    public final void setCurrentTouchEvent( final TouchEvent aTouchEvent )
        {
        myCurrentTouchEvent = aTouchEvent;
        }

    public final boolean isReleaseEvent()
        {
        return myCurrentTouchEvent.isRelease();
        }

    public void checkForTriggeredTouchables()
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            if ( touchable.isTriggeredBy( myCurrentTouchEvent ) )
                {
                touchable.triggered = true;
                enqueueTouchedTarget( touchable );
                }
            }
        }

    public void checkForActivatedTouchables()
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            if ( touchable.isActivatedBy( myCurrentTouchEvent ) )
                {
                touchable.activated = true;
                }
            }
        }

    public void checkForDeactivatedTouchables()
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            if ( touchable.isDeactivatedBy( myCurrentTouchEvent ) )
                {
                touchable.activated = false;
                }
            }
        }

    public void checkForReleasedTouchables()
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            if ( touchable.isReleasedBy( myCurrentTouchEvent ) )
                {
                enqueueReleasedTarget( touchable );
                touchable.resetState();
                }
            }
        }

    public void resetAllTouchables()
        {
        for ( int idx = 0; idx < myTouchables.size; idx++ )
            {
            final Touchable touchable = (Touchable) myTouchables.get( idx );
            touchable.resetState();
            }
        }

    // Implementation

    private void trigger( final QueuedTouchEvent aEvent )
        {
        if ( aEvent.handler != null ) triggerHandlerEvent( aEvent );
        if ( aEvent.keyID != QueuedTouchEvent.NO_KEY_ID ) triggerKeyEvent( aEvent );
        if ( aEvent.keyCode != QueuedTouchEvent.NO_KEY_CODE ) triggerKeyCodeEvent( aEvent );
        }

    private void triggerHandlerEvent( final QueuedTouchEvent aEvent )
        {
        if ( aEvent.action == QueuedTouchEvent.TRIGGERED )
            {
            aEvent.handler.onPressed( aEvent.object );
            }
        else if ( aEvent.action == QueuedTouchEvent.RELEASED )
            {
            aEvent.handler.onReleased( aEvent.object );
            }
        }

    private void triggerKeyEvent( final QueuedTouchEvent aEvent )
        {
        if ( aEvent.action == QueuedTouchEvent.TRIGGERED )
            {
            final KeysHandler keys = myGameSystem.keys;
            keys.queueSetEvent( aEvent.keyID );
            }
        else if ( aEvent.action == QueuedTouchEvent.RELEASED )
            {
            final KeysHandler keys = myGameSystem.keys;
            keys.queueClearEvent( aEvent.keyID );
            }
        }

    private void triggerKeyCodeEvent( final QueuedTouchEvent aEvent )
        {
        final KeysHandler keys = myGameSystem.keys;
        keys.lastAction = 0;

        if ( aEvent.action == QueuedTouchEvent.TRIGGERED )
            {
            keys.lastCode = aEvent.keyCode;
            }
        else if ( aEvent.action == QueuedTouchEvent.RELEASED )
            {
            keys.lastCode = 0;
            }
        }

    private void enqueueTouchedTarget( final Touchable aTouchable )
        {
        final QueuedTouchEvent event = (QueuedTouchEvent) myPooledEvents.getOrCreateInstance();
        event.action = QueuedTouchEvent.TRIGGERED;
        event.copyFrom( aTouchable );
        myQueuedTouchEvents.add( event );
        }

    private void enqueueReleasedTarget( final Touchable aTouchable )
        {
        final QueuedTouchEvent event = (QueuedTouchEvent) myPooledEvents.getOrCreateInstance();
        event.action = QueuedTouchEvent.RELEASED;
        event.copyFrom( aTouchable );
        myQueuedTouchEvents.add( event );
        }


    private TouchEvent myCurrentTouchEvent;

    private final GameSystem myGameSystem;

    private final DynamicArray myTouchables = new DynamicArray();

    private final DynamicArray myQueuedTouchEvents = new DynamicArray();

    private final ObjectPool myPooledEvents;
    }
