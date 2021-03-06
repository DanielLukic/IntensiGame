package core;

import junit.framework.TestCase;
import net.intensicode.util.*;
import net.intensicode.touch.*;

import java.io.IOException;

public final class TestTouchSlider extends TestCase
    {
    public static final int NO_HORIZONTAL_MOVEMENT = 0;

    public static final int ONE_STEP_LEFT = -1;

    public static final int ONE_STEP_RIGHT = +1;


    public final void testTapIsNoMovement() throws IOException
        {
        runGestureData( "gesture_data_tap.txt", NO_HORIZONTAL_MOVEMENT );
        }

    public final void testLeftIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_left.txt", ONE_STEP_LEFT );
        }

    public final void testRightIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_right.txt", ONE_STEP_RIGHT );
        }

    private void runGestureData( final String aResourcePath, final int aExpectedHorizontalSteps ) throws IOException
        {
        loadGestureData( aResourcePath );
        for ( int idx = 0; idx < numberOfDataEntries(); idx++ )
            {
            setGestureDataEntry( idx );
            createTouchSliderObject();
            sendGestureDataToObject();
            assertEquals( aResourcePath + " - index " + idx, aExpectedHorizontalSteps, myTouchSlider.slideSteps.x );
            }
        }

    private void loadGestureData( final String aResourcePath ) throws IOException
        {
        final String data = loadResourceIntoString( aResourcePath );
        final DynamicArray lines = StringUtils.splitString( data, true );

        resetGestureData();

        for ( int idx = 0; idx < lines.size; idx++ )
            {
            final String line = (String) lines.get( idx );
            addToGestureData( line );
            if ( isStartOfDataEntry( line ) ) addDataEntryStartIndex( idx );
            }
        }

    private void resetGestureData()
        {
        myGestureDataLines.clear();
        myDataEntryStartIndexes.clear();
        }

    private String loadResourceIntoString( final String aResourcePath ) throws IOException
        {
        return new FakeResourcesManager().loadString( aResourcePath );
        }

    private void addToGestureData( final String aLine )
        {
        myGestureDataLines.add( aLine );
        }

    private boolean isStartOfDataEntry( final String aLine )
        {
        return aLine.indexOf( "MOTIONEVENT DOWN" ) != -1;
        }

    private void addDataEntryStartIndex( final int aIdx )
        {
        myDataEntryStartIndexes.add( new Integer( aIdx ) );
        }

    private int numberOfDataEntries()
        {
        return myDataEntryStartIndexes.size;
        }

    private void setGestureDataEntry( final int aDataEntryIndex )
        {
        final Integer startIndexObject = (Integer) myDataEntryStartIndexes.get( aDataEntryIndex );
        final int indexAfterStartOfDataEntry = startIndexObject.intValue() + 1;

        resetDataEntry();

        for ( int idx = indexAfterStartOfDataEntry; idx < myGestureDataLines.size; idx++ )
            {
            final String line = (String) myGestureDataLines.get( idx );
            if ( isStartOfDataEntry( line ) ) break;
            if ( isTouchEventLine( line ) ) addToDataEntry( line );
            }
        }

    private void resetDataEntry()
        {
        myDataEntryLines.clear();
        }

    private boolean isTouchEventLine( final String aLine )
        {
        return aLine.indexOf( "TOUCHEVENT" ) != -1;
        }

    private void addToDataEntry( final String aLine )
        {
        myDataEntryLines.add( aLine );
        }

    private void createTouchSliderObject()
        {
        final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
        myTouchSlider = new TouchSlider( configuration );
        myTouchSlider.touchableArea.x = myTouchSlider.touchableArea.y = 0;
        myTouchSlider.touchableArea.width = 480;
        myTouchSlider.touchableArea.height = 800;
        myTouchSlider.stepSizeInPixels.width = myTouchSlider.stepSizeInPixels.height = 18;
        }

    private void sendGestureDataToObject()
        {
        for ( int idx = 0; idx < myDataEntryLines.size; idx++ )
            {
            final String touchEventLine = (String) myDataEntryLines.get( idx );
            final String eventDataString = extractEventData( touchEventLine );
            final TouchEventData eventData = new TouchEventData( eventDataString );
            myTouchSlider.onTouchEvent( eventData );
            }
        }

    private String extractEventData( final String aTouchEventLine )
        {
        final int offset = aTouchEventLine.indexOf( "TOUCHEVENT" );
        final int startOfData = aTouchEventLine.indexOf( '(', offset );
        final int endOfData = aTouchEventLine.indexOf( ')', offset );
        return aTouchEventLine.substring( startOfData + 1, endOfData );
        }

    private static final class TouchEventData extends TouchEvent
        {
        public TouchEventData( final String aTouchEventDataString )
            {
            final DynamicArray parts = StringUtils.splitString( aTouchEventDataString, ",", false );
            myType = (String) parts.get( 0 );
            myPosX = Integer.parseInt( (String) parts.get( 1 ) );
            myPosY = Integer.parseInt( (String) parts.get( 2 ) );
            myTimestamp = Long.parseLong( (String) parts.get( 3 ) );
            }

        // From TouchEvent

        public long timestamp()
            {
            return myTimestamp;
            }

        public boolean isPress()
            {
            return myType.equalsIgnoreCase( "press" );
            }

        public boolean isSwipe()
            {
            return myType.equalsIgnoreCase( "swipe" );
            }

        public boolean isRelease()
            {
            return myType.equalsIgnoreCase( "release" );
            }

        public int getX()
            {
            return myPosX;
            }

        public int getY()
            {
            return myPosY;
            }

        private String myType;

        private int myPosX;

        private int myPosY;

        private long myTimestamp;
        }


    private TouchSlider myTouchSlider;

    private final DynamicArray myGestureDataLines = new DynamicArray();

    private final DynamicArray myDataEntryStartIndexes = new DynamicArray();

    private final DynamicArray myDataEntryLines = new DynamicArray();
    }
