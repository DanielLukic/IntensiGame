package net.intensicode.configuration;

import net.intensicode.ConfigurableActionValue;
import net.intensicode.core.ScreenStack;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.screens.TouchTestingScreen;
import net.intensicode.util.Log;

public final class TouchTesting implements ConfigurableActionValue
    {
    public TouchTesting( final ScreenStack aScreenStack, final FontGenerator aFontGenerator )
        {
        myScreenStack = aScreenStack;
        myFontGenerator = aFontGenerator;
        }

    // From ConfigurableValue

    public final String getTitle()
        {
        return "Touch testing";
        }

    public final String getInfoText()
        {
        return "Show touch testing screen.";
        }

    public final void trigger()
        {
        try
            {
            final TouchTestingScreen screen = new TouchTestingScreen( myFontGenerator );
            myScreenStack.pushOnce( screen );
            }
        catch ( final Exception e )
            {
            Log.error( "failed showing touch testing screen", e );
            }
        }

    private final ScreenStack myScreenStack;

    private final FontGenerator myFontGenerator;
    }
