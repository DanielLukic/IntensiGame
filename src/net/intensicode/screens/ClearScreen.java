package net.intensicode.screens;

public final class ClearScreen extends ScreenBase
    {
    public ClearScreen()
        {
        this( 0x000000 );
        }

    public ClearScreen( final int aRGB24 )
        {
        myRGB24 = aRGB24;
        }

    // From ScreenBase

    public final void onControlTick()
        {
        }

    public final void onDrawFrame()
        {
        graphics().clearRGB24( myRGB24 );
        }

    private final int myRGB24;
    }
