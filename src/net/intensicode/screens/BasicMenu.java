package net.intensicode.screens;

import net.intensicode.core.GameSystem;
import net.intensicode.core.KeysHandler;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.DynamicArray;
import net.intensicode.util.EntryPositioner;

public abstract class BasicMenu extends MultiScreen
        //#if TOUCH
        implements net.intensicode.touch.TouchableHandler
        //#endif
    {
    public final EntryPositioner positioner = new EntryPositioner();


    public BasicMenu( final MenuHandler aMenuHandler, final FontGenerator aMenuFont )
        {
        myMenuHandler = aMenuHandler;
        myFont = aMenuFont;
        }

    public final BasicMenuEntry getEntryById( final int aID )
        {
        for ( int idx = 0; idx < myEntries.size; idx++ )
            {
            final BasicMenuEntry entry = getEntry( idx );
            if ( entry.id() == aID ) return entry;
            }
        throw new IllegalArgumentException();
        }

    public final int getSelectedEntryIndex()
        {
        return mySelectedEntryIndex;
        }

    public final void selectEntryByIndex( final int aIndex )
        {
        updateSelectedEntry( aIndex );
        }

    public final BasicMenuEntry addMenuEntry( final BasicMenuEntry aEntry ) throws Exception
        {
        //#if TOUCH
        aEntry.touchable().associatedHandler = this;
        //#endif
        addScreen( aEntry.visual() );
        myEntries.add( aEntry );

        updateSelectedEntry();

        return aEntry;
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
            final BasicMenuEntry menuEntry = getEntry( idx );
            menuEntry.setSelected( idx == mySelectedEntryIndex );
            }
        }

    //#ifdef TOUCH

    // From TouchableHandler

    public void onPressed( final Object aTouchable )
        {
        try
            {
            final BasicMenuEntry selected = (BasicMenuEntry) aTouchable;
            for ( int idx = 0; idx < myEntries.size; idx++ )
                {
                final BasicMenuEntry entry = getEntry( idx );
                if ( entry == selected ) updateSelectedEntry( idx );
                }
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
        positioner.setFont( myFont );
        positioner.shareDirectScreen( screen() );
        positioner.updatePositions( myEntries );
        }

    public final void onControlTick() throws Exception
        {
        //#if TOUCH
        addTouchableAreas();
        //#endif

        final KeysHandler keys = system().keys;
        if ( keys.checkUpAndConsume() || keys.checkLeftAndConsume() )
            {
            updateSelectedEntry( mySelectedEntryIndex - 1 );
            }
        if ( keys.checkDownAndConsume() || keys.checkRightAndConsume() )
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

    protected BasicMenuEntry getEntry( final int aIndex )
        {
        return (BasicMenuEntry) myEntries.get( aIndex );
        }

    protected void onLeftSoftKey( final BasicMenuEntry aSelectedEntry ) throws Exception
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

    protected void onRightSoftKey( final BasicMenuEntry aSelectedEntry ) throws Exception
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

    //#if TOUCH

    private void addTouchableAreas()
        {
        final net.intensicode.touch.TouchHandler touch = touch();
        final int numberOfMenuEntries = myEntries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.addLocalControl( getEntry( idx ).touchable() );
            }
        }

    //#endif


    private int mySelectedEntryIndex;

    protected final FontGenerator myFont;

    private final MenuHandler myMenuHandler;

    protected final DynamicArray myEntries = new DynamicArray();
    }
