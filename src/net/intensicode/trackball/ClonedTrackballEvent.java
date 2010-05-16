//#condition TRACKBALL

package net.intensicode.trackball;

final class ClonedTrackballEvent extends TrackballEvent
    {
    public long timestamp;

    public boolean press;

    public boolean swipe;

    public boolean release;

    public int x;

    public int y;


    public final ClonedTrackballEvent reinitializeWith( final TrackballEvent aTrackballEvent )
        {
        timestamp = aTrackballEvent.timestamp();
        press = aTrackballEvent.isPress();
        swipe = aTrackballEvent.isSwipe();
        release = aTrackballEvent.isRelease();
        x = aTrackballEvent.getX();
        y = aTrackballEvent.getY();
        return this;
        }

    // From TrackballEvent

    public final long timestamp()
        {
        return timestamp;
        }

    public final boolean isPress()
        {
        return press;
        }

    public final boolean isSwipe()
        {
        return swipe;
        }

    public final boolean isRelease()
        {
        return release;
        }

    public final int getX()
        {
        return x;
        }

    public final int getY()
        {
        return y;
        }
    }
