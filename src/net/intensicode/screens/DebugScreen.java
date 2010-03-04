//#condition DEBUG

package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class DebugScreen extends MultiScreen
    {
    public boolean visible = false;

    public boolean autoVisible = false;

    public boolean clearBackground = false;

    public int clearColorARGB32 = 0x80FFFFFF;

    public boolean drawBorder = true;

    public int borderColorARGB32 = 0x80FFFF00;


    public DebugScreen( final FontGenerator aFont )
        {
        myFont = aFont;
        }

    public DebugInfo giveEmptyDebugInfo()
        {
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
        }

    public final void changeFont( final FontGenerator aFontGenerator )
        {
        myFont = aFontGenerator;
        }

    // From ScreenBase

    public final void onInitOnce() throws Exception
        {
        addScreen( myBackground = new ClearScreen() );
        }

    public final void onInitEverytime() throws Exception
        {
        myDebugInfos.clear();
        }

    public final void onControlTick() throws Exception
        {
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
        }

    public final void onDrawFrame()
        {
        if ( !visible )
            {
            myDebugInfos.clear();
            return;
            }

        super.onDrawFrame();

        for ( int idx = 0; idx < myDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) myDebugInfos.get( idx );
            info.onDrawFrame( graphics(), myFont );
            }

        if ( !drawBorder ) return;

        graphics().setColorARGB32( borderColorARGB32 );
        graphics().fillRect( 0, 0, screen().width(), myFont.charHeight() );
        graphics().fillRect( 0, screen().height() - myFont.charHeight(), screen().width(), myFont.charHeight() );
        graphics().fillRect( 0, 0, myFont.charHeight(), screen().height() );
        graphics().fillRect( screen().width() - myFont.charHeight(), 0, myFont.charHeight(), screen().height() );
        }


    private FontGenerator myFont;

    private ClearScreen myBackground;

    private final DynamicArray myDebugInfos = new DynamicArray();

    private static final int MAX_DEBUG_INFOS = 256;
    }
