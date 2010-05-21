package net.intensicode.core;

import net.intensicode.util.Assert;

public final class GameTiming
    {
    /**
     * The system will ensure that this many control ticks will be issued every second.
     */
    public int ticksPerSecond = 32;

    /**
     * The system will sleep instead of rendering more frames per second than this.
     */
    public int maxFramesPerSecond = 32;

    public int maxTicksPerFrame = 2;

    public int measuredFramesPerSecond;

    public int measuredTicksPerSecond;


    final void reset()
        {
        myLastAveragingStart = myLastTimingStart = myLastFrameStart = myLastTickStart = nowInMs();
        myRemainingTickTime = myFrameCount = myAveragingFrameCount = 0;
        }

    final boolean tooMuchTimeHasPassed()
        {
        final long now = nowInMs();

        final long lastFrameTime = now - myLastFrameStart;
        if ( lastFrameTime > ONE_SECOND_IN_MS ) return true;

        final long accumulatedFrameTime = now - myLastAveragingStart;
        if ( accumulatedFrameTime < MIN_TIME_FOR_AVERAGING_IN_MS ) return false;

        final long averageFrameTime = accumulatedFrameTime / myAveragingFrameCount;

        myLastAveragingStart = now;
        myAveragingFrameCount = 0;

        final long maxFrameTime = ONE_SECOND_IN_MS / maxFramesPerSecond;
        return averageFrameTime > maxFrameTime;
        }

    final boolean enoughTimeHasPassedForUpdatingStats()
        {
        return ( accumulatedFrameTime() > MIN_TIME_FOR_STATS_IN_MS );
        }

    final void updateStats()
        {
        final long accumulatedFrameTime = accumulatedFrameTime();
        //#if DEBUG
        Assert.isTrue( "accumulated frame time has to be valid", accumulatedFrameTime > 0 );
        //#endif

        measuredFramesPerSecond = (int) ( myFrameCount * ONE_SECOND_IN_MS / accumulatedFrameTime );
        measuredTicksPerSecond = (int) ( myTickCount * ONE_SECOND_IN_MS / accumulatedFrameTime );

        myFrameCount = myTickCount = 0;
        myLastTimingStart += accumulatedFrameTime;
        }

    final long frameWaitTime()
        {
        //#if DEBUG
        Assert.isTrue( "max fps has to be valid", maxFramesPerSecond > 0 );
        //#endif
        final long frameTime = nowInMs() - myLastFrameStart;
        final long expectedTime = ONE_SECOND_IN_MS / maxFramesPerSecond;
        final long delta = expectedTime - frameTime;
        return Math.max( 0, delta );
        }

    final int numberOfPendingTicks()
        {
        final long now = nowInMs();

        final long timeForTicks = ( now - myLastTickStart ) + myRemainingTickTime;
        myLastTickStart = now;

        final long numberOfTicks = timeForTicks * ticksPerSecond / ONE_SECOND_IN_MS;
        myRemainingTickTime = timeForTicks - numberOfTicks * ONE_SECOND_IN_MS / ticksPerSecond;

        return Math.min( maxTicksPerFrame, (int) numberOfTicks );
        }

    final void notifyStartOfTick()
        {
        myTickCount++;
        }

    final void notifyStartOfFrame()
        {
        myLastFrameStart = nowInMs();
        myFrameCount++;
        myAveragingFrameCount++;
        }

    // Implementation

    private long nowInMs()
        {
        return System.currentTimeMillis();
        }

    private long accumulatedFrameTime()
        {
        return nowInMs() - myLastTimingStart;
        }


    private int myTickCount;

    private int myFrameCount;

    private int myAveragingFrameCount;

    private long myLastTickStart;

    private long myLastFrameStart;

    private long myLastTimingStart;

    private long myLastAveragingStart;

    private long myRemainingTickTime;

    private static final int ONE_SECOND_IN_MS = 1000;

    private static final int MIN_TIME_FOR_STATS_IN_MS = ONE_SECOND_IN_MS;

    private static final int MIN_TIME_FOR_AVERAGING_IN_MS = ONE_SECOND_IN_MS * 2;
    }
