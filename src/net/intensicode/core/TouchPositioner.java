//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.*;

public final class TouchPositioner implements TouchEventListener
    {
    public final Size touchCellSize = new Size( 1, 1 );

    public final Rectangle touchableArea = new Rectangle();

    public final Position lastCellTouched = new Position();

    public int lastCellAffinity;

    public long lastChangeTimestamp;

    public boolean hasValidPosition;

    public Rectangle optionalHotzone;


    // From TouchEventListener

    public final void onTouchEvent( final TouchEvent aTouchEvent )
        {
        final Rectangle hotzone = optionalHotzone != null ? optionalHotzone : touchableArea;

        final boolean touchInside = hotzone.contains( aTouchEvent.getX(), aTouchEvent.getY() );
        if ( !touchInside ) return;

        saveCurrentCellInfo();
        updateTouchedCell( aTouchEvent );
        updateTimestampsAndDelta( aTouchEvent );
        }

    // Implementation

    private void saveCurrentCellInfo()
        {
        mySavedCellInfo.setTo( lastCellTouched );
        }

    private void updateTouchedCell( final TouchEvent aTouchEvent )
        {
        final int touchX = aTouchEvent.getX();
        final int touchY = aTouchEvent.getY();
        final int xPos = touchX - touchableArea.x;
        final int yPos = touchY - touchableArea.y;
        final int xIndex = xPos / touchCellSize.width;
        final int yIndex = yPos / touchCellSize.height;
        final int columns = ( touchableArea.width + touchCellSize.width - 1 ) / touchCellSize.width;
        final int rows = ( touchableArea.height + touchCellSize.height - 1 ) / touchCellSize.height;
        lastCellTouched.x = Math.min( Math.max( 0, xIndex ), columns );
        lastCellTouched.y = Math.min( Math.max( 0, yIndex ), rows );
        final int cellCenterX = lastCellTouched.x * touchCellSize.width + touchCellSize.width / 2;
        final int cellCenterY = lastCellTouched.y * touchCellSize.height + touchCellSize.height / 2;
        final int cellAffinityX = Math.abs( cellCenterX - xPos );
        final int cellAffinityY = Math.abs( cellCenterY - yPos );
        final int cellAffinity = IntegerSquareRoot.sqrt( cellAffinityX + cellAffinityY );
        lastCellAffinity = cellAffinity;
        hasValidPosition = true;
        }

    private void updateTimestampsAndDelta( final TouchEvent aTouchEvent )
        {
        if ( mySavedCellInfo.equals( lastCellTouched ) ) return;
        lastChangeTimestamp = aTouchEvent.timestamp();
        }


    private final Position mySavedCellInfo = new Position();
    }
