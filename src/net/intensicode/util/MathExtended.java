package net.intensicode.util;

public class MathExtended
    {
    public final static double SQRT3 = 1.732050807568877294;

    public final static double LOGdiv2 = -0.6931471805599453094;



    public static double asin( double value )
        {
        if ( value < -1.0 || value > 1.0 ) return Double.NaN;
        if ( value == -1.0 ) return -Math.PI / 2;
        if ( value == 1 ) return Math.PI / 2;
        return atan( value / Math.sqrt( 1 - value * value ) );
        }

    public static double acos( double value )
        {
        double f = asin( value );
        if ( f == Double.NaN ) return f;
        return Math.PI / 2 - f;
        }

    public static double atan( double value )
        {
        boolean signChange = false;
        boolean Invert = false;
        int sp = 0;
        double x2, a;

        if ( value < 0.0 )
            {
            value = -value;
            signChange = true;
            }

        if ( value > 1.0 )
            {
            value = 1 / value;
            Invert = true;
            }

        while ( value > Math.PI / 12 )
            {
            sp++;
            a = value + SQRT3;
            a = 1 / a;
            value = value * SQRT3;
            value = value - 1;
            value = value * a;
            }

        x2 = value * value;
        a = x2 + 1.4087812;
        a = 0.55913709 / a;
        a = a + 0.60310579;
        a = a - ( x2 * 0.05160454 );
        a = a * value;

        while ( sp > 0 )
            {
            a = a + Math.PI / 6;
            sp--;
            }

        if ( Invert ) a = Math.PI / 2 - a;
        if ( signChange ) a = -a;

        return a;
        }

    public static double atan2( double y, double x )
        {
        if ( y == 0. && x == 0.0 ) return 0.;
        if ( x > 0.0 ) return atan( y / x );

        if ( x < 0.0 )
            {
            if ( y < 0.0 ) return -( Math.PI - atan( y / x ) );
            else return Math.PI - atan( -y / x );
            }

        if ( y < 0.0 ) return -Math.PI / 2.;
        else return Math.PI / 2.;
        }

    public static double log( double value )
        {
        if ( !( value > 0.0 ) ) return Double.NaN;
        if ( value == 1.0 ) return 0.0;

        // Argument of _log must be (0; 1]
        if ( value > 1.0 )
            {
            value = 1 / value;
            return -_log( value );
            }

        return _log( value );
        }

    public static double exp( double value )
        {
        if ( value == 0.0 ) return 1.;

        double f = 1;
        long d = 1;
        double k;
        boolean isless = ( value < 0.0 );
        if ( isless ) value = -value;
        k = value / d;

        for ( long i = 2; i < 50; i++ )
            {
            f = f + k;
            k = k * value / i;
            }

        if ( isless ) return 1 / f;
        else return f;
        }

    // Implementation

    private static double _log( double value )
        {
        if ( !( value > 0.0 ) ) return Double.NaN;

        double f = 0.0;

        int appendix = 0;
        while ( value > 0.0 && value <= 1.0 )
            {
            value *= 2.0;
            appendix++;
            }

        value /= 2.0;
        appendix--;

        double y1 = value - 1.0;
        double y2 = value + 1.0;
        double y = y1 / y2;

        double k = y;
        y2 = k * y;

        for ( long i = 1; i < 50; i += 2 )
            {
            f += k / i;
            k *= y2;
            }

        f *= 2.0;
        for ( int i = 0; i < appendix; i++ )
            {
            f += LOGdiv2;
            }

        return f;
        }

    public static double pow( double x, double y )
        {
        if ( y == 0.0 ) return 1.0;
        if ( y == 1.0 ) return x;
        if ( x == 0.0 ) return 0.0;
        if ( x == 1.0 ) return 1.0;

        long l = (long) Math.floor( y );
        boolean integerValue = ( y == (double) l );

        if ( integerValue )
            {
            boolean neg = false;
            if ( y < 0.0 ) neg = true;

            double result = x;
            for ( long i = 1; i < ( neg ? -l : l ); i++ )
                {
                result = result * x;
                }

            if ( neg ) return 1.0 / result;
            else return result;
            }
        else
            {
            if ( x > 0.0 ) return exp( y * log( x ) );
            else return Double.NaN;
            }
        }
    }
