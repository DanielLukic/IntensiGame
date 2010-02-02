package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public abstract class MenuBase extends MultiScreen implements TouchableHandler
    {
    public final DynamicArray menuEntries = new DynamicArray( 5, 5 );

    public final FontGenerator menuFontGen;

    public int selectedEntry;


    public MenuBase( final FontGenerator aMenuFont )
        {
        menuFontGen = aMenuFont;
        }

    //#ifdef TOUCH_SUPPORTED

    // From TouchableHandler

    public void onPressed( final Object aTouchable )
        {
        try
            {
            final MenuEntry selected = (MenuEntry) aTouchable;
            onSelected( selected );
            }
        catch ( final Exception e )
            {
            GameSystem.showException( e );
            }
        }

    public void onReleased( final Object aTouchable )
        {
        }

    //#endif

    // From ScreenBase

    public final void onTop()
        {
        //#if TOUCH_SUPPORTED
        addTouchableAreas();
        //#endif
        }

    public void onPop()
        {
        //#if TOUCH_SUPPORTED
        removeTouchableAreas();
        //#endif
        }

    public void onInitEverytime() throws Exception
        {
        //#if TOUCH_SUPPORTED
        addTouchableAreas();
        //#endif
        }

    public final void onControlTick() throws Exception
        {
        final KeysHandler keys = system().keys;
        if ( keys.checkUpAndConsume() )
            {
            updateSelectedEntry( selectedEntry - 1 );
            }
        if ( keys.checkDownAndConsume() )
            {
            updateSelectedEntry( selectedEntry + 1 );
            }
        if ( keys.checkLeftSoftAndConsume() || keys.checkStickDownAndConsume() || keys.checkFireAndConsume() )
            {
            onLeftSoftKey( getEntry( selectedEntry ) );
            }
        if ( keys.checkRightSoftAndConsume() )
            {
            onRightSoftKey( getEntry( selectedEntry ) );
            }

        super.onControlTick();
        }

    // Protected Interface

    protected abstract void onSelected( final MenuEntry aSelectedEntry ) throws Exception;

    protected void onLeftSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        onSelected( aSelectedEntry );
        }

    protected void onRightSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        // This is the default behaviour :)
        system().shutdownAndExit();
        }

    protected final void removeAllEntries()
        {
        for ( int idx = 0; idx < menuEntries.size; idx++ )
            {
            removeScreen( (ScreenBase) menuEntries.get( idx ) );
            }
        menuEntries.clear();
        }

    protected final MenuEntry addMenuEntry( final int aID, final String aText ) throws Exception
        {
        final int x = screen().width() / 2;
        final int y = ( menuEntries.size + 2 ) * ( menuFontGen.charHeight() * 3 / 2 );

        final MenuEntry newEntry = new MenuEntry( menuFontGen, aText, new Position( x, y ) );
        newEntry.id = aID;
        //#if TOUCH_SUPPORTED
        newEntry.touchable.associatedHandler = this;
        //#endif
        addScreen( newEntry );
        menuEntries.add( newEntry );
        return newEntry;
        }

    protected final void updateSelectedEntry()
        {
        updateSelectedEntry( selectedEntry );
        }

    protected void updateSelectedEntry( final int aSelectedEntry )
        {
        final int numberOfEntries = menuEntries.size;
        selectedEntry = ( aSelectedEntry + numberOfEntries ) % numberOfEntries;

        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final MenuEntry menuEntry = getEntry( idx );
            menuEntry.setSelected( idx == selectedEntry );
            }
        }

    // Implementation

    //#if TOUCH_SUPPORTED

    private void addTouchableAreas()
        {
        final TouchHandler touch = touch();
        final int numberOfMenuEntries = menuEntries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.addLocalControl( getEntry( idx ).touchable );
            }
        }

    private void removeTouchableAreas()
        {
        final TouchHandler touch = touch();
        final int numberOfMenuEntries = menuEntries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.removeLocalControl( getEntry( idx ).touchable );
            }
        }

    //#endif

    private MenuEntry getEntry( final int aIndex )
        {
        return (MenuEntry) menuEntries.get( aIndex );
        }
    }
