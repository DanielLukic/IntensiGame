package net.intensicode.screens;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.graphics.SpriteGenerator;

public class ButtonStyleMenu extends BasicMenu
        //#if TOUCH
        implements net.intensicode.touch.TouchableHandler
        //#endif
    {
    public ButtonStyleMenu( final MenuHandler aMenuHandler, final FontGenerator aMenuFont )
        {
        super( aMenuHandler, aMenuFont );
        }

    public final void setEntryImage( final SpriteGenerator aSpriteGenerator )
        {
        if ( aSpriteGenerator == SpriteGenerator.NULL ) myEntryImageGenerator = null;
        else myEntryImageGenerator = aSpriteGenerator;

        for ( int idx = 0; idx < myEntries.size; idx++ )
            {
            final BasicMenuEntry entry = getEntry( idx );
            if ( !( entry instanceof ButtonStyleEntry ) ) continue;
            final ButtonStyleEntry buttonEntry = (ButtonStyleEntry) entry;
            buttonEntry.setButtonGenerator( myEntryImageGenerator );
            }
        }

    public final BasicMenuEntry addMenuEntry( final int aID, final String aText ) throws Exception
        {
        final ButtonStyleMenuEntry entry = new ButtonStyleMenuEntry();
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


    private SpriteGenerator myEntryImageGenerator;
    }
