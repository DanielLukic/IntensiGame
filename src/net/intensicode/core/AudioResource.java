package net.intensicode.core;

public interface AudioResource
    {
    void setLoopForever();

    void setVolume( int aVolumeInPercent );

    void mute();

    void unmute();

    void play();

    void stop();

    void pause();

    void resume();
    }
