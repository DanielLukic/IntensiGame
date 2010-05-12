package core;

import net.intensicode.trackball.TrackballEvent;
import net.intensicode.util.*;

final class TrackballEventTestData extends TrackballEvent
    {
    TrackballEventTestData( final String aDataEventLine )
        {
        final DynamicArray parts = StringUtils.splitString( aDataEventLine, ",", false );
        myType = (String) parts.get( 0 );
        myPosX = Integer.parseInt( (String) parts.get( 1 ) );
        myPosY = Integer.parseInt( (String) parts.get( 2 ) );
        myTimestamp = Long.parseLong( (String) parts.get( 3 ) );
        }

    // From TrackballEvent

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
