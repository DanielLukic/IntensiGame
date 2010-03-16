package net.intensicode.configuration;

import net.intensicode.*;

public final class SaveConfiguration implements ConfigurableActionValue
    {
    public SaveConfiguration( final SystemContext aSystemContext )
        {
        mySystemContext = aSystemContext;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Save configuration";
        }

    public final String getInfoText()
        {
        return "Save all configuration settings.";
        }

    public final void trigger()
        {
        mySystemContext.saveConfigurableValues();
        }


    private final SystemContext mySystemContext;
    }
