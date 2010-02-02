package net.intensicode.core;

public abstract class KeysHandler
    {
    public final KeysConfiguration platformKeysConfiguration;

    public boolean[] dontRepeatFlags = new boolean[NUM_KEY_IDS];

    public int keyRepeatIntervalInTicks = 0;

    public int keyRepeatDelayInTicks = 0;

    //#if DEBUG
    //# public int lastInvalidCode;
    //#endif

    public int lastAction;

    public int lastCode;


    public int softLeftCode;

    public int softRightCode;

    public int softPauseCode;

    public int softDeleteCode;

    public int softBackCode;

    public int softPencilCode;

    public int upCode;

    public int downCode;

    public int leftCode;

    public int rightCode;

    public int fireCode;

    public int fireCodeA;

    public int fireCodeB;

    public int fireCodeC;

    public int fireCodeD;

    public int starCode;

    public int poundCode;


    public static final int INVALID = 0;

    public static final int LEFT_SOFT = 1;

    public static final int RIGHT_SOFT = 2;

    public static final int PAUSE_KEY = 3;

    public static final int DELETE_KEY = 4;

    public static final int BACK_KEY = 5;

    public static final int PENCIL_KEY = 6;

    public static final int UP = 7;

    public static final int DOWN = 8;

    public static final int LEFT = 9;

    public static final int RIGHT = 10;

    public static final int STICK_DOWN = 11;

    public static final int FIRE1 = 12;

    public static final int FIRE2 = 13;

    public static final int FIRE3 = 14;

    public static final int FIRE4 = 15;

    public static final int NUM_KEY_IDS = 16;



    public final void set( final int aKeyID )
        {
        tap( aKeyID );
        resetRepeat( aKeyID, true );
        }

    public final void clear( final int aKeyID )
        {
        resetRepeat( aKeyID, false );
        consume( aKeyID );
        }

    public final boolean checkLeftSoftAndConsume()
        {
        return checkAndConsume( LEFT_SOFT );
        }

    public final boolean checkRightSoftAndConsume()
        {
        return checkAndConsume( RIGHT_SOFT );
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

    public final boolean checkStickDownAndConsume()
        {
        return checkAndConsume( STICK_DOWN );
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

    public final boolean checkFire3AndConsume()
        {
        return checkAndConsume( FIRE3 );
        }

    public final boolean checkFire4AndConsume()
        {
        return checkAndConsume( FIRE4 );
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

    public final void resetCodes()
        {
        //#if DEBUG
        //# net.intensicode.util.Log.debug( "Resetting key codes" );
        //#endif

        softLeftCode = platformKeysConfiguration.softKeyLeft;
        softRightCode = platformKeysConfiguration.softKeyRight;
        softPauseCode = platformKeysConfiguration.softKeyPause;
        softDeleteCode = platformKeysConfiguration.softKeyDelete;
        softBackCode = platformKeysConfiguration.softKeyBack;
        softPencilCode = platformKeysConfiguration.softKeyPencil;

        upCode = platformKeysConfiguration.keyUp;
        downCode = platformKeysConfiguration.keyDown;
        leftCode = platformKeysConfiguration.keyLeft;
        rightCode = platformKeysConfiguration.keyRight;
        fireCode = platformKeysConfiguration.keyFire;
        fireCodeA = platformKeysConfiguration.keyGameA;
        fireCodeB = platformKeysConfiguration.keyGameB;
        fireCodeC = platformKeysConfiguration.keyGameC;
        fireCodeD = platformKeysConfiguration.keyGameD;
        starCode = platformKeysConfiguration.keyStar;
        poundCode = platformKeysConfiguration.keyPound;
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

        dontRepeatFlags[ KeysHandler.FIRE1 ] = true;
        dontRepeatFlags[ KeysHandler.FIRE2 ] = true;
        dontRepeatFlags[ KeysHandler.FIRE3 ] = true;
        dontRepeatFlags[ KeysHandler.FIRE4 ] = true;
        dontRepeatFlags[ KeysHandler.STICK_DOWN ] = true;
        dontRepeatFlags[ KeysHandler.LEFT_SOFT ] = true;
        dontRepeatFlags[ KeysHandler.RIGHT_SOFT ] = true;
        dontRepeatFlags[ KeysHandler.PAUSE_KEY ] = true;
        dontRepeatFlags[ KeysHandler.DELETE_KEY ] = true;
        dontRepeatFlags[ KeysHandler.BACK_KEY ] = true;
        dontRepeatFlags[ KeysHandler.PENCIL_KEY ] = true;

        myNumberOfQueuedKeyEvents = 0;
        }

    public final void queueSetEvent( final int aKeyID )
        {
        if ( myNumberOfQueuedKeyEvents == myQueuedKeyEvents.length ) myNumberOfQueuedKeyEvents--;
        myQueuedKeyEvents[ myNumberOfQueuedKeyEvents++ ] = SET_MASK | aKeyID;
        }

    public final void queueClearEvent( final int aKeyID )
        {
        if ( myNumberOfQueuedKeyEvents == myQueuedKeyEvents.length ) myNumberOfQueuedKeyEvents--;
        myQueuedKeyEvents[ myNumberOfQueuedKeyEvents++ ] = aKeyID;
        }

    public final void onControlTick()
        {
        if ( myNumberOfQueuedKeyEvents > 0 )
            {
            final int event = myQueuedKeyEvents[ 0 ];

            final boolean set = ( event & SET_MASK ) == SET_MASK;
            final int keyCodeID = event & KEYCODE_ID_MASK;
            if ( set ) set( keyCodeID );
            else clear( keyCodeID );

            System.arraycopy( myQueuedKeyEvents, 1, myQueuedKeyEvents, 0, myNumberOfQueuedKeyEvents - 1 );
            myNumberOfQueuedKeyEvents--;
            }

        for ( int idx = 0; idx < NUM_KEY_IDS; idx++ )
            {
            if ( dontRepeatFlags[ idx ] || !myRepeatFlags[ idx ] ) continue;
            repeatKey( idx );
            }
        }

    // Protected Interface

    protected KeysHandler( final KeysConfiguration aKeysConfiguration )
        {
        platformKeysConfiguration = aKeysConfiguration;
        resetCodes();
        reset( 0 );
        }

    // Implementation

    private void tap( final int aKeyID )
        {
        myPressedStates[ aKeyID ] = true;
        }

    private void resetRepeat( final int aKeyID, final boolean aStartRepeat )
        {
        myRepeatFlags[ aKeyID ] = aStartRepeat;
        myRepeatTicks[ aKeyID ] = aStartRepeat ? keyRepeatDelayInTicks : 0;
        }

    private void repeatKey( final int aKeyID )
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



    private int myNumberOfQueuedKeyEvents;

    private final int[] myRepeatTicks = new int[NUM_KEY_IDS];

    private final boolean[] myRepeatFlags = new boolean[NUM_KEY_IDS];

    private final boolean[] myPressedStates = new boolean[NUM_KEY_IDS];

    private final int[] myQueuedKeyEvents = new int[MAX_QUEUED_KEY_EVENTS];


    private static final int SET_MASK = 0x00010000;

    private static final int KEYCODE_ID_MASK = 0x0000FFFF;

    private static final int MAX_QUEUED_KEY_EVENTS = 32;
    }
