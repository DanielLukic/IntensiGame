package net.intensicode.configuration;

import net.intensicode.*;

public final class DeleteConfiguration implements ConfigurableActionValue
    {
    public DeleteConfiguration( final SystemContext aSystemContext )
        {
        mySystemContext = aSystemContext;
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
        final IntensiGameHelper helper = new IntensiGameHelper( mySystemContext.system() );
        helper.deleteConfiguration( mySystemContext.getPlatformValues() );
        helper.deleteConfiguration( mySystemContext.getSystemValues() );
        helper.deleteConfiguration( mySystemContext.getApplicationValues() );
        }


    private final SystemContext mySystemContext;
    }
