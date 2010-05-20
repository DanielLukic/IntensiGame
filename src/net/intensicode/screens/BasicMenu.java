package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.*;
import net.intensicode.util.DynamicArray;

public class BasicMenu extends MultiScreen
        //#if TOUCH
        implements net.intensicode.touch.TouchableHandler
        //#endif
    {
    public static final int ENTRY_POSITIONS_CENTERED = 0;

    public static final int ENTRY_POSITIONS_TOP_ALIGNED = 1;

    public static final int ENTRY_POSITIONS_MANUAL = 2;

    public int entryPositionsMode = ENTRY_POSITIONS_CENTERED;

    public float lineSpacingFixed = 2f;

    public int verticalOffset;


    public BasicMenu( final MenuHandler aMenuHandler, final FontGenerator aMenuFont )
        {
        myMenuHandler = aMenuHandler;
        myFont = aMenuFont;
        setOffsetToTopAligned();
        }

    public final void setEntryImage( final SpriteGenerator aSpriteGenerator )
        {
        myEntryImageGenerator = aSpriteGenerator;
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
        final BasicMenuEntry entry = new BasicMenuEntry( aID, aText, myFont );
        entry.imageGenerator = myEntryImageGenerator;
        //#if TOUCH
        entry.touchable.associatedHandler = this;
        //#endif
        addScreen( entry );
        myEntries.add( entry );

        updateSelectedEntry();

        return entry;
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

    private void setOffsetToTopAligned()
        {
        verticalOffset = myFont.charHeight();
        }

    private void setOffsetToVerticallyCentered()
        {
        if ( myEntries.size == 0 ) return;

        final float maxSpacing = screen().height() * 1f / ( myEntries.size * myFont.charHeight() );
        lineSpacingFixed = Math.min( maxSpacing, lineSpacingFixed );
        verticalOffset = (int) ( ( screen().height() - ( myEntries.size - 1 ) * myFont.charHeight() * lineSpacingFixed ) / 2 );
        }

    private void updateEntryPositions()
        {
        final int xCenter = screen().width() / 2;
        for ( int idx = 0; idx < myEntries.size; idx++ )
            {
            final BasicMenuEntry entry = getEntry( idx );
            entry.position.x = xCenter;
            entry.position.y = (int) ( verticalOffset + idx * myFont.charHeight() * lineSpacingFixed );
            //#if TOUCH
            entry.updateTouchable();
            //#endif
            }
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

    private final FontGenerator myFont;

    private final MenuHandler myMenuHandler;

    private final DynamicArray myEntries = new DynamicArray();

    private SpriteGenerator myEntryImageGenerator;
    }
