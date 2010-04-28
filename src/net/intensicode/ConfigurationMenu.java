package net.intensicode;

import net.intensicode.graphics.FontGenerator;
import net.intensicode.screens.*;
import net.intensicode.util.DynamicArray;

public final class ConfigurationMenu extends MenuBase
    {
    public ConfigurationMenu( final String aTitle, final FontGenerator aFontGenerator )
        {
        super( aFontGenerator );
        myTitle = aTitle;
        }

    private final DynamicArray myNodes = new DynamicArray();

    public final void add( final ConfigurationElementsTree aConfigNode )
        {
        myNodes.add( aConfigNode );
        }

    public final void addChildren( final ConfigurationElementsTree aConfigNode )
        {
        final int numberOfEntries = aConfigNode.numberOfEntries();
        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            add( aConfigNode.getEntry( idx ) );
            }
        }

    public void onInitOnce() throws Exception
        {
        mySoftkeys = new SoftkeysScreen( font );
        mySoftkeys.setSoftkeys( "SELECT", "BACK" );
        addScreen( mySoftkeys );
        }

    protected void beforeInitEverytime() throws Exception
        {
        removeAllEntries();

        addScreen( new AlignedTextScreen( font, myTitle, screen().width() / 2, font.charHeight(), FontGenerator.CENTER ) );

        for ( int idx = 0; idx < myNodes.size; idx++ )
            {
            final ConfigurationElementsTree node = (ConfigurationElementsTree) myNodes.get( idx );
            addMenuEntry( idx, node.label );
            }
        }

    protected void onRightSoftKey( final MenuEntry aSelectedEntry ) throws Exception
        {
        stack().popScreen( this );
        }

    protected void onSelected( final MenuEntry aSelectedEntry ) throws Exception
        {
        final ConfigurationElementsTree node = (ConfigurationElementsTree) myNodes.get( aSelectedEntry.id );
        if ( node.isLeaf() )
            {
            // TODO: Open configuration dialog screen..
            }
        else
            {
            final ConfigurationMenu menu = new ConfigurationMenu( node.label, font );
            menu.addChildren( node );
            stack().pushOnce( menu );
            }
        }

    private final String myTitle;

    private SoftkeysScreen mySoftkeys;
    }
