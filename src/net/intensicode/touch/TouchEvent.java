//#condition TOUCH

package net.intensicode.touch;

public abstract class TouchEvent
    {
    public abstract long timestamp();

    public abstract boolean isPress();

    public abstract boolean isSwipe();

    public abstract boolean isRelease();

    public abstract int getX();

    public abstract int getY();

    // From Object

    public final boolean equals( final Object aObject )
        {
        if ( !( aObject instanceof TouchEvent ) ) return false;
        final TouchEvent that = (TouchEvent) aObject;
        if ( this.isPress() != that.isPress() ) return false;
        if ( this.isSwipe() != that.isSwipe() ) return false;
        if ( this.isRelease() != that.isRelease() ) return false;
        if ( this.getX() != that.getX() ) return false;
        if ( this.getY() != that.getY() ) return false;
        //#if DEBUG_TOUCH
        if ( this.timestamp() != that.timestamp() ) throw new IllegalStateException( "TouchEvent matches except for timestamp");
        //#endif
        return true;
        }

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "TouchEvent(" );
        buffer.append( isPress() ? "press" : isSwipe() ? "swipe" : isRelease() ? "release" : "unknown" );
        buffer.append( ',' );
        buffer.append( getX() );
        buffer.append( ',' );
        buffer.append( getY() );
        buffer.append( ',' );
        buffer.append( timestamp() );
        buffer.append( ")" );
        return buffer.toString();
        }
    }
