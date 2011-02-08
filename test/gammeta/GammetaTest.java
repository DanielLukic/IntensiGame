package gammeta;

import junit.framework.TestCase;
import net.intensicode.core.NetworkRequest;
import net.intensicode.hiscore.*;

import java.util.Calendar;
import java.util.TimeZone;

public final class GammetaTest extends TestCase
    {
    public final void testAuthorization()
        {
        final Settings settings = new Settings( "ZF/4P0GkD+eFkUzitngXzZkKsEs", "7e55e1187e55e118", "DK Classic/1.2 (PC)", "jTd5DRn7N/DkR2S+PHhH/CoflJc=", true );
        final GammetaAuthorization authorization = new GammetaAuthorization( settings );
        authorization.update( "sls.gamecloudservices.com", "Tue, 02 Nov 2010 03:11:11 GMT", "xM8UK1cHzymg4+yqF2/M301WqL4=" );
        assertEquals( "GGC JK9aiHmXZMmS1nAt6QpLvUIvsQs=", authorization.toHmacSha1() );
        }

    public final void testSalt()
        {
        final Settings settings = new Settings( "ZF/4P0GkD+eFkUzitngXzZkKsEs", "7e55e1187e55e118", "DK Classic/1.2 (PC)", "jTd5DRn7N/DkR2S+PHhH/CoflJc=", true );
        final GammetaScore score = new GammetaScore( "Steve W.", 1049100, 0 );
        final GammetaSalt salt = new GammetaSalt( settings );
        salt.update( score );
        assertEquals( "xM8UK1cHzymg4+yqF2/M301WqL4=", salt.toBase64Sha1() );
        }

    public final void testDate()
        {
        final Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
        calendar.set( Calendar.YEAR, 2010 );
        calendar.set( Calendar.MONTH, Calendar.NOVEMBER );
        calendar.set( Calendar.DAY_OF_MONTH, 2 );
        calendar.set( Calendar.HOUR_OF_DAY, 3 );
        calendar.set( Calendar.MINUTE, 11 );
        calendar.set( Calendar.SECOND, 11 );

        final GammetaDate date = new GammetaDate();
        assertEquals( "Tue, 02 Nov 2010 03:11:11 GMT", date.from( calendar ) );
        }

    public final void testRequest()
        {
        final Settings settings = new Settings( "ZF/4P0GkD+eFkUzitngXzZkKsEs", "7e55e1187e55e118", "DK Classic/1.2 (PC)", "jTd5DRn7N/DkR2S+PHhH/CoflJc=", true );
        final GammetaScore score = new GammetaScore( "Steve W.", 1049100, 22 );

        final GammetaRequest request = new GammetaRequest( settings, "sls.gamecloudservices.com", settings.gameId, NetworkRequest.METHOD_POST );
        request.addVariable( "PlayerName", score.name );
        request.addVariable( "PlayerHash", settings.playerIdBase64 );
        request.addVariable( "Points", new Integer( score.points ) );
        request.addVariable( "Latitude", new Double( 47.804854 ) );
        request.addVariable( "Longitude", new Double( -122.381516 ) );
        request.addTag( "DK" );
        request.addTag( "Arcade" );
        request.addTag( "Level:" + score.level );

        final GammetaSalt salt = new GammetaSalt( settings );
        salt.update( score );
        request.setSaltBase64( salt.toBase64Sha1() );

        request.setDate( "Tue, 02 Nov 2010 03:11:11 GMT" );

        assertEquals( "http://sls.gamecloudservices.com/7e55e1187e55e118", request.url() );

        assertEquals( "{\n" +
                      "\t\"PlayerName\": \"Steve W.\",\n" +
                      "\t\"PlayerHash\": \"jTd5DRn7N/DkR2S+PHhH/CoflJc=\",\n" +
                      "\t\"Points\": 1049100,\n" +
                      "\t\"Latitude\": 47.804854,\n" +
                      "\t\"Longitude\": -122.381516,\n" +
                      "\t\"Tags\": [\"DK\", \"Arcade\", \"Level:22\"]\n" +
                      "}", new String( request.body() ) );
        /*
        Host: sls.gamecloudservices.com
        User-Agent: DK Classic/1.2 (PC)
        Date: Tue, 02 Nov 2010 03:11:11 GMT
        Content-Type: application/json
        Content-Length: 186
        X-GGC-Version: 2010-09-01
        X-GGC-Mode: test
        X-GGC-Salt: xM8UK1cHzymg4+yqF2/M301WqL4=
        Authorization: GGC JK9aiHmXZMmS1nAt6QpLvUIvsQs=
         */
        assertEquals( "sls.gamecloudservices.com", request.headerValue( "Host" ) );
        assertEquals( "DK Classic/1.2 (PC)", request.headerValue( "User-Agent" ) );
        assertEquals( "Tue, 02 Nov 2010 03:11:11 GMT", request.headerValue( "Date" ) );
        assertEquals( "application/json", request.headerValue( "Content-Type" ) );
        assertEquals( "186", request.headerValue( "Content-Length" ) );
        assertEquals( "2010-09-01", request.headerValue( "X-GGC-Version" ) );
        assertEquals( "test", request.headerValue( "X-GGC-Mode" ) );
        assertEquals( "xM8UK1cHzymg4+yqF2/M301WqL4=", request.headerValue( "X-GGC-Salt" ) );
        assertEquals( "GGC JK9aiHmXZMmS1nAt6QpLvUIvsQs=", request.headerValue( "Authorization" ) );
        }
    }
