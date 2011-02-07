package net.intensicode.hiscore;

import net.intensicode.core.*;

public final class SimpleLeaderboardService
    {
    public SimpleLeaderboardService( final Settings aSettings, final NetworkIO aNetworkIO )
        {
        mySettings = aSettings;
        myNetworkIO = aNetworkIO;
        }

    public final void retrieveScores( final SimpleLeaderboardCallback aCallback )
        {
        final GammetaRequest request = new GammetaRequest( mySettings, SERVICE_HOST, mySettings.gameId + "/world", NetworkRequest.METHOD_GET );
        request.addQuery( "style", "leaderboard" );
        myNetworkIO.process( request, new NetworkCallback()
        {
        public final void onReceived( final byte[] aBytes )
            {
            aCallback.onScores( aBytes );
            }

        public final void onError( final Throwable aThrowable )
            {
            aCallback.onError( aThrowable );
            }
        } );
        }

    public final void submitScore( final Score aScore, final GammetaCallback aCallback )
        {
        final GammetaRequest request = new GammetaRequest( mySettings, SERVICE_HOST, mySettings.gameId, NetworkRequest.METHOD_POST );
        request.addVariable( "PlayerHash", mySettings.playerIdBase64 );
        request.addVariable( "PlayerName", aScore.name );
        request.addVariable( "Points", new Integer( aScore.points ) );
        request.addTag( "Level:" + aScore.level );

        final GammetaSalt salt = new GammetaSalt( mySettings );
        salt.update( aScore );
        request.setSaltBase64(salt.toBase64Sha1());

        myNetworkIO.process( request, new NetworkCallback()
        {
        public final void onReceived( final byte[] aBytes )
            {
            aCallback.onSuccess();
            }

        public final void onError( final Throwable aThrowable )
            {
            aCallback.onError( aThrowable );
            }
        } );
        }

    private final Settings mySettings;

    private final NetworkIO myNetworkIO;

    private static final String SERVICE_HOST = "sls.gamecloudservices.com";
    }
