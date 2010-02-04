package net.intensicode.core;

import java.io.IOException;

public interface AudioManager
    {
    String ERROR_NO_SOUND_DEVICE_AVAILABLE = "no sound device available";

    boolean supportsMusicPlusSound();

    boolean supportsMultiSound();

    int numberOfSoundChannels();

    void enableMusicAndSound();

    void enableMusicOnly();

    void enableSoundOnly();

    void disable();

    void setMasterMute( boolean aMutedFlag );

    void setMasterVolume( int aVolumeInPercent );

    void setMasterMusicMute( boolean aMutedFlag );

    void setMasterSoundMute( boolean aMutedFlag );

    void setMasterMusicVolume( int aVolumeInPercent );

    void setMasterSoundVolume( int aVolumeInPercent );

    MusicResource loadMusic( String aMusicName ) throws IOException;

    SoundResource loadSound( String aSoundName ) throws IOException;
    }
