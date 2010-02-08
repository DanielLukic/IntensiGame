package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public abstract class MenuBase extends MultiScreen implements TouchableHandler
    {
    public final DynamicArray entries = new DynamicArray();

    public final FontGenerator font;

    public int selectedEntryIndex;

    public int yOffset;

    public int ySpacingFixed = FixedMath.FIXED_1 * 2;


    public MenuBase( final FontGenerator aMenuFont )
        {
        font = aMenuFont;
        yOffset = aMenuFont.charHeight();
        }

    public final void setOffsetToVerticallyCentered()
        {
        yOffset = ( screen().height() - ( entries.size - 1 ) * FixedMath.toInt( font.charHeight() * ySpacingFixed ) ) / 2;
        }

    public final void updateEntryPositions()
        {
        for ( int idx = 0; idx < entries.size; idx++ )
            {
            final MenuEntry entry = getEntry( idx );
            entry.position.y = yOffset + FixedMath.toInt( idx * font.charHeight() * ySpacingFixed );
            //#if TOUCH_SUPPORTED
            entry.updateTouchable();
            //#endif
            }
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

    public final void onPop()
        {
        //#if TOUCH_SUPPORTED
        removeTouchableAreas();
        //#endif
        }

    public final void onInitEverytime() throws Exception
        {
        beforeInitEverytime();

        setOffsetToVerticallyCentered();
        updateEntryPositions();

        //#if TOUCH_SUPPORTED
        addTouchableAreas();
        //#endif

        afterInitEverytime();
        }

    public final void onControlTick() throws Exception
        {
        final KeysHandler keys = system().keys;
        if ( keys.checkUpAndConsume() )
            {
            updateSelectedEntry( selectedEntryIndex - 1 );
            }
        if ( keys.checkDownAndConsume() )
            {
            updateSelectedEntry( selectedEntryIndex + 1 );
            }
        if ( keys.checkLeftSoftAndConsume() || keys.checkStickDownAndConsume() || keys.checkFireAndConsume() )
            {
            onLeftSoftKey( getEntry( selectedEntryIndex ) );
            }
        if ( keys.checkRightSoftAndConsume() )
            {
            onRightSoftKey( getEntry( selectedEntryIndex ) );
            }

        super.onControlTick();
        }

    // Protected Interface

    protected abstract void onSelected( final MenuEntry aSelectedEntry ) throws Exception;

    protected void beforeInitEverytime() throws Exception
        {
        }

    protected void afterInitEverytime() throws Exception
        {
        }

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
        for ( int idx = 0; idx < entries.size; idx++ )
            {
            removeScreen( (ScreenBase) entries.get( idx ) );
            }
        entries.clear();
        }

    protected final MenuEntry addMenuEntry( final int aID, final String aText ) throws Exception
        {
        final int x = screen().width() / 2;
        final int y = yOffset + font.charHeight() * FixedMath.toInt( entries.size * ySpacingFixed );

        final MenuEntry newEntry = new MenuEntry( font, aText, new Position( x, y ) );
        newEntry.id = aID;
        //#if TOUCH_SUPPORTED
        newEntry.touchable.associatedHandler = this;
        //#endif
        addScreen( newEntry );
        entries.add( newEntry );
        return newEntry;
        }

    protected final void updateSelectedEntry()
        {
        updateSelectedEntry( selectedEntryIndex );
        }

    protected final void updateSelectedEntry( final int aSelectedEntry )
        {
        final int numberOfEntries = entries.size;
        selectedEntryIndex = ( aSelectedEntry + numberOfEntries ) % numberOfEntries;

        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final MenuEntry menuEntry = getEntry( idx );
            menuEntry.setSelected( idx == selectedEntryIndex );
            }
        }

    protected final MenuEntry getEntry( final int aIndex )
        {
        return (MenuEntry) entries.get( aIndex );
        }

    // Implementation

    //#if TOUCH_SUPPORTED

    private void addTouchableAreas()
        {
        final TouchHandler touch = touch();
        final int numberOfMenuEntries = entries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.addLocalControl( getEntry( idx ).touchable );
            }
        }

    private void removeTouchableAreas()
        {
        final TouchHandler touch = touch();
        final int numberOfMenuEntries = entries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.removeLocalControl( getEntry( idx ).touchable );
            }
        }

    //#endif
    }
