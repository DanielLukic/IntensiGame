package net.intensicode.core;

import net.intensicode.util.*;

public abstract class AudioManager
    {
    public final boolean isMusicEnabled()
        {
        return myMusicEnabled;
        }

    public final boolean isSoundEnabled()
        {
        return mySoundEnabled;
        }

    public final void enableMusicAndSound()
        {
        myMusicEnabled = true;
        mySoundEnabled = true;
        enableResources( myMusicResources );
        enableResources( mySoundResources );
        }

    private void enableResources( final DynamicArray aResources )
        {
        for ( int idx = 0; idx < aResources.size; idx++ )
            {
            final AudioResourceEx resource = (AudioResourceEx) aResources.get( idx );
            resource.enable();
            }
        }

    public final void enableMusicOnly()
        {
        myMusicEnabled = true;
        mySoundEnabled = false;
        enableResources( myMusicResources );
        disableResources( mySoundResources );
        }

    private void disableResources( final DynamicArray aResources )
        {
        for ( int idx = 0; idx < aResources.size; idx++ )
            {
            final AudioResourceEx resource = (AudioResourceEx) aResources.get( idx );
            resource.disable();
            }
        }

    public final void enableSoundOnly()
        {
        myMusicEnabled = false;
        mySoundEnabled = true;
        disableResources( myMusicResources );
        enableResources( mySoundResources );
        }

    public final void disable()
        {
        myMusicEnabled = false;
        mySoundEnabled = false;
        disableResources( myMusicResources );
        disableResources( mySoundResources );
        }

    public void resumePlayback()
        {
        if ( myMusicEnabled ) resumeResources( myMusicResources );
        if ( mySoundEnabled ) resumeResources( mySoundResources );
        }

    private void resumeResources( final DynamicArray aResources )
        {
        for ( int idx = 0; idx < aResources.size; idx++ )
            {
            final AudioResourceEx resource = (AudioResourceEx) aResources.get( idx );
            resource.enable();
            }
        }

    public void haltPlayback()
        {
        haltResources( myMusicResources );
        haltResources( mySoundResources );
        }

    private void haltResources( final DynamicArray aResources )
        {
        for ( int idx = 0; idx < aResources.size; idx++ )
            {
            final AudioResourceEx resource = (AudioResourceEx) aResources.get( idx );
            resource.disable();
            }
        }

    public final void setMasterVolume( final int aVolumeInPercent )
        {
        myMusicVolume = aVolumeInPercent;
        mySoundVolume = aVolumeInPercent;
        setVolume( myMusicResources, aVolumeInPercent );
        setVolume( mySoundResources, aVolumeInPercent );
        }

    private void setVolume( final DynamicArray aResources, final int aVolumeInPercent )
        {
        for ( int idx = 0; idx < aResources.size; idx++ )
            {
            final AudioResource resource = (AudioResource) aResources.get( idx );
            resource.setVolume( aVolumeInPercent );
            }
        }

    public final void setMasterMusicVolume( final int aVolumeInPercent )
        {
        myMusicVolume = aVolumeInPercent;
        setVolume( myMusicResources, aVolumeInPercent );
        }

    public final void setMasterSoundVolume( final int aVolumeInPercent )
        {
        mySoundVolume = aVolumeInPercent;
        setVolume( mySoundResources, aVolumeInPercent );
        }

    public final AudioResource loadMusic( final String aMusicName )
        {
        try
            {
            //#if DEBUG
            Log.debug( "loading music resource {}", aMusicName );
            //#endif
            final AudioResourceEx resource = loadMusicUnsafe( aMusicName );
            if ( !myMusicEnabled ) resource.disable();
            resource.setVolume( myMusicVolume );
            resource.setLoopForever();
            registerMusicResource( resource );
            return resource;
            }
        catch ( final Exception e )
            {
            Log.error( "failed loading music resource {}: {}", aMusicName, e.toString(), null );
            return new SilentAudioResource();
            }
        }

    public final AudioResource loadSound( final String aSoundName )
        {
        try
            {
            //#if DEBUG
            Log.debug( "loading sound resource {}", aSoundName );
            //#endif
            final AudioResourceEx resource = loadSoundUnsafe( aSoundName );
            if ( !mySoundEnabled ) resource.disable();
            resource.setVolume( mySoundVolume );
            registerSoundResource( resource );
            return resource;
            }
        catch ( final Exception e )
            {
            Log.error( "failed loading sound resource {}: {}", aSoundName, e.toString(), null );
            return new SilentAudioResource();
            }
        }

    // Protected Interface

    protected AudioManager()
        {
        myMusicEnabled = mySoundEnabled = true;
        myMusicVolume = 50;
        mySoundVolume = 75;
        }

    protected abstract AudioResourceEx loadMusicUnsafe( final String aMusicName ) throws Exception;

    protected abstract AudioResourceEx loadSoundUnsafe( final String aSoundName ) throws Exception;

    // Implementation

    private void registerMusicResource( final AudioResourceEx aMusicResource )
        {
        myMusicResources.add( aMusicResource );
        }

    private void registerSoundResource( final AudioResourceEx aMusicResource )
        {
        mySoundResources.add( aMusicResource );
        }


    private int myMusicVolume;

    private int mySoundVolume;

    private boolean myMusicEnabled;

    private boolean mySoundEnabled;

    private final DynamicArray myMusicResources = new DynamicArray();

    private final DynamicArray mySoundResources = new DynamicArray();
    }
