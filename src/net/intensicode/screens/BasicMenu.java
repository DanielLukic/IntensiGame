package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.*;
import net.intensicode.util.*;

public class BasicMenu extends MultiScreen
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
            if ( entry.id == aID ) return entry;
            }
        throw new IllegalArgumentException();
        }

    public final void setEntryImage( final SpriteGenerator aSpriteGenerator )
        {
        if ( aSpriteGenerator == SpriteGenerator.NULL ) myEntryImageGenerator = null;
        else myEntryImageGenerator = aSpriteGenerator;

        for ( int idx = 0; idx < myEntries.size; idx++ )
            {
            final BasicMenuEntry entry = getEntry( idx );
            entry.imageGenerator = myEntryImageGenerator;
            }
        }

    public final int getSelectedEntryIndex()
        {
        return mySelectedEntryIndex;
        }

    public final void selectEntryByIndex( final int aIndex )
        {
        updateSelectedEntry( aIndex );
        }

    public final BasicMenuEntry addMenuEntry( final int aID, final String aText ) throws Exception
        {
        final BasicMenuEntry entry = new BasicMenuEntry();
        entry.id = aID;
        entry.text = aText;
        entry.fontGen = myFont;
        entry.imageGenerator = myEntryImageGenerator;
        //#if TOUCH
        entry.touchable.associatedHandler = this;
        //#endif
        addScreen( entry );
        myEntries.add( entry );

        updateSelectedEntry();

        return entry;
        }

    public final BasicMenuEntry addMenuEntry( final BasicMenuEntry aEntry ) throws Exception
        {
        aEntry.imageGenerator = myEntryImageGenerator;
        //#if TOUCH
        aEntry.touchable.associatedHandler = this;
        //#endif
        addScreen( aEntry );
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
            menuEntry.selected = idx == mySelectedEntryIndex;
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

    private BasicMenuEntry getEntry( final int aIndex )
        {
        return (BasicMenuEntry) myEntries.get( aIndex );
        }

    //#if TOUCH

    private void addTouchableAreas()
        {
        final net.intensicode.touch.TouchHandler touch = touch();
        final int numberOfMenuEntries = myEntries.size;
        for ( int idx = 0; idx < numberOfMenuEntries; idx++ )
            {
            touch.addLocalControl( getEntry( idx ).touchable );
            }
        }

    //#endif


    private int mySelectedEntryIndex;

    private SpriteGenerator myEntryImageGenerator;

    private final FontGenerator myFont;

    private final MenuHandler myMenuHandler;

    private final DynamicArray myEntries = new DynamicArray();
    }
