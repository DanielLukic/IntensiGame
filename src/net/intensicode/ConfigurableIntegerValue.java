package net.intensicode;

public interface ConfigurableIntegerValue extends ConfigurableValue
    {
    int DEFAULT_STEP_SIZE = 1;

    String getValueAsText( int aConfiguredValue );

    void setNewValue( int aConfiguredValue );

    int getMaxValue();

    int getCurrentValue();

    int getStepSize();
    }
