package net.intensicode.screens;

public final class ClearScreen extends ScreenBase
    {
    public int clearColorARGB32;

    public ClearScreen()
        {
        this( 0x000000 );
        }

    public ClearScreen( final int aARGB32 )
        {
        clearColorARGB32 = aARGB32;
        }

    // From ScreenBase

    public final void onControlTick()
        {
        }

    public final void onDrawFrame()
        {
        graphics().clearARGB32( clearColorARGB32 );
        }
    }
