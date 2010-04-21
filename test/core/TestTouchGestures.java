package core;

import junit.framework.TestCase;
import net.intensicode.core.*;
import net.intensicode.util.*;

import java.io.IOException;

public final class TestTouchGestures extends TestCase
    {
    public final void testUpGestureIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_up.txt", "NORTH" );
        }

    public final void testUpRightGestureIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_up_right.txt", "NORTH_EAST" );
        }

    public final void testUpLeftGestureIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_up_left.txt", "NORTH_WEST" );
        }

    public final void testRightGestureIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_right.txt", "EAST" );
        }

    public final void testLeftGestureIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_left.txt", "WEST" );
        }

    public final void testTapGestureIsRecognized() throws IOException
        {
        runGestureData( "gesture_data_tap.txt", "TAP" );
        }

    private void runGestureData( final String aResourcePath, final String aExpectedGesture ) throws IOException
        {
        loadGestureData( aResourcePath );
        for ( int idx = 0; idx < numberOfDataEntries(); idx++ )
            {
            setGestureDataEntry( idx );
            createTouchGesturesObject();
            sendGestureDataToObject();
            assertEquals( aResourcePath + " - index " + idx, aExpectedGesture, resultingGesture() );
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

    private void createTouchGesturesObject()
        {
        final TouchGesturesConfiguration configuration = new TouchGesturesConfiguration();
        myTouchGestures = new TouchGestures( configuration, new FakePlatformContext() );
        }

    private void sendGestureDataToObject()
        {
        for ( int idx = 0; idx < myDataEntryLines.size; idx++ )
            {
            final String touchEventLine = (String) myDataEntryLines.get( idx );
            final String eventDataString = extractEventData( touchEventLine );
            final TouchEventData eventData = new TouchEventData( eventDataString );
            myTouchGestures.onTouchEvent( eventData );
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

    private String resultingGesture()
        {
        final StringBuffer buffer = new StringBuffer();
        for ( int idx = 0; idx < myTouchGestures.gesture.size; idx++ )
            {
            buffer.append( myTouchGestures.gesture.get( idx ) );
            buffer.append( ',' );
            }
        if ( buffer.length() > 0 ) buffer.setLength( buffer.length() - 1 );
        return buffer.toString();
        }


    private TouchGestures myTouchGestures;

    private final DynamicArray myGestureDataLines = new DynamicArray();

    private final DynamicArray myDataEntryStartIndexes = new DynamicArray();

    private final DynamicArray myDataEntryLines = new DynamicArray();
    }
