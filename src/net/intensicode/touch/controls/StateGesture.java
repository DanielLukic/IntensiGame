//#condition TOUCH

package net.intensicode.touch.controls;

import net.intensicode.touch.TouchGestures;

public final class StateGesture extends State
    {
    public void enter()
        {
        shared.recognizedGesture = recognize( shared.deltaFromStart.x, shared.deltaFromStart.y );
        shared.idleState.transition();
        }

    private String recognize( float aDeltaX, float aDeltaY )
        {
        final float scaledX = Math.abs( aDeltaX ) * shared.configuration.gestureDirectionIgnoreFactor;
        final float scaledY = Math.abs( aDeltaY ) * shared.configuration.gestureDirectionIgnoreFactor;
        if ( Math.abs( aDeltaX ) > scaledY ) aDeltaY = 0;
        if ( Math.abs( aDeltaY ) > scaledX ) aDeltaX = 0;
        final int indexX = determineStrokeIndex( aDeltaX );
        final int indexY = determineStrokeIndex( aDeltaY );
        final int strokeIndex = indexY * 3 + indexX;
        return TouchGestures.STROKES[ strokeIndex ];
        }

    private int determineStrokeIndex( final float aDelta )
        {
        if ( aDelta < 0 ) return 0;
        if ( aDelta > 0 ) return 2;
        return 1;
        }

    public final void processTouchEvent()
        {
        throw new IllegalStateException();
        }
    }
