package net.intensicode.util;

public final class Assert
    {
    public static void isBetween( final String aMessage, final int aLowerBound, final int aUpperBound, final int aValue )
        {
        //#if DEBUG
        if ( aValue >= aLowerBound && aValue <= aUpperBound ) return;
        fail( aMessage, "is outside", new Integer( aLowerBound ), new Integer( aUpperBound ) );
        //#endif
        }

    public static void between( final String aMessage, final int aLowerBound, final int aUpperBound, final int aValue )
        {
        //#if DEBUG
        isBetween( aMessage, aLowerBound, aUpperBound, aValue );
        //#endif
        }

    public static void isTrue( final String aMessage, final boolean aValue )
        {
        //#if DEBUG
        if ( aValue ) return;
        fail( aMessage, "is false", "bool value" );
        //#endif
        }

    public static void isFalse( final String aMessage, final boolean aValue )
        {
        //#if DEBUG
        if ( !aValue ) return;
        fail( aMessage, "is true", "bool value" );
        //#endif
        }

    public static void isNull( final String aMessage, final Object aObject )
        {
        //#if DEBUG
        if ( aObject == null ) return;
        fail( aMessage, "is not null", aObject );
        //#endif
        }

    public static void isNotNull( final String aMessage, final Object aObject )
        {
        //#if DEBUG
        if ( aObject != null ) return;
        fail( aMessage, "is null", "the object" );
        //#endif
        }

    public static void notNull( final String aMessage, final Object aObject )
        {
        //#if DEBUG
        isNotNull( aMessage, aObject );
        //#endif
        }

    public static void isSame( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if DEBUG
        if ( aExpected == aActual ) return;
        fail( aMessage, "are not the same", aExpected, aActual );
        //#endif
        }

    public static void same( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if DEBUG
        isSame( aMessage, aExpected, aActual );
        //#endif
        }

    public static void isNotSame( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if DEBUG
        if ( aExpected != aActual ) return;
        fail( aMessage, "are the same", aExpected, aActual );
        //#endif
        }

    public static void notSame( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if DEBUG
        isNotSame( aMessage, aExpected, aActual );
        //#endif
        }

    public static void equals( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if DEBUG
        if ( aExpected == aActual || aExpected.equals( aActual ) ) return;
        fail( aMessage, "are not equal", aExpected, aActual );
        //#endif
        }

    public static void notEquals( final String aMessage, final Object aExpected, final Object aActual )
        {
        //#if DEBUG
        if ( aExpected != aActual && !aExpected.equals( aActual ) ) return;
        fail( aMessage, "are equal", aExpected, aActual );
        //#endif
        }

    public static void fail( final String aMessage, final String aErrorCondition, final Object aFirst, final Object aSecond )
        {
        //#if DEBUG
        final StringBuffer message = StringUtils.format( "{}: {} and {} {}", new Object[]{ aMessage, aFirst, aSecond, aErrorCondition } );
        fail( message.toString() );
        //#endif
        }

    public static void fail( final String aMessage, final String aErrorCondition, final Object aObject )
        {
        //#if DEBUG
        final StringBuffer message = StringUtils.format( "{}: {} {}", new Object[]{ aMessage, aObject, aErrorCondition } );
        fail( message.toString() );
        //#endif
        }

    public static void fail( final String aMessage )
        {
        //#if DEBUG
        throw new RuntimeException( aMessage );
        //#endif
        }
    }
