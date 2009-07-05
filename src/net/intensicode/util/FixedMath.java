package net.intensicode.util;



public final class FixedMath
    {
    public static final int FIXED_SHIFT = 12;

    public static final int FIXED_MULTIPLIER = 1 << FIXED_SHIFT;

    public static final int FIXED_MASK = FIXED_MULTIPLIER - 1;

    public static final int FIXED_ONE = FIXED_MULTIPLIER;

    public static final int FIXED_HALF = FIXED_ONE >> 1;

    public static final int FIXED_0 = 0;

    public static final int FIXED_0_25 = FIXED_ONE / 4;

    public static final int FIXED_0_5 = FIXED_ONE / 2;

    public static final int FIXED_1 = FIXED_ONE;

    public static final int FIXED_5 = FIXED_ONE * 5;

    public static final int FIXED_25 = FIXED_ONE * 25;

    public static final int FIXED_30 = FIXED_ONE * 30;

    public static final int FIXED_50 = FIXED_ONE * 50;

    public static final int FIXED_100 = FIXED_ONE * 100;

    public static final int FIXED_180 = FIXED_ONE * 180;

    public static final int FIXED_360 = FIXED_ONE * 360;



    public static final int toFixed( final int aInteger )
        {
        return aInteger << FIXED_SHIFT;
        }

    public static final int toInt( final int aFixedDecimal )
        {
        return aFixedDecimal >> FIXED_SHIFT;
        }

    public static final long toInt( final long aFixedDecimal )
        {
        return aFixedDecimal >> FIXED_SHIFT;
        }

    public static final int toIntRounded( final int aFixedDecimal )
        {
        return ( aFixedDecimal + FIXED_HALF ) >> FIXED_SHIFT;
        }

    public static final void toFixed( final Position aPosition )
        {
        aPosition.x = toFixed( aPosition.x );
        aPosition.y = toFixed( aPosition.y );
        }

    public static final void toInt( final Position aFixedPosition )
        {
        aFixedPosition.x = toInt( aFixedPosition.x );
        aFixedPosition.y = toInt( aFixedPosition.y );
        }

    public static final void toIntRounded( final Position aFixedPosition )
        {
        aFixedPosition.x = toIntRounded( aFixedPosition.x );
        aFixedPosition.y = toIntRounded( aFixedPosition.y );
        }

    public static final int fraction( final int aFixedDecimal )
        {
        return aFixedDecimal & FIXED_MASK;
        }

    public static final int mul( final int aFixedDecimal1, final int aFixedDecimal2 )
        {
        final long a = ( (long) aFixedDecimal1 ) * ( (long) aFixedDecimal2 );
        return (int) ( a >> FIXED_SHIFT );
        }

    public static final int div( final int aFixedDecimal1, final int aFixedDecimal2 )
        {
        final long a = (long) aFixedDecimal1;
        final long b = (long) aFixedDecimal2;
        return (int) ( ( a << FIXED_SHIFT ) / b );
        }

    public static final int length( final int aFixedDeltaX, final int aFixedDeltaY )
        {
        final long a2 = FixedMath.mul( aFixedDeltaX, aFixedDeltaX );
        final long b2 = FixedMath.mul( aFixedDeltaY, aFixedDeltaY );
        final long c2 = FixedMath.toInt( ( a2 + b2 ) << LENGTH_PRE_SHIFT );
        final int length = IntegerSquareRoot.sqrt( (int) c2 );
        return FixedMath.toFixed( length ) >> LENGTH_POST_SHIFT;
        }

    public static final String toString( final int aFixedValue )
        {
        return Integer.toString( toInt( aFixedValue ) );
        }



    private static final int LENGTH_PRE_SHIFT = FIXED_SHIFT;

    private static final int LENGTH_POST_SHIFT = LENGTH_PRE_SHIFT / 2;
    }
