package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class DebugScreen extends MultiScreen
    {
    public FontGenerator font;

    public boolean visible = false;

    public boolean autoVisible = false;

    public boolean clearBackground = false;

    public int clearColorARGB32 = 0x80FFFFFF;

    public boolean drawBorder = true;

    public int borderColorARGB32 = 0x80FFFF00;


    public static DebugInfo getDebugInfo()
        {
        if ( theInstanceOrNull == null ) return SHARED_DEBUG_INFO;
        return theInstanceOrNull.giveEmptyDebugInfo();
        }

    public static DebugScreen getOrCreateSharedInstance( final FontGenerator aFont )
        {
        if ( theInstanceOrNull == null ) theInstanceOrNull = new DebugScreen( aFont );
        return theInstanceOrNull;
        }

    public DebugScreen( final FontGenerator aFont )
        {
        font = aFont;
        }

    public DebugInfo giveEmptyDebugInfo()
        {
        //#if DEBUG_SCREEN
        if ( autoVisible ) visible = true;

        for ( int idx = 0; idx < myDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) myDebugInfos.get( idx );
            if ( info == null )
                {
                final DebugInfo newInfo = new DebugInfo();
                myDebugInfos.set( idx, newInfo );
                return newInfo;
                }
            if ( !info.active ) return info;
            }

        if ( myDebugInfos.size == MAX_DEBUG_INFOS )
            {
            //#if DEBUG
            Log.debug( "max debug info count reached: {}", MAX_DEBUG_INFOS );
            //#endif
            myDebugInfos.remove( Random.INSTANCE.nextInt( MAX_DEBUG_INFOS ) );
            }

        final DebugInfo newInfo = new DebugInfo();
        myDebugInfos.add( newInfo );
        return newInfo;
        //#else
        //# return SHARED_DEBUG_INFO;
        //#endif
        }

    // From ScreenBase

    public final void onInitOnce() throws Exception
        {
        //#if DEBUG_SCREEN
        addScreen( myBackground = new ClearScreen() );
        //#endif
        }

    public final void onInitEverytime() throws Exception
        {
        //#if DEBUG_SCREEN
        myDebugInfos.clear();
        //#endif
        }

    public final void onControlTick() throws Exception
        {
        //#if DEBUG_SCREEN
        if ( !visible )
            {
            myDebugInfos.clear();
            return;
            }

        setVisibility( myBackground, clearBackground );
        myBackground.clearColorARGB32 = clearColorARGB32;

        super.onControlTick();

        boolean atLeastOneActive = false;
        for ( int idx = 0; idx < myDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) myDebugInfos.get( idx );
            info.onControlTick();
            atLeastOneActive |= info.active;
            }

        if ( autoVisible && !atLeastOneActive ) visible = false;
        //#endif
        }

    public final void onDrawFrame()
        {
        //#if DEBUG_SCREEN
        if ( !visible )
            {
            myDebugInfos.clear();
            return;
            }

        super.onDrawFrame();

        for ( int idx = 0; idx < myDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) myDebugInfos.get( idx );
            info.onDrawFrame( graphics(), font );
            }

        if ( !drawBorder ) return;

        graphics().setColorARGB32( borderColorARGB32 );
        graphics().fillRect( 0, 0, screen().width(), font.charHeight() );
        graphics().fillRect( 0, screen().height() - font.charHeight(), screen().width(), font.charHeight() );
        graphics().fillRect( 0, 0, font.charHeight(), screen().height() );
        graphics().fillRect( screen().width() - font.charHeight(), 0, font.charHeight(), screen().height() );
        //#endif
        }


    private ClearScreen myBackground;

    private final DynamicArray myDebugInfos = new DynamicArray();

    private static DebugScreen theInstanceOrNull;

    private static final int MAX_DEBUG_INFOS = 256;

    private static final DebugInfo SHARED_DEBUG_INFO = new DebugInfo();
    }
