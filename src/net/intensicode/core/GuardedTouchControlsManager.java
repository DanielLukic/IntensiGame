//#condition TOUCH_SUPPORTED

package net.intensicode.core;

public final class GuardedTouchControlsManager extends TouchControlsManager
    {
    public GuardedTouchControlsManager( final GameSystem aGameSystem )
        {
        super( aGameSystem );
        }

    // From TouchControlsManager

    public final synchronized void setBlending( final int aAlpha256 )
        {
        super.setBlending( aAlpha256 );
        }

    public final synchronized void removeAll()
        {
        super.removeAll();
        }

    public final synchronized void remove( final Touchable aTouchable )
        {
        super.remove( aTouchable );
        }

    public final synchronized void add( final Touchable aTouchable )
        {
        super.add( aTouchable );
        }

    public final synchronized void drawAllTouchables()
        {
        super.drawAllTouchables();
        }

    public final synchronized void checkForTriggeredTouchables()
        {
        super.checkForTriggeredTouchables();
        }

    public final synchronized void checkForActivatedTouchables()
        {
        super.checkForActivatedTouchables();
        }

    public final synchronized void checkForReleasedTouchables()
        {
        super.checkForReleasedTouchables();
        }

    public final synchronized void resetAllTouchables()
        {
        super.resetAllTouchables();
        }
    }
