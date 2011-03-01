package net.intensicode.configuration;

import net.intensicode.*;
import net.intensicode.core.GameSystem;

public final class DeleteConfiguration implements ConfigurableActionValue
    {
    public DeleteConfiguration( final SystemContext aSystemContext, final GameSystem aGameSystem )
        {
        mySystemContext = aSystemContext;
        myGameSystem = aGameSystem;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Delete configuration";
        }

    public final String getInfoText()
        {
        return "Delete all configuration settings. Note that you have to exit and restart the application " +
               "to make the changes take effect.";
        }

    public final void trigger()
        {
        final IntensiGameHelper helper = new IntensiGameHelper( myGameSystem );
        helper.deleteConfiguration( mySystemContext.getPlatformValues() );
        helper.deleteConfiguration( mySystemContext.getSystemValues() );
        helper.deleteConfiguration( mySystemContext.getApplicationValues() );
        }


    private final GameSystem myGameSystem;

    private final SystemContext mySystemContext;
    }
