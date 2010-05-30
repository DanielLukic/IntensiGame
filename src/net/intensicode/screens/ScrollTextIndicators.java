package net.intensicode.screens;

import net.intensicode.core.KeysHandler;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class ScrollTextIndicators extends ScreenBase
    {
    public static final String UP_INDICATOR_LABEL = "[UP]";

    public static final String DOWN_INDICATOR_LABEL = "[DOWN]";

    public final Position upPosition = new Position();

    public final Position downPosition = new Position();

    public boolean showUpIndicator;

    public boolean showDownIndicator;


    public ScrollTextIndicators( final FontGenerator aFontGenerator )
        {
        Assert.notNull( "valid font", aFontGenerator );
        myFont = aFontGenerator;
        }

    public final void changeFont( final FontGenerator aFontGenerator )
        {
        Assert.notNull( "valid font", aFontGenerator );
        myFont = aFontGenerator;
        }

    // From ScreenBase

    public final void onControlTick() throws Exception
        {
        //#if TOUCH
        tickTouchableIndicators();
        //#endif
        }

    public final void onDrawFrame()
        {
        if ( showUpIndicator )
            {
            myFont.blitString( graphics(), UP_INDICATOR_LABEL, upPosition, FontGenerator.CENTER );
            }

        if ( showDownIndicator )
            {
            myFont.blitString( graphics(), DOWN_INDICATOR_LABEL, downPosition, FontGenerator.CENTER );
            }
        }

    //#if TOUCH

    private void tickTouchableIndicators()
        {
        final int charHeight = myFont.charHeight();

        if ( showUpIndicator )
            {
            if ( myUpIndicator == null )
                {
                myUpIndicator = new net.intensicode.touch.TouchableArea();
                myUpIndicator.associatedKeyID = KeysHandler.UP;
                myUpIndicator.rectangle.setCenterAndSize( upPosition, myFont.stringWidth( UP_INDICATOR_LABEL ), charHeight );
                }
            touch().addLocalControl( myUpIndicator );
            }

        if ( showDownIndicator )
            {
            if ( myDownIndicator == null )
                {
                myDownIndicator = new net.intensicode.touch.TouchableArea();
                myDownIndicator.associatedKeyID = KeysHandler.DOWN;
                myDownIndicator.rectangle.setCenterAndSize( downPosition, myFont.stringWidth( DOWN_INDICATOR_LABEL ), charHeight );
                }
            touch().addLocalControl( myDownIndicator );
            }
        }

    private net.intensicode.touch.TouchableArea myUpIndicator;

    private net.intensicode.touch.TouchableArea myDownIndicator;

    //#endif

    private FontGenerator myFont;
    }
