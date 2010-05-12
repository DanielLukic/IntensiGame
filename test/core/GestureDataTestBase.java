package core;

import junit.framework.TestCase;
import net.intensicode.util.*;

import java.io.IOException;

abstract class GestureDataTestBase extends TestCase
    {
    private final String myStartOfDataIdentifier;

    private final String myDataEventLineIdentifier;


    GestureDataTestBase( final String aStartOfDataIdentifier, final String aDataEventLineIdentifier )
        {
        myStartOfDataIdentifier = aStartOfDataIdentifier;
        myDataEventLineIdentifier = aDataEventLineIdentifier;
        }

    protected abstract void createTestObject();

    protected abstract void sendGestureDataToTestObject( final String aEventDataString );

    protected abstract Object getTestObject();

    // Protected API

    protected void runGestureData( final String aResourcePath, final GestureMatcher aGestureMatcher ) throws IOException
        {
        loadGestureData( aResourcePath );
        for ( int idx = 0; idx < numberOfDataEntries(); idx++ )
            {
            setGestureDataEntry( idx );
            createTestObject();
            sendGestureDataToTestObject();
            assertTrue( aResourcePath + " - index " + idx, aGestureMatcher.matched( getTestObject() ) );
            }
        }

    // Implementation

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

    private boolean isStartOfDataEntry( final String aLine )
        {
        return aLine.indexOf( myStartOfDataIdentifier ) != -1;
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
            if ( isDataLine( line ) ) addToDataEntry( line );
            }
        }

    private boolean isDataLine( final String aLine )
        {
        return aLine.indexOf( myDataEventLineIdentifier ) != -1;
        }

    private void resetDataEntry()
        {
        myDataEntryLines.clear();
        }

    private void addToDataEntry( final String aLine )
        {
        myDataEntryLines.add( aLine );
        }

    private void sendGestureDataToTestObject()
        {
        for ( int idx = 0; idx < myDataEntryLines.size; idx++ )
            {
            final String eventDataLine = (String) myDataEntryLines.get( idx );
            final String eventDataString = extractEventData( eventDataLine );
            sendGestureDataToTestObject( eventDataString );
            }
        }

    private String extractEventData( final String aEventDataLine )
        {
        final int offset = aEventDataLine.indexOf( myDataEventLineIdentifier );
        final int startOfData = aEventDataLine.indexOf( '(', offset );
        final int endOfData = aEventDataLine.indexOf( ')', offset );
        return aEventDataLine.substring( startOfData + 1, endOfData );
        }

    private final DynamicArray myGestureDataLines = new DynamicArray();

    private final DynamicArray myDataEntryStartIndexes = new DynamicArray();

    private final DynamicArray myDataEntryLines = new DynamicArray();
    }
