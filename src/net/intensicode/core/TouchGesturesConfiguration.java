//#condition TOUCH

package net.intensicode.core;

import net.intensicode.util.FixedMath;

public final class TouchGesturesConfiguration
    {
    public int breakTimeThresholdInMillis = 80;

    public int samePositionThresholdInPixels = 12;

    public int strokeThresholdInPixels = 6;

    public int directionIgnoreFactorFixed = FixedMath.FIXED_1 + FixedMath.FIXED_0_5 + FixedMath.FIXED_0_25 + FixedMath.FIXED_0_1;
    }
