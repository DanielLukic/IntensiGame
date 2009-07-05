/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

import net.intensicode.PlatformConfiguration;
import net.intensicode.SystemConfiguration;
import net.intensicode.util.Log;

import javax.microedition.lcdui.Canvas;



/**
 * TODO: Describe this!
 */
public final class Keys
    {
    public PlatformConfiguration platformConfiguration;

    public boolean debugEnabled;

    public int cheatCode;

    public int lastInvalidCode;

    public int lastCode;

    public int lastAction;

    public int keyRepeatDelayInTicks = 0;

    public int keyRepeatIntervalInTicks = 0;

    public boolean[] dontRepeatFlags = new boolean[NUM_KEY_IDS];


    public int upCode;

    public int leftCode;

    public int rightCode;

    public int downCode;

    public int fireCode;

    public int fireCodeA;

    public int fireCodeB;

    public int fireCodeC;

    public int fireCodeD;

    public int starCode;

    public int poundCode;

    public int leftSoftCode;

    public int rightSoftCode;

    public int pauseKeyCode;


    public static final int INVALID = 0;

    public static final int LEFT = 1;

    public static final int RIGHT = 2;

    public static final int UP = 3;

    public static final int DOWN = 4;

    public static final int FIRE1 = 5;

    public static final int FIRE2 = 6;

    public static final int LEFT_SOFT = 7;

    public static final int RIGHT_SOFT = 8;

    public static final int STICK_DOWN = 9;

    public static final int PAUSE_KEY = 10;

    public static final int NUM_KEY_IDS = 11;



    public final void initialize( final Canvas aCanvas )
        {
        if ( platformConfiguration != null ) return;
        platformConfiguration = new PlatformConfiguration( aCanvas );
        }

    public void resetCodes()
        {
        //#if DEBUG
        //# net.intensicode.util.Log.debug( "Resetting key codes" );
        //#endif

        upCode = PlatformConfiguration.KEY_UP;
        leftCode = PlatformConfiguration.KEY_LEFT;
        rightCode = PlatformConfiguration.KEY_RIGHT;
        downCode = PlatformConfiguration.KEY_DOWN;
        fireCode = PlatformConfiguration.KEY_FIRE;
        fireCodeA = PlatformConfiguration.KEY_GAME_A;
        fireCodeB = PlatformConfiguration.KEY_GAME_B;
        fireCodeC = PlatformConfiguration.KEY_GAME_C;
        fireCodeD = PlatformConfiguration.KEY_GAME_D;
        starCode = PlatformConfiguration.KEY_STAR;
        poundCode = PlatformConfiguration.KEY_POUND;
        leftSoftCode = PlatformConfiguration.KEY_SOFT_LEFT;
        rightSoftCode = PlatformConfiguration.KEY_SOFT_RIGHT;
        pauseKeyCode = SystemConfiguration.KEYCODES_PAUSEKEY;
        }

    public final void reset( final int aTicksPerSecond )
        {
        //#if DEBUG
        //# net.intensicode.util.Log.debug( "Resetting key repeat" );
        //#endif

        for ( int idx = 0; idx < NUM_KEY_IDS; idx++ )
            {
            myRepeatTicks[ idx ] = 0;
            myPressedStates[ idx ] = myRepeatFlags[ idx ] = dontRepeatFlags[ idx ] = false;
            }

        keyRepeatDelayInTicks = aTicksPerSecond * 50 / 100;
        keyRepeatIntervalInTicks = aTicksPerSecond * 33 / 100;

        dontRepeatFlags[ Keys.FIRE1 ] = true;
        dontRepeatFlags[ Keys.FIRE2 ] = true;
        dontRepeatFlags[ Keys.STICK_DOWN ] = true;
        dontRepeatFlags[ Keys.LEFT_SOFT ] = true;
        dontRepeatFlags[ Keys.RIGHT_SOFT ] = true;

        myNumberOfQueuedKeyEvents = 0;
        }

    public final boolean checkLeftAndConsume()
        {
        return checkAndConsume( LEFT );
        }

    public final boolean checkRightAndConsume()
        {
        return checkAndConsume( RIGHT );
        }

    public final boolean checkUpAndConsume()
        {
        return checkAndConsume( UP );
        }

    public final boolean checkDownAndConsume()
        {
        return checkAndConsume( DOWN );
        }

    public final boolean checkFireAndConsume()
        {
        return checkAndConsume( FIRE1 ) || checkAndConsume( FIRE2 );
        }

    public final boolean checkFire1AndConsume()
        {
        return checkAndConsume( FIRE1 );
        }

    public final boolean checkFire2AndConsume()
        {
        return checkAndConsume( FIRE2 );
        }

    public final boolean checkLeftSoftAndConsume()
        {
        return checkAndConsume( LEFT_SOFT );
        }

    public final boolean checkRightSoftAndConsume()
        {
        return checkAndConsume( RIGHT_SOFT );
        }

    public final boolean checkStickDownAndConsume()
        {
        return checkAndConsume( STICK_DOWN );
        }

    public final boolean checkAndConsume( final int aKeyID )
        {
        final boolean result = check( aKeyID );
        consume( aKeyID );
        return result;
        }

    public final boolean check( final int aKeyID )
        {
        return myPressedStates[ aKeyID ];
        }

    public final void consume( final int aKeyID )
        {
        myPressedStates[ aKeyID ] = false;
        }

    public final boolean someKeyPressed()
        {
        for ( int idx = 0; idx < myPressedStates.length; idx++ )
            {
            if ( myPressedStates[ idx ] ) return true;
            }
        return myNumberOfQueuedKeyEvents > 0;
        }

    // Package Interface

    Keys()
        {
        reset( 0 );
        resetCodes();
        }

    final void onControlTick()
        {
        if ( myNumberOfQueuedKeyEvents > 0 )
            {
            final int event = myQueuedKeyEvents[ 0 ];

            final boolean set = (event & SET_MASK) == SET_MASK;
            final int keyCodeID = event & KEYCODE_ID_MASK;
            if ( set ) set( keyCodeID );
            else clear( keyCodeID );

            System.arraycopy( myQueuedKeyEvents, 1, myQueuedKeyEvents, 0, myNumberOfQueuedKeyEvents - 1 );
            myNumberOfQueuedKeyEvents--;
            }

        for ( int idx = 0; idx < NUM_KEY_IDS; idx++ )
            {
            if ( dontRepeatFlags[ idx ] || myRepeatFlags[ idx ] == false ) continue;
            repeatKey( idx );
            }
        }

    final void keyPressed( final int aKeyCode, final int aGameAction )
        {
        final int keyID = keyByCode( aKeyCode, aGameAction, true );
        if ( keyID != INVALID ) queueSet( keyID );

        //#if RUNME
        final int code = getCanvasCode( aKeyCode );
        //#else
        //# final int code = aKeyCode;
        //#endif

        //#if CHEAT
        if ( myCheatSequenceIndex == CHEAT_SEQUENCE.length )
            {
            //#if DEBUG
            Log.debug( "Setting cheat code: {}", code );
            //#endif
            cheatCode = code - 48 + 70;
            myCheatSequenceIndex = 0;
            }
        myCheatSequenceIndex = handleSequence( CHEAT_SEQUENCE, myCheatSequenceIndex, code );
        //#endif

        myDebugSequenceIndex = handleSequence( DEBUG_SEQUENCE, myDebugSequenceIndex, code );
        if ( myDebugSequenceIndex == DEBUG_SEQUENCE.length )
            {
            debugEnabled = true;
            myDebugSequenceIndex = 0;
            }
        }

    final void keyReleased( final int aKeyCode, final int aGameAction )
        {
        final int keyID = keyByCode( aKeyCode, aGameAction, false );
        if ( keyID != INVALID ) queueClear( keyID );
        }

    final void set( final int aKeyID )
        {
        tap( aKeyID );
        resetRepeat( aKeyID, true );
        }

    final void clear( final int aKeyID )
        {
        resetRepeat( aKeyID, false );
        consume( aKeyID );
        }

    // Implementation

    private final void queueSet( final int aKeyID )
        {
        if ( myNumberOfQueuedKeyEvents == myQueuedKeyEvents.length ) myNumberOfQueuedKeyEvents--;
        myQueuedKeyEvents[ myNumberOfQueuedKeyEvents++ ] = SET_MASK | aKeyID;
        }

    private final void queueClear( final int aKeyID )
        {
        if ( myNumberOfQueuedKeyEvents == myQueuedKeyEvents.length ) myNumberOfQueuedKeyEvents--;
        myQueuedKeyEvents[ myNumberOfQueuedKeyEvents++ ] = aKeyID;
        }

    private final void tap( final int aKeyID )
        {
        myPressedStates[ aKeyID ] = true;
        }

    private final void resetRepeat( final int aKeyID, final boolean aStartRepeat )
        {
        myRepeatFlags[ aKeyID ] = aStartRepeat;
        myRepeatTicks[ aKeyID ] = aStartRepeat ? keyRepeatDelayInTicks : 0;
        }

    private final void repeatKey( final int aKeyID )
        {
        if ( myRepeatTicks[ aKeyID ] > 0 )
            {
            myRepeatTicks[ aKeyID ]--;
            }
        else
            {
            myRepeatTicks[ aKeyID ] = keyRepeatIntervalInTicks;
            tap( aKeyID );
            }
        }

    private final int keyByCode( final int aCode, final int aGameAction, final boolean aPressed )
        {
        lastCode = aCode;
        lastAction = aGameAction;

        if ( aGameAction == leftCode ) return LEFT;
        if ( aGameAction == rightCode ) return RIGHT;
        if ( aGameAction == upCode ) return UP;
        if ( aGameAction == downCode ) return DOWN;
        if ( aGameAction == fireCode ) return FIRE1;
        if ( aGameAction == fireCodeA ) return FIRE1;
        if ( aGameAction == fireCodeB ) return FIRE2;
        if ( aGameAction == fireCodeC ) return FIRE1;
        if ( aGameAction == fireCodeD ) return FIRE2;
        if ( aGameAction == starCode ) return FIRE1;
        if ( aGameAction == poundCode ) return FIRE2;
        if ( aGameAction == leftSoftCode ) return LEFT_SOFT;
        if ( aGameAction == rightSoftCode ) return RIGHT_SOFT;

        //#if RUNME
        if ( aGameAction == 44 ) return LEFT_SOFT;
        if ( aGameAction == 46 ) return RIGHT_SOFT;
        //#endif

        if ( aCode == leftCode ) return LEFT;
        if ( aCode == rightCode ) return RIGHT;
        if ( aCode == upCode ) return UP;
        if ( aCode == downCode ) return DOWN;
        if ( aCode == fireCode ) return FIRE1;
        if ( aCode == fireCodeA ) return FIRE1;
        if ( aCode == fireCodeB ) return FIRE2;
        if ( aCode == fireCodeC ) return FIRE1;
        if ( aCode == fireCodeD ) return FIRE2;
        if ( aCode == starCode ) return FIRE1;
        if ( aCode == poundCode ) return FIRE2;
        if ( aCode == leftSoftCode ) return LEFT_SOFT;
        if ( aCode == rightSoftCode ) return RIGHT_SOFT;
        if ( aCode == pauseKeyCode ) return PAUSE_KEY;

        // Stupid fall backs for real devices..
        switch ( aCode )
            {
            case -6:
            case -1:
            case -21:
            case 21:
            case -10:
            case -202:
            case 57345:
                return LEFT_SOFT;
            case -7:
            case -4:
            case -22:
            case -20:
            case 22:
            case -11:
            case -203:
            case 57346:
                return RIGHT_SOFT;
            }

        //#if DEBUG && CHEAT && RUNME
        cheatCode = aPressed ? aCode : 0;
        //#endif

        //#if DEBUG
        //# net.intensicode.util.Log.debug( "Unhandled keycode: {}", aCode );
        //# lastInvalidCode = aCode;
        //#endif

        return INVALID;
        }

    private final int handleSequence( final int[] aSequence, final int aIndex, final int aKeyCode )
        {
        if ( aSequence[ aIndex ] != aKeyCode )
            {
            if ( aIndex == 0 ) return 0;
            return handleSequence( aSequence, 0, aKeyCode );
            }
        return aIndex + 1;
        }

    private final int getCanvasCode( final int aKeyCode )
        {
        if ( aKeyCode == 92 ) return PlatformConfiguration.KEY_POUND;
        if ( aKeyCode == 93 ) return PlatformConfiguration.KEY_STAR;
        return aKeyCode;
        }


    private int myCheatSequenceIndex;

    private int myDebugSequenceIndex;

    private int myNumberOfQueuedKeyEvents;

    private final int[] myQueuedKeyEvents = new int[MAX_QUEUED_KEY_EVENTS];

    private final boolean[] myPressedStates = new boolean[NUM_KEY_IDS];

    private final int[] myRepeatTicks = new int[NUM_KEY_IDS];

    private final boolean[] myRepeatFlags = new boolean[NUM_KEY_IDS];

    private static final int SET_MASK = 0x00010000;

    private static final int KEYCODE_ID_MASK = 0x0000FFFF;

    private static final int MAX_QUEUED_KEY_EVENTS = 32;

    private static final int[] DEBUG_SEQUENCE = new int[]{ PlatformConfiguration.KEY_POUND, PlatformConfiguration.KEY_STAR, PlatformConfiguration.KEY_1, PlatformConfiguration.KEY_5, PlatformConfiguration.KEY_9 };

    //#if CHEAT
    private static final int[] CHEAT_SEQUENCE = new int[]{ PlatformConfiguration.KEY_POUND, PlatformConfiguration.KEY_STAR, PlatformConfiguration.KEY_3, PlatformConfiguration.KEY_5, PlatformConfiguration.KEY_7 };

    //#endif
    }
