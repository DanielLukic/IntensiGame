package net.intensicode.hiscore;

public final class Settings
    {
    public final String key;

    public final String gameId;

    public final String userAgent;

    public final String playerIdBase64;

    public final boolean testMode;

    public Settings( final String aKey, final String aGameId, final String aUserAgent, final String aPlayerIdBase64, final boolean aTestMode )
        {
        key = aKey;
        gameId = aGameId;
        userAgent = aUserAgent;
        playerIdBase64 = aPlayerIdBase64;
        testMode = aTestMode;
        }
    }
