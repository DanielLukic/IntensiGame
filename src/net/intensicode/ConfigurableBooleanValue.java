package net.intensicode;

public interface ConfigurableBooleanValue extends ConfigurableValue
    {
    String getTitle();

    String getInfoText();

    String getValueAsText( boolean aConfiguredValue );

    void setNewValue( boolean aConfiguredValue );

    boolean getCurrentValue();
    }
