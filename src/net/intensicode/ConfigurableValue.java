package net.intensicode;

public interface ConfigurableValue
    {
    int DEFAULT_STEP_SIZE = 1;

    String getTitle();

    String getInfoText();

    String getValueAsText( int aConfiguredValue );

    void setNewValue( int aConfiguredValue );

    int getMaxValue();

    int getCurrentValue();

    int getStepSize();
    }
