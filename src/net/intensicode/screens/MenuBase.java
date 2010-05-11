package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public abstract class MenuBase extends MultiScreen
        //#if TOUCH
        implements net.intensicode.touch.TouchableHandler
        //#endif
    {
    public final DynamicArray entries = new DynamicArray();

    public final FontGenerator font;

    public int selectedEntryIndex;

    public int yOffset;

    public float ySpacingFixed = 2f;


    public MenuBase( final FontGenerator aMenuFont )
        {
        font = aMenuFont;
        yOffset = aMenuFont.charHeight();
        }

    public final void setOffsetToVerticallyCentered()
        {
        yOffset = (int) ( ( screen().height() - ( entries.size - 1 ) * ( font.charHeight() * ySpacingFixed ) ) / 2 );
        }

    public final void updateEntryPositions()
        {
        for ( int idx = 0; idx < entries.size; idx++ )
            {
            final MenuEntry entry = getEntry( idx );
            entry.position.x = screen().width() / 2;
            entry.position.y = (int) ( yOffset + idx * font.charHeight() * ySpacingFixed );
            //#if TOUCH
            entry.updateTouchable();
            //#endif
            }
        }

    //#ifdef TOUCH

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

    public final void onInitEverytime() throws Exception
        {
        beforeInitEverytime();

        setOffsetToVerticallyCentered();
        updateEntryPositions();

        afterInitEverytime();
        }

    public void onOrientationChanged() throws Exception
        {
        super.onOrientationChanged();
        setOffsetToVerticallyCentered();
        updateEntryPositions();
        }

    public final void onControlTick() throws Exception
        {
        beforeControlTick();

        //#if TOUCH
        addTouchableAreas();
        //#endif

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

        afterControlTick();
        }

    // Protected Interface

    protected abstract void onSelected( final MenuEntry aSelectedEntry ) throws Exception;

    protected void beforeInitEverytime() throws Exception
        {
        }

    protected void afterInitEverytime() throws Exception
        {
        }

    protected void beforeControlTick()
        {
        }

    protected void afterControlTick() throws Exception
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
        final int y = (int) ( yOffset + font.charHeight() * entries.size * ySpacingFixed );

        final MenuEntry newEntry = new MenuEntry( font, aText, new Position( x, y ) );
        newEntry.id = aID;
        //#if TOUCH
        newEntry.touchable.associatedHandler = this;
        //#endif
        addScreen( newEntry );
        entries.add( newEntry );

        updateSelectedEntry();

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

    //#if TOUCH

    private void addTouchableAreas()
        {
        final net.intensicode.touch.TouchHandler touch = touch();
        final int numberOfMenuEntries = entries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.addLocalControl( getEntry( idx ).touchable );
            }
        }

    //#endif
    }
