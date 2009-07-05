/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.Engine;
import net.intensicode.core.Keys;
import net.intensicode.core.MultiScreen;
import net.intensicode.util.DynamicArray;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;



/**
 * TODO: Describe this!
 */
public abstract class AbstractMenu extends MultiScreen
    {
    public final DynamicArray menuEntries = new DynamicArray( 5, 5 );

    public final FontGen menuFontGen;

    public int selectedEntry;



    public AbstractMenu( final FontGen aMenuFont )
        {
        menuFontGen = aMenuFont;
        }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        final Keys keys = aEngine.keys;
        if ( keys.checkUpAndConsume() )
            {
            updateSelectedEntry( selectedEntry - 1 );
            }
        else if ( keys.checkDownAndConsume() )
            {
            updateSelectedEntry( selectedEntry + 1 );
            }
        else if ( keys.checkLeftSoftAndConsume() || keys.checkStickDownAndConsume() || keys.checkFireAndConsume() )
            {
            onLeftSoftKey( getEntry( selectedEntry ) );
            }
        else if ( keys.checkRightSoftAndConsume() )
            {
            onRightSoftKey( getEntry( selectedEntry ) );
            }

        super.onControlTick( aEngine );
        }

    // Protected Interface

    protected abstract void onSelected( final MenuEntry aSelectedEntry ) throws Exception;

    protected void onLeftSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        onSelected( aSelectedEntry );
        }

    protected void onRightSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        engine().shutdownAndExit();
        }

    protected final void removeAllEntries()
        {
        for ( int idx = 0; idx < menuEntries.size; idx++ )
            {
            removeScreen( (AbstractScreen) menuEntries.get( idx ) );
            }
        menuEntries.clear();
        }

    protected final MenuEntry addMenuEntry( final int aID, final String aText ) throws Exception
        {
        final int x = screen().width() / 2;
        final int y = (menuEntries.size + 2) * (menuFontGen.charHeight() * 3 / 2);

        final MenuEntry newEntry = new MenuEntry( menuFontGen, aText, new Position( x, y ) );
        newEntry.id = aID;
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
        selectedEntry = (aSelectedEntry + numberOfEntries) % numberOfEntries;

        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final MenuEntry menuEntry = getEntry( idx );
            menuEntry.setSelected( idx == selectedEntry );
            }
        }

    private final MenuEntry getEntry( final int aIndex )
        {
        return (MenuEntry) menuEntries.get( aIndex );
        }
    }