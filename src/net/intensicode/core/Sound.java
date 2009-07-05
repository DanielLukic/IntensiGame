package net.intensicode.core;

import net.intensicode.SystemConfiguration;
import net.intensicode.util.Log;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * TODO: Describe this!
 */
public final class Sound implements PlayerListener
    {
    public boolean disabledFlag = false;

    public boolean musicDisabled = false;

    public boolean soundDisabled = false;



    public final void setMute( final boolean aMutedFlag )
        {
        setMusicMute( aMutedFlag );
        setSoundMute( aMutedFlag );
        }

    public final void setMusicMute( final boolean aMutedFlag )
        {
        setMute( myMusicPlayers.elements(), aMutedFlag );
        }

    public final void setSoundMute( final boolean aMutedFlag )
        {
        setMute( mySoundPlayers.elements(), aMutedFlag );
        }

    public final void setMusicVolume( final int aVolumeInPercent )
        {
        //#if DEBUG
        if ( aVolumeInPercent < 0 || aVolumeInPercent > 100 ) throw new IllegalArgumentException();
        //#endif
        myMusicVolumeInPercent = Math.max( 0, Math.min( 100, aVolumeInPercent ) );
        setVolume( myMusicPlayers.elements(), myMusicVolumeInPercent );
        }

    public final void setSoundVolume( final int aVolumeInPercent )
        {
        //#if DEBUG
        if ( aVolumeInPercent < 0 || aVolumeInPercent > 100 ) throw new IllegalArgumentException();
        //#endif
        mySoundVolumeInPercent = Math.max( 0, Math.min( 100, aVolumeInPercent ) );
        setVolume( mySoundPlayers.elements(), mySoundVolumeInPercent );
        }

    public final Object loadMusic( final String aMusicName )
        {
        if ( disabledFlag || musicDisabled ) return aMusicName;

        if ( myMusicPlayers.containsKey( aMusicName ) == false )
            {
            //#if DEBUG
            Log.debug( "Loading music {} {}", new Object[]{ aMusicName, SystemConfiguration.MUSIC_FORMAT_MIME_TYPE } );
            //#endif

            final StringBuffer filename = new StringBuffer( "/music/" );
            filename.append( aMusicName );
            filename.append( SystemConfiguration.MUSIC_FORMAT_SUFFIX );

            try
                {
                final InputStream stream = myLoader.openChecked( filename.toString() );
                final String mimeType = SystemConfiguration.MUSIC_FORMAT_MIME_TYPE;
                final Player musicPlayer = createPlayer( stream, mimeType, myMusicVolumeInPercent );
                if ( musicPlayer != null ) myMusicPlayers.put( aMusicName, musicPlayer );
                }
            catch ( final Throwable aThrowable )
                {
                //#if DEBUG
                Log.error( "Failed loading music {}", aMusicName, aThrowable );
                //#endif
                return aMusicName;
                }
            }

        return aMusicName;
        }

    public final Object loadSound( final String aSoundName )
        {
        if ( disabledFlag || soundDisabled ) return aSoundName;

        if ( mySoundPlayers.containsKey( aSoundName ) == false )
            {
            //#if DEBUG
            Log.debug( "Loading sound {} {}", new Object[]{ aSoundName, SystemConfiguration.SOUND_FORMAT_MIME_TYPE } );
            //#endif

            final StringBuffer filename = new StringBuffer( "/sound/" );
            filename.append( aSoundName );
            filename.append( SystemConfiguration.SOUND_FORMAT_SUFFIX );

            try
                {
                final InputStream stream = myLoader.openChecked( filename.toString() );
                final String mimeType = SystemConfiguration.SOUND_FORMAT_MIME_TYPE;
                final Player soundPlayer = createPlayer( stream, mimeType, mySoundVolumeInPercent );
                if ( soundPlayer != null ) mySoundPlayers.put( aSoundName, soundPlayer );
                }
            catch ( final Throwable aThrowable )
                {
                //#if DEBUG
                Log.error( "Failed loading sound {}", aSoundName, aThrowable );
                //#endif
                return aSoundName;
                }
            }

        return aSoundName;
        }

    public final void playMusic( final Object aMusicHandle ) throws Exception
        {
        if ( disabledFlag || musicDisabled || myMusicPaused ) return;

        loadMusic( (String) aMusicHandle );

        if ( myCurrentlyPlayingMusic != null && myCurrentlyPlayingMusic != aMusicHandle )
            {
            stopMusic();
            }

        final Object musicPlayer = myMusicPlayers.get( aMusicHandle );
        startPlayer( musicPlayer, -1, myMusicVolumeInPercent );

        myCurrentlyPlayingMusic = (String) aMusicHandle;

        myMusicPaused = false;
        }

    public final void playSound( final Object aSoundHandle ) throws Exception
        {
        if ( disabledFlag || soundDisabled ) return;

        //#if !MULTI_SOUNDS
        if ( myCurrentlyPlayingSound != null ) return;
        //#endif

        loadSound( (String) aSoundHandle );

        final Object soundPlayer = mySoundPlayers.get( aSoundHandle );
        startPlayer( soundPlayer, 1, mySoundVolumeInPercent );

        myCurrentlyPlayingSound = (String) aSoundHandle;
        }

    public final void stopMusic() throws Exception
        {
        if ( myCurrentlyPlayingMusic == null ) return;

        stopPlayer( myMusicPlayers.get( myCurrentlyPlayingMusic ) );
        myCurrentlyPlayingMusic = null;
        }

    public final void pauseMusic() throws Exception
        {
        if ( myCurrentlyPlayingMusic == null ) return;

        stopPlayer( myMusicPlayers.get( myCurrentlyPlayingMusic ) );
        myMusicPaused = true;
        }

    public final void resumeMusic() throws Exception
        {
        if ( myCurrentlyPlayingMusic == null ) return;

        resumePlayer( myMusicPlayers.get( myCurrentlyPlayingMusic ) );
        myMusicPaused = false;
        }

    public final void stopSound( final Object aSoundHandle ) throws Exception
        {
        if ( aSoundHandle == null ) return;

        stopPlayer( mySoundPlayers.get( aSoundHandle ) );
        }

    // From PlayerListener

    public final void playerUpdate( final Player aPlayer, final String aString, final Object aObject )
        {
        if ( aString == PlayerListener.END_OF_MEDIA || aString == PlayerListener.STOPPED )
            {
            onSoundPlayed( aPlayer );
            }
        }

    // Package Interface

    Sound( final Engine aEngine )
        {
        myEngine = aEngine;
        myLoader = aEngine.loader;
        }

    final void start() throws Exception
        {
        if ( myCurrentlyPlayingMusic != null ) playMusic( myCurrentlyPlayingMusic );
        }

    final void stop()
        {
        try
            {
            stopAndPurge( myMusicPlayers, false );
            stopAndPurge( mySoundPlayers, false );
            }
        catch ( final MediaException e )
            {
            //#if DEBUG
            Engine.showError( "Failed stopping sound system", e );
            //#endif
            }
        }

    final void onControlTick() throws Exception
        {
        try
            {
            if ( disabledFlag || musicDisabled ) stopAndPurge( myMusicPlayers, true );
            if ( disabledFlag || soundDisabled ) stopAndPurge( mySoundPlayers, true );
            if ( disabledFlag ) return;

            if ( myCurrentlyPlayingMusic != null ) handlePlayingMusic();
            if ( myCurrentlyPlayingSound != null ) handlePlayingSound();
            }
        catch ( final Throwable aThrowable )
            {
            //#if DEBUG
            Log.error( "Sound#onControlTick failed", aThrowable );
            //#endif
            }
        }

    // Implementation

    private final void onSoundPlayed( final Player aPlayer )
        {
        if ( mySoundPlayers.get( myCurrentlyPlayingSound ) == aPlayer )
            {
            myCurrentlyPlayingSound = null;
            }
        //#if DEBUG
        else
            {
            final String name = findPlayerName( aPlayer );
            Log.debug( "Unexpected sound stopped playing: {}", name );
            }
        //#endif
        }

    private final void handlePlayingMusic() throws Exception
        {
        if ( myCurrentlyPlayingMusic == null ) return;
        if ( myEngine == null || myEngine.screen == null ) return;
        if ( musicDisabled || myEngine.screen.visible == false ) return;

        if ( myMusicPlayers.containsKey( myCurrentlyPlayingMusic ) == false )
            {
            loadMusic( myCurrentlyPlayingMusic );
            }

        final Player player = (Player) myMusicPlayers.get( myCurrentlyPlayingMusic );
        if ( player != null && player.getState() != Player.STARTED )
            {
            playMusic( myCurrentlyPlayingMusic );
            }
        }

    private final void handlePlayingSound() throws Exception
        {
        if ( myCurrentlyPlayingSound == null ) return;
        if ( myEngine == null || myEngine.screen == null ) return;
        if ( soundDisabled || myEngine.screen.visible == false ) return;

        if ( mySoundPlayers.containsKey( myCurrentlyPlayingSound ) == false )
            {
            myCurrentlyPlayingSound = null;
            }
        else
            {
            final Player player = (Player) mySoundPlayers.get( myCurrentlyPlayingSound );
            if ( player != null && player.getState() != Player.STARTED )
                {
                onSoundPlayed( player );
                }
            }
        }

    private final Player createPlayer( final InputStream aStream, final String aMimeType, final int aInitialVolume ) throws IOException, MediaException
        {
        if ( disabledFlag ) return null;

        final Player player = Manager.createPlayer( aStream, aMimeType );
        player.addPlayerListener( this );
        player.prefetch();

        final VolumeControl vc = (VolumeControl) player.getControl( "VolumeControl" );
        if ( vc != null )
            {
            vc.setLevel( aInitialVolume );
            myVolumeControls.put( player, vc );
            }

        return player;
        }

    private final void startPlayer( final Object aPlayerHandle, final int aLoopCount, final int aVolumeInPercent ) throws MediaException
        {
        if ( aPlayerHandle == null ) return;

        stopPlayer( aPlayerHandle );
        resetPlayer( aPlayerHandle, aLoopCount, aVolumeInPercent );
        resumePlayer( aPlayerHandle );
        }

    private final void stopPlayer( final Object aPlayerHandle ) throws MediaException
        {
        if ( aPlayerHandle == null ) return;

        final Player player = (Player) aPlayerHandle;
        player.stop();
        }

    private final void resetPlayer( final Object aPlayerHandle, final int aLoopCount, final int aVolumeInPercent ) throws MediaException
        {
        if ( aPlayerHandle == null ) return;

        final Player player = (Player) aPlayerHandle;
        player.setMediaTime( 0 );
        player.setLoopCount( aLoopCount );

        final VolumeControl volumeControl = getVolumeControlFor( player );
        if ( volumeControl != null ) volumeControl.setLevel( aVolumeInPercent );
        }

    private final void resumePlayer( final Object aPlayerHandle ) throws MediaException
        {
        if ( aPlayerHandle == null ) return;

        final Player player = (Player) aPlayerHandle;
        player.start();
        }

    private final void setMute( final Enumeration aPlayersList, final boolean aMutedFlag )
        {
        while ( aPlayersList.hasMoreElements() )
            {
            final VolumeControl control = getVolumeControlFor( aPlayersList.nextElement() );
            if ( control != null ) control.setMute( aMutedFlag );
            }
        }

    private final void setVolume( final Enumeration aPlayersList, final int aVolumeInPercent )
        {
        while ( aPlayersList.hasMoreElements() )
            {
            final VolumeControl control = getVolumeControlFor( aPlayersList.nextElement() );
            if ( control != null ) control.setLevel( aVolumeInPercent );
            }
        }

    private final VolumeControl getVolumeControlFor( final Object aPlayer )
        {
        return (VolumeControl) myVolumeControls.get( aPlayer );
        }

    private final void stopAndPurge( final Hashtable aPlayers, final boolean aPurgeFlag ) throws MediaException
        {
        final Enumeration players = aPlayers.elements();
        while ( players.hasMoreElements() )
            {
            stopPlayer( players.nextElement() );
            }
        if ( aPurgeFlag ) aPlayers.clear();
        }

    private final String findPlayerName( final Player aPlayer )
        {
        final String name = findPlayerName( mySoundPlayers, aPlayer );
        return name != null ? name : findPlayerName( myMusicPlayers, aPlayer );
        }

    private final String findPlayerName( final Hashtable aPlayers, final Player aPlayer )
        {
        final Enumeration players = aPlayers.keys();
        while ( players.hasMoreElements() )
            {
            final String name = (String) players.nextElement();
            if ( aPlayers.get( name ) == aPlayer ) return name;
            }
        return null;
        }



    private boolean myMusicPaused;

    private int myMusicVolumeInPercent = 75;

    private int mySoundVolumeInPercent = 100;

    private String myCurrentlyPlayingMusic;

    private String myCurrentlyPlayingSound;


    private final Engine myEngine;

    private final ResourceLoader myLoader;

    private final Hashtable myMusicPlayers = new Hashtable();

    private final Hashtable mySoundPlayers = new Hashtable();

    private final Hashtable myVolumeControls = new Hashtable();
    }
