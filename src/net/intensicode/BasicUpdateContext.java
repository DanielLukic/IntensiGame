package net.intensicode;

import net.intensicode.core.GameSystem;
import org.json.me.JSONObject;

public class BasicUpdateContext implements UpdateContext
    {
    public BasicUpdateContext( final GameSystem aGameSystem, final JSONObject aUpdateData )
        {
        myGameSystem = aGameSystem;
        myUpdateData = aUpdateData;
        }

    public void triggerUpdate()
        {
        final String infoOrNull = myUpdateData.optString( "url" );
        if ( infoOrNull != null ) myGameSystem.platform.openWebBrowser( infoOrNull );
        }

    private final GameSystem myGameSystem;

    private final JSONObject myUpdateData;
    }
