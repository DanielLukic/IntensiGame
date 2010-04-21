package core;

import junit.framework.TestCase;
import net.intensicode.core.*;

public final class TrackballControllerTest extends TestCase
    {
    protected void setUp() throws Exception
        {
        myController = new TrackballController( new TrackballConfiguration(), new FakePlatformContext() );
        myController.rightDelta = 1;
        myController.downDelta = 3;
        }

    public final void test_shouldIgnoreDeltaX_1()
        {
        myController.configuration.directionIgnoreFactor = 1f;
        assertTrue( "3 > 1 * 1", myController.shouldIgnoreDeltaX() );
        assertFalse( "1 > 3 * 1", myController.shouldIgnoreDeltaY() );
        }

    public final void test_shouldIgnoreDeltaX_2()
        {
        myController.configuration.directionIgnoreFactor = 2f;
        assertTrue( "3 > 1 * 2", myController.shouldIgnoreDeltaX() );
        assertFalse( "1 > 3 * 2", myController.shouldIgnoreDeltaY() );
        }

    public final void test_shouldIgnoreDeltaX_3()
        {
        myController.configuration.directionIgnoreFactor = 3f;
        assertFalse( "3 > 1 * 3", myController.shouldIgnoreDeltaX() );
        assertFalse( "1 > 3 * 3", myController.shouldIgnoreDeltaY() );
        }

    private TrackballController myController;
    }
