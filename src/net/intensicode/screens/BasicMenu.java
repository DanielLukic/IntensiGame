package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public class BasicMenu extends MultiScreen implements TouchableHandler
    {
    public static final int ENTRY_POSITIONS_CENTERED = 0;

    public static final int ENTRY_POSITIONS_TOP_ALIGNED = 1;

    public static final int ENTRY_POSITIONS_MANUAL = 2;

    public int entryPositionsMode = ENTRY_POSITIONS_CENTERED;

    public int lineSpacingFixed = FixedMath.FIXED_1 * 2;


    public BasicMenu( final MenuHandler aMenuHandler, final FontGenerator aMenuFont )
        {
        myMenuHandler = aMenuHandler;
        myFont = aMenuFont;
        setOffsetToTopAligned();
        }

    public final MenuEntry getEntry( final int aIndex )
        {
        return (MenuEntry) myEntries.get( aIndex );
        }

    public final MenuEntry addMenuEntry( final int aID, final String aText ) throws Exception
        {
        final int x = 0;
        final int y = myVerticalOffset + myFont.charHeight() * FixedMath.toInt( myEntries.size * lineSpacingFixed );

        final MenuEntry newEntry = new MenuEntry( myFont, aText, new Position( x, y ) );
        newEntry.id = aID;
        //#if TOUCH
        newEntry.touchable.associatedHandler = this;
        //#endif
        addScreen( newEntry );
        myEntries.add( newEntry );

        updateSelectedEntry();

        return newEntry;
        }

    public final void removeAllEntries()
        {
        for ( int idx = 0; idx < myEntries.size; idx++ )
            {
            removeScreen( (ScreenBase) myEntries.get( idx ) );
            }
        myEntries.clear();
        }

    public final void updateSelectedEntry()
        {
        updateSelectedEntry( mySelectedEntryIndex );
        }

    public final void updateSelectedEntry( final int aSelectedEntry )
        {
        final int numberOfEntries = myEntries.size;
        mySelectedEntryIndex = ( aSelectedEntry + numberOfEntries ) % numberOfEntries;

        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final MenuEntry menuEntry = getEntry( idx );
            menuEntry.setSelected( idx == mySelectedEntryIndex );
            }
        }

    //#ifdef TOUCH

    // From TouchableHandler

    public void onPressed( final Object aTouchable )
        {
        try
            {
            final MenuEntry selected = (MenuEntry) aTouchable;
            myMenuHandler.onSelected( selected );
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
        if ( entryPositionsMode == ENTRY_POSITIONS_CENTERED )
            {
            setOffsetToVerticallyCentered();
            }
        if ( entryPositionsMode != ENTRY_POSITIONS_MANUAL )
            {
            updateEntryPositions();
            }
        }

    public final void onControlTick() throws Exception
        {
        //#if TOUCH
        addTouchableAreas();
        //#endif

        final KeysHandler keys = system().keys;
        if ( keys.checkUpAndConsume() )
            {
            updateSelectedEntry( mySelectedEntryIndex - 1 );
            }
        if ( keys.checkDownAndConsume() )
            {
            updateSelectedEntry( mySelectedEntryIndex + 1 );
            }
        if ( keys.checkLeftSoftAndConsume() || keys.checkStickDownAndConsume() || keys.checkFireAndConsume() )
            {
            onLeftSoftKey( getEntry( mySelectedEntryIndex ) );
            }
        if ( keys.checkRightSoftAndConsume() )
            {
            onRightSoftKey( getEntry( mySelectedEntryIndex ) );
            }

        super.onControlTick();
        }

    // Protected Interface

    protected void onLeftSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        if ( myMenuHandler instanceof MenuHandlerEx )
            {
            final MenuHandlerEx handlerEx = (MenuHandlerEx) myMenuHandler;
            handlerEx.onLeftSoftKey( aSelectedEntry );
            }
        else
            {
            myMenuHandler.onSelected( aSelectedEntry );
            }
        }

    protected void onRightSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        if ( myMenuHandler instanceof MenuHandlerEx )
            {
            final MenuHandlerEx handlerEx = (MenuHandlerEx) myMenuHandler;
            handlerEx.onRightSoftKey( aSelectedEntry );
            }
        else
            {
            // This is the default behaviour :)
            system().shutdownAndExit();
            }
        }

    // Implementation

    private void setOffsetToTopAligned()
        {
        myVerticalOffset = myFont.charHeight();
        }

    private void setOffsetToVerticallyCentered()
        {
        myVerticalOffset = ( screen().height() - ( myEntries.size - 1 ) * FixedMath.toInt( myFont.charHeight() * lineSpacingFixed ) ) / 2;
        }

    private void updateEntryPositions()
        {
        final int xCenter = screen().width() / 2;
        for ( int idx = 0; idx < myEntries.size; idx++ )
            {
            final MenuEntry entry = getEntry( idx );
            entry.position.x = xCenter;
            entry.position.y = myVerticalOffset + FixedMath.toInt( idx * myFont.charHeight() * lineSpacingFixed );
            //#if TOUCH
            entry.updateTouchable();
            //#endif
            }
        }

    //#if TOUCH

    private void addTouchableAreas()
        {
        final TouchHandler touch = touch();
        final int numberOfMenuEntries = myEntries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.addLocalControl( getEntry( idx ).touchable );
            }
        }

    //#endif


    public int myVerticalOffset;

    private int mySelectedEntryIndex;

    private final FontGenerator myFont;

    private final MenuHandler myMenuHandler;

    private final DynamicArray myEntries = new DynamicArray();
    }
