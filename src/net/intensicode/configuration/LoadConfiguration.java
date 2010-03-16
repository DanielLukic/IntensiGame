package net.intensicode.configuration;

import net.intensicode.*;

public final class LoadConfiguration implements ConfigurableActionValue
    {
    public LoadConfiguration( final SystemContext aSystemContext )
        {
        mySystemContext = aSystemContext;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Load configuration";
        }

    public final String getInfoText()
        {
        return "Load all configuration settings.";
        }

    public final void trigger()
        {
        mySystemContext.loadConfigurableValues();
        }


    private final SystemContext mySystemContext;
    }
