package net.intensicode.net.connection;

import net.intensicode.net.Request;

public interface CachingControl
    {
    boolean isDirtyData( final Request aRequest );

    boolean canBeCached( final Request aRequest );
    }
