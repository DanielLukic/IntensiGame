package net.intensicode.hiscore;

import net.intensicode.core.*;
import net.intensicode.util.Log;
import org.json.me.*;

import java.io.UnsupportedEncodingException;

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
            try
                {
                final Score[] scores = extractScores( aBytes );
                aCallback.onScores( scores );
                }
            catch ( Throwable t )
                {
                onError( t );
                }
            }

        public final void onError( final Throwable aThrowable )
            {
            aCallback.onError( aThrowable );
            }
        } );
        }

    private Score[] extractScores( final byte[] aBytes ) throws UnsupportedEncodingException, JSONException
        {
        final String body = new String( aBytes, "UTF-8" );

        final JSONArray array = new JSONArray( body );
        final Score[] scores = new Score[array.length()];

        Log.info( "scores: {}", array.length() );
        for ( int idx = 0; idx < array.length(); idx++ )
            {
            // [{"CountryCode":"DE","DateTimeAchievedUTC":"2011-02-07 10:20:53Z","PlayerHash":"lrmwjowoncqp0lc3bbgo99vsl9w=","PlayerName":"TEST","Points":3000000,"Region":"Berlin","Tags":["UserAgent:JamJam Free\/2.1.24 (Android)","Level:22"]},{"CountryCode":"US","DateTimeAchievedUTC":"2011-02-07 10:18:07Z","PlayerHash":"lrmwjowoncqp0lc3bbgo99vsl9w=","PlayerName":"Toaster","Points":2000000,"Region":"Washington","Tags":["UserAgent:JamJam FREE","Level:22"]},{"CountryCode":"US","DateTimeAchievedUTC":"2011-02-07 10:10:12Z","PlayerHash":"lrmwjowoncqp0lc3bbgo99vsl9w=","PlayerName":"TheFrenchDJ","Points":1049100,"Region":"Washington","Tags":["UserAgent:JamJam FREE","Level:22"]},{"CountryCode":"DE","DateTimeAchievedUTC":"2011-02-07 11:03:32Z","PlayerHash":"lrmwjowoncqp0lc3bbgo99vsl9w=","PlayerName":"TFDJ","Points":50000,"Region":"Berlin","Tags":["UserAgent:JamJam Free\/2.1.24 (Android)","Level:5"]},{"CountryCode":"DE","DateTimeAchievedUTC":"2011-02-07 11:00:34Z","PlayerHash":"lrmwjowoncqp0lc3bbgo99vsl9w=","PlayerName":"Try","Points":10000,"Region":"Berlin","Tags":["UserAgent:JamJam FREE","Level:1"]}]
            final JSONObject object = array.getJSONObject( idx );
            final String name = object.getString( "PlayerName" );
            final int points = object.getInt( "Points" );
            final JSONArray tags = object.getJSONArray( "Tags" );
            final Score score = new Score( name, points, 0 );
            scores[ idx ] = score;
            }

        return scores;
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
        request.setSaltBase64( salt.toBase64Sha1() );

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
