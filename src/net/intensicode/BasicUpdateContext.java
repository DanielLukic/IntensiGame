package net.intensicode;

import net.intensicode.core.GameSystem;
import net.intensicode.util.Log;
import org.json.me.JSONObject;

public class BasicUpdateContext implements UpdateContext
    {
    public BasicUpdateContext( final JSONObject aUpdateData )
        {
        myUpdateData = aUpdateData;
        }

    public void triggerUpdate(final GameSystem aGameSystem)
        {
        Log.info( "myUpdateData: {}", myUpdateData );
        Log.info( "myGameSystem: {}", aGameSystem );
        Log.info( "platform: {}", aGameSystem.platform );
        final String infoOrNull = myUpdateData.optString( "url" );
        if ( infoOrNull != null ) aGameSystem.platform.openWebBrowser( infoOrNull );
        }

    private final JSONObject myUpdateData;
    }
