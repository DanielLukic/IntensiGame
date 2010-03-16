package net.intensicode;

import net.intensicode.io.StorageIO;

import java.io.*;

public final class ConfigurationElementsTreeIO extends StorageIO
    {
    public ConfigurationElementsTreeIO( final ConfigurationElementsTree aTree )
        {
        super( aTree.label );
        myTree = aTree;
        }

    // From StorageIO

    public final void loadFrom( final DataInputStream aInput ) throws IOException
        {
        final String treeLabel = aInput.readUTF();
        if ( !myTree.label.equals( treeLabel ) ) throw new IOException( "label mismatch" );

        load( myTree, aInput );
        }

    public final void saveTo( final DataOutputStream aOutput ) throws IOException
        {
        final String treeLabel = myTree.label;
        aOutput.writeUTF( treeLabel );

        save( myTree, aOutput );
        }

    // Implementation

    private static void load( final ConfigurationElementsTree aTree, final DataInputStream aInput ) throws IOException
        {
        final int treeId = aInput.read();
        if ( treeId != ID_TREE ) throw new IOException( "tree id expected" );

        final int numberOfEntries = aInput.readInt();
        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final String entryLabel = aInput.readUTF();
            final ConfigurationElementsTree entry = aTree.getEntryByLabel( entryLabel );
            if ( entry.isLeaf() )
                {
                final int entryId = aInput.read();
                if ( entryId != ID_LEAF ) throw new IOException( "leaf id expected" );
                entry.value.setNewValue( aInput.readInt() );
                }
            else
                {
                load( entry, aInput );
                }
            }
        }

    private static void save( final ConfigurationElementsTree aTree, final DataOutputStream aOutput ) throws IOException
        {
        aOutput.write( ID_TREE );

        aOutput.writeInt( aTree.numberOfEntries() );
        for ( int idx = 0; idx < aTree.numberOfEntries(); idx++ )
            {
            final ConfigurationElementsTree entry = aTree.getEntry( idx );
            aOutput.writeUTF( entry.label );
            if ( entry.isLeaf() )
                {
                aOutput.write( ID_LEAF );
                aOutput.writeInt( entry.value.getCurrentValue() );
                }
            else
                {
                save( entry, aOutput );
                }
            }
        }


    private final ConfigurationElementsTree myTree;

    private static final char ID_TREE = 'T';

    private static final char ID_LEAF = 'L';
    }
