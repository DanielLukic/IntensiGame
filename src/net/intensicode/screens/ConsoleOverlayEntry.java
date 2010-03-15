package net.intensicode.screens;

public final class ConsoleOverlayEntry
    {
    public boolean active;

    public int tickCounter;

    public final String message;


    public ConsoleOverlayEntry( final String aMessage, final int aTicksBeforeRemoval )
        {
        active = true;
        message = aMessage;
        tickCounter = aTicksBeforeRemoval;
        }

    public final void onControlTick()
        {
        if ( active && tickCounter > 0 ) tickCounter--;
        else active = false;
        }
    }
