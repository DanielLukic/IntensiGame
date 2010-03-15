package net.intensicode;

import net.intensicode.util.DynamicArray;

public final class ConfigurationElementsTree
    {
    public static final ConfigurationElementsTree EMPTY = new ConfigurationElementsTree( "" );

    public ConfigurableValue value;

    public String label;


    public ConfigurationElementsTree( final String aLabel )
        {
        label = aLabel;
        }

    public final ConfigurationElementsTree addSubTree( final String aLabel )
        {
        final ConfigurationElementsTree subTree = new ConfigurationElementsTree( aLabel );
        myEntries.add( subTree );
        return subTree;
        }

    public final ConfigurationElementsTree addLeaf( final ConfigurableValue aConfigurableValue )
        {
        final ConfigurationElementsTree leaf = new ConfigurationElementsTree( aConfigurableValue.getTitle() );
        leaf.value = aConfigurableValue;
        myEntries.add( leaf );
        return leaf;
        }

    public final int numberOfEntries()
        {
        return myEntries.size;
        }

    public final ConfigurationElementsTree getEntry( final int aIndex )
        {
        return (ConfigurationElementsTree) myEntries.get( aIndex );
        }

    public final ConfigurationElementsTree getEntryByLabel( final String aLabel )
        {
        for ( int idx = 0; idx < numberOfEntries(); idx++ )
            {
            final ConfigurationElementsTree entry = getEntry( idx );
            if ( entry.label.equals( aLabel ) ) return entry;
            }
        throw new IllegalArgumentException( "entry label not found: " + aLabel );
        }

    public final boolean isLeaf()
        {
        return myEntries.size == 0;
        }


    private final DynamicArray myEntries = new DynamicArray();
    }
