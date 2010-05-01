//#condition TRACKBALL

package net.intensicode.trackball;

public abstract class TrackballEvent
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
        if ( !( aObject instanceof TrackballEvent ) ) return false;
        final TrackballEvent that = (TrackballEvent) aObject;
        if ( this.isPress() != that.isPress() ) return false;
        if ( this.isSwipe() != that.isSwipe() ) return false;
        if ( this.isRelease() != that.isRelease() ) return false;
        if ( this.getX() != that.getX() ) return false;
        if ( this.getY() != that.getY() ) return false;
        return true;
        }

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "TrackballEvent(" );
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
