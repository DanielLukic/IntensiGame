package net.intensicode.hiscore;

import net.intensicode.util.BASE64SHA1;

public final class GammetaSalt
    {
    public GammetaSalt( final Settings aSettings )
        {
        mySettings = aSettings;
        }

    public final void update( final Score aScore )
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( aScore.name );
        buffer.append( "\n" );
        buffer.append( mySettings.playerIdBase64 );
        buffer.append( "\n" );
        buffer.append( aScore.points );
        myBase64Sha1 = BASE64SHA1.encode( buffer.toString() );
        }

    public final String toBase64Sha1()
        {
        return myBase64Sha1;
        }

    private String myBase64Sha1;

    private final Settings mySettings;
    }
