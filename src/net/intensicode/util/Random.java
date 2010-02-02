package net.intensicode.util;

public final class Random
    {
    public static final Random INSTANCE = new Random( 1704 );



    public Random()
        {
        myCurrentSeed = System.currentTimeMillis();
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



    private long myCurrentSeed;

    private static final long MULTIPLIER = 25214903917L;

    private static final long ADDEND = 11;

    private static final long MASK = 281474976710655L;
    }
