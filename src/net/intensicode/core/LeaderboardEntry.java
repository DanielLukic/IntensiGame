package net.intensicode.core;

public final class LeaderboardEntry
    {
    public final int rank;

    public final long score;

    public final int level;

    public final String name;

    public LeaderboardEntry( final int aRank, final long aScore, final int aLevel, final String aName )
        {
        rank = aRank;
        score = aScore;
        level = aLevel;
        name = aName;
        }
    }
