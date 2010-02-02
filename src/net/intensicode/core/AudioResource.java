package net.intensicode.core;

public interface AudioResource
    {
    void setVolume( int aVolumeInPercent );

    void mute();

    void unmute();

    void play();

    void stop();

    void pause();

    void resume();
    }
