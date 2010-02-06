package net.intensicode.core;

public interface AudioResource
    {
    void setLoopForever();

    void setVolume( int aVolumeInPercent );

    void mute();

    void unmute();

    void start();

    void stop();

    void resume();

    void pause();
    }
