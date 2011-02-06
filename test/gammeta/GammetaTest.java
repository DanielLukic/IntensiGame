package gammeta;

import junit.framework.TestCase;
import net.intensicode.hiscore.*;

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
        final Score score = new Score( "Steve W.", 1049100, 0 );
        final GammetaSalt salt = new GammetaSalt( settings );
        salt.update( score );
        assertEquals( "xM8UK1cHzymg4+yqF2/M301WqL4=", salt.toBase64Sha1() );
        }
    }
