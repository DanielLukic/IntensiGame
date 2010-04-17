package net.intensicode;

public interface ConfigurableFloatValue extends ConfigurableValue
    {
    float DEFAULT_STEP_SIZE = 0.1f;

    String getValueAsText( float aConfiguredValue );

    void setNewValue( float aConfiguredValue );

    float getCurrentValue();

    float getValueRange();

    float getStepSize();
    }
