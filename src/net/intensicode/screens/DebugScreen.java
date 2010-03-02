//#condition DEBUG

package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

// TODO: Move this into GameSystem#debug and avoid static(s)!

public final class DebugScreen extends MultiScreen
    {
    public static boolean visible = false;

    public static boolean autoVisible = false;

    public boolean clearBackground = false;

    public int clearColorARGB32 = 0x80FFFFFF;

    public boolean drawBorder = true;

    public int borderColorARGB32 = 0x80FFFF00;


    public DebugScreen( final FontGenerator aFont )
        {
        myFont = aFont;
        }

    public static DebugInfo giveEmptyDebugInfo()
        {
        if ( autoVisible ) visible = true;

        for ( int idx = 0; idx < theDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) theDebugInfos.get( idx );
            if ( info == null )
                {
                final DebugInfo newInfo = new DebugInfo();
                theDebugInfos.set( idx, newInfo );
                return newInfo;
                }
            if ( !info.active ) return info;
            }

        final DebugInfo newInfo = new DebugInfo();
        theDebugInfos.add( newInfo );
        return newInfo;
        }

    // From ScreenBase

    public final void onInitOnce() throws Exception
        {
        addScreen( myBackground = new ClearScreen() );
        }

    public final void onInitEverytime() throws Exception
        {
        }

    public final void onControlTick() throws Exception
        {
        if ( !visible ) return;

        setVisibility( myBackground, clearBackground );
        myBackground.clearColorARGB32 = clearColorARGB32;

        super.onControlTick();

        if ( theDebugInfos.size > 0 ) Log.debug( "ticking {} debug infos", theDebugInfos.size );

        boolean atLeastOneActive = false;
        for ( int idx = 0; idx < theDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) theDebugInfos.get( idx );
            info.onControlTick();
            atLeastOneActive |= info.active;
            }

        if ( autoVisible && !atLeastOneActive ) visible = false;
        }

    public final void onDrawFrame()
        {
        if ( !visible ) return;

        super.onDrawFrame();

        if ( theDebugInfos.size > 0 ) Log.debug( "drawing {} debug infos", theDebugInfos.size );

        for ( int idx = 0; idx < theDebugInfos.size; idx++ )
            {
            final DebugInfo info = (DebugInfo) theDebugInfos.get( idx );
            info.onDrawFrame( graphics(), myFont );
            }

        if ( !drawBorder ) return;

        graphics().setColorARGB32( borderColorARGB32 );
        graphics().fillRect( 0, 0, screen().width(), myFont.charHeight() );
        graphics().fillRect( 0, screen().height() - myFont.charHeight(), screen().width(), myFont.charHeight() );
        graphics().fillRect( 0, 0, myFont.charHeight(), screen().height() );
        graphics().fillRect( screen().width() - myFont.charHeight(), 0, myFont.charHeight(), screen().height() );
        }


    private ClearScreen myBackground;

    private final FontGenerator myFont;

    private static final DynamicArray theDebugInfos = new DynamicArray();
    }
