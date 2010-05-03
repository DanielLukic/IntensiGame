package net.intensicode.util;

public final class Assert
    {
    public static void isBetween( final String aMessage, final int aLowerBound, final int aUpperBound, final int aValue )
        {
        //#if ASSERT
        if ( aValue >= aLowerBound && aValue <= aUpperBound ) return;
        fail( aMessage, "is outside", new Integer( aLowerBound ), new Integer( aUpperBound ) );
        //#endif
        }

    public static void between( final String aMessage, final int aLowerBound, final int aUpperBound, final int aValue )
        {
        //#if ASSERT
        isBetween( aMessage, aLowerBound, aUpperBound, aValue );
        //#endif
        }

    public static void isTrue( final String aMessage, final boolean aValue )
        {
        //#if ASSERT
        if ( aValue ) return;
        fail( aMessage, "is false", "bool value" );
        //#endif
        }

    public static void isFalse( final String aMessage, final boolean aValue )
        {
        //#if ASSERT
        if ( !aValue ) return;
        fail( aMessage, "is true", "bool value" );
        //#endif
        }

    public static void isNull( final String aMessage, final Object aObject )
        {
        //#if ASSERT
        if ( aObject == null ) return;
        fail( aMessage, "is not null", aObject );
        //#endif
        }

    public static void isNotNull( final String aMessage, final Object aObject )
        {
        //#if ASSERT
        if ( aObject != null ) return;
        fail( aMessage, "is null", "the object" );
        //#endif
        }

    public static void notNull( final String aMessage, final Object aObject )
        {
        //#if ASSERT
        isNotNull( aMessage, aObject );
        //#endif
        }

    public static void isSame( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if ASSERT
        if ( aExpected == aActual ) return;
        fail( aMessage, "are not the same", aExpected, aActual );
        //#endif
        }

    public static void same( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if ASSERT
        isSame( aMessage, aExpected, aActual );
        //#endif
        }

    public static void isNotSame( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if ASSERT
        if ( aExpected != aActual ) return;
        fail( aMessage, "are the same", aExpected, aActual );
        //#endif
        }

    public static void notSame( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if ASSERT
        isNotSame( aMessage, aExpected, aActual );
        //#endif
        }

    public static void equals( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if ASSERT
        if ( aExpected == aActual || aExpected.equals( aActual ) ) return;
        fail( aMessage, "are not equal", aExpected, aActual );
        //#endif
        }

    public static void equals( final String aMessage, final int aExpected, final int aActual )
        {
        //#if ASSERT
        if ( aExpected == aActual ) return;
        fail( aMessage, "are not equal", Integer.toString( aExpected ), Integer.toString( aActual ) );
        //#endif
        }

    public static void equals( final String aMessage, final long aExpected, final long aActual )
        {
        //#if ASSERT
        if ( aExpected == aActual ) return;
        fail( aMessage, "are not equal", Long.toString( aExpected ), Long.toString( aActual ) );
        //#endif
        }

    //#if !CLDC10

    public static void equals( final String aMessage, final float aExpected, final float aActual, final float aEpsilon )
        {
        //#if ASSERT
        if ( Math.abs( aExpected - aActual ) <= Math.abs( aEpsilon ) ) return;
        fail( aMessage, "are not equal", Float.toString( aExpected ), Float.toString( aActual ) );
        //#endif
        }

    public static void equals( final String aMessage, final double aExpected, final double aActual, final double aEpsilon )
        {
        //#if ASSERT
        if ( Math.abs( aExpected - aActual ) <= Math.abs( aEpsilon ) ) return;
        fail( aMessage, "are not equal", Double.toString( aExpected ), Double.toString( aActual ) );
        //#endif
        }

    //#endif

    public static void notEquals( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if ASSERT
        if ( aExpected != aActual && !aExpected.equals( aActual ) ) return;
        fail( aMessage, "are equal", aExpected, aActual );
        //#endif
        }

    public static void notEquals( final String aMessage, final long aExpected, final long aActual )
        {
        //#if ASSERT
        if ( aExpected != aActual ) return;
        fail( aMessage, "are equal", Long.toString( aExpected ), Long.toString( aActual ) );
        //#endif
        }

    public static void fail( final String aMessage, final String aErrorCondition, final Object aFirst, final Object aSecond )
        {
        //#if ASSERT
        final StringBuffer message = StringUtils.format( "{}: {} and {} {}", new Object[]{ aMessage, aFirst, aSecond, aErrorCondition } );
        fail( message.toString() );
        //#endif
        }

    public static void fail( final String aMessage, final String aErrorCondition, final Object aObject )
        {
        //#if ASSERT
        final StringBuffer message = StringUtils.format( "{}: {} {}", new Object[]{ aMessage, aObject, aErrorCondition } );
        fail( message.toString() );
        //#endif
        }

    public static void fail( final String aMessage )
        {
        //#if ASSERT
        throw new RuntimeException( aMessage );
        //#endif
        }
    }
