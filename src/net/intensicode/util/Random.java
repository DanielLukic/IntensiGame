package net.intensicode.util;

public final class Random
    {
    public static final Random INSTANCE = new Random( 1704 );


    public Random()
        {
        this( System.currentTimeMillis() );
        }

    public Random( final long aSeed )
        {
        setSeed( aSeed );
        }

    public final long getSeed()
        {
        return myCurrentSeed;
        }

    public final void setSeed( final long aSeed )
        {
        myCurrentSeed = aSeed;
        myRandom.setSeed( aSeed );
        }

    public final int nextInt()
        {
        return (int) ( myCurrentSeed = ( myCurrentSeed * MULTIPLIER + ADDEND ) % MASK );
        }

    public final int nextInt( final int aExclusiveMaxValue )
        {
        final int value = nextInt() & 0x7FFFFFFF;
        return value % aExclusiveMaxValue;
        }

    public float nextFloat( final float aMaximum )
        {
        final float random = myRandom.nextFloat();
        return random * aMaximum;
        }


    private long myCurrentSeed;

    private static final long MULTIPLIER = 25214903917L;

    private static final long ADDEND = 11;

    private static final long MASK = 281474976710655L;

    private final java.util.Random myRandom = new java.util.Random();
    }
