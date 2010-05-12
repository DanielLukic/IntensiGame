package core;

import net.intensicode.PlatformContext;
import net.intensicode.trackball.*;

import java.io.IOException;

public final class TrackballControllerDataTest extends GestureDataTestBase
    {
    public TrackballControllerDataTest()
        {
        super( "SNIP", "TRACKBALL" );
        }

    public final void test_trackball_data_down_drop() throws IOException
        {
        runGestureData( "trackball_data_down_drop.txt", new EnoughDownTicksForDropMatcher() );
        }

    protected void createTestObject()
        {
        final TrackballConfiguration configuration = new TrackballConfiguration();
        final PlatformContext context = new FakePlatformContext();
        myTestObject = new TrackballController( configuration, context );
        }

    protected void sendGestureDataToTestObject( final String aEventDataString )
        {
        final TrackballEventTestData eventData = new TrackballEventTestData( aEventDataString );
        myTestObject.onTrackballEvent( eventData );
        }

    protected Object getTestObject()
        {
        return myTestObject;
        }


    private TrackballController myTestObject;


    private static class EnoughDownTicksForDropMatcher implements GestureMatcher
        {
        public static final int DOWN_TICKS_FOR_DROP = 3;

        public final boolean matched( final Object aTestObject )
            {
            final TrackballController controller = (TrackballController) aTestObject;
            return controller.downDelta >= DOWN_TICKS_FOR_DROP;
            }
        }
    }
