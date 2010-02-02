package net.intensicode.util;

public final class Assert
    {
    //#if DEBUG

    public static void isBetween( final String aMessage, final int aLowerBound, final int aUpperBound, final int aValue )
        {
        if ( aValue >= aLowerBound && aValue <= aUpperBound ) return;
        fail( aMessage, "is outside", new Integer( aLowerBound ), new Integer( aUpperBound ) );
        }

    public static void between( final String aMessage, final int aLowerBound, final int aUpperBound, final int aValue )
        {
        isBetween( aMessage, aLowerBound, aUpperBound, aValue );
        }

    public static void isTrue( final String aMessage, final boolean aValue )
        {
        if ( aValue ) return;
        fail( aMessage, "is false", "bool value" );
        }

    public static void isFalse( final String aMessage, final boolean aValue )
        {
        if ( !aValue ) return;
        fail( aMessage, "is true", "bool value" );
        }

    public static void isNull( final String aMessage, final Object aObject )
        {
        if ( aObject == null ) return;
        fail( aMessage, "is not null", aObject );
        }

    public static void isNotNull( final String aMessage, final Object aObject )
        {
        if ( aObject != null ) return;
        fail( aMessage, "is null", "the object" );
        }

    public static void notNull( final String aMessage, final Object aObject )
        {
        isNotNull( aMessage, aObject );
        }

    public static void equals( final String aMessage, final Object aExpected, final Object aActual )
        {
        if ( aExpected == aActual || aExpected.equals( aActual ) ) return;
        fail( aMessage, "are not the same", aExpected, aActual );
        }

    public static void notEquals( final String aMessage, final Object aExpected, final Object aActual )
        {
        if ( aExpected != aActual && !aExpected.equals( aActual ) ) return;
        fail( aMessage, "are the same", aExpected, aActual );
        }

    public static void fail( final String aMessage, final String aErrorCondition, final Object aFirst, final Object aSecond )
        {
        final StringBuffer message = StringUtils.format( "{}: {} and {} {}", new Object[]{ aMessage, aFirst, aSecond, aErrorCondition } );
        fail( message.toString() );
        }

    public static void fail( final String aMessage, final String aErrorCondition, final Object aObject )
        {
        final StringBuffer message = StringUtils.format( "{}: {} {}", new Object[]{ aMessage, aObject, aErrorCondition } );
        fail( message.toString() );
        }

    public static void fail( final String aMessage )
        {
        //#if DEBUG
        throw new RuntimeException( aMessage );
        //#endif
        }

    //#endif
    }
