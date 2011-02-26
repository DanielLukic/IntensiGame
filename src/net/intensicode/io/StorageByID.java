package net.intensicode.io;

import java.io.*;

public abstract class StorageByID extends StorageIO
    {
    public StorageByID( final String aName )
        {
        super( aName );
        }

    // From StorageIO

    public void loadFrom( final DataInputStream aInput ) throws IOException
        {
        while ( true )
            {
            final int id = aInput.read();
            if ( id == -1 ) break;
            loadEntry( id, aInput );
            }
        }

    // Protected Interface

    protected static void writeBoolean( final DataOutputStream aOutput, final int aID, final boolean aValue ) throws IOException
        {
        aOutput.write( aID );
        aOutput.write( aValue ? 1 : 0 );
        }

    protected static void writeByte( final DataOutputStream aOutput, final int aID, final int aValue ) throws IOException
        {
        aOutput.write( aID );
        aOutput.writeByte( aValue );
        }

    protected static void writeInt( final DataOutputStream aOutput, final int aID, final int aValue ) throws IOException
        {
        aOutput.write( aID );
        aOutput.writeInt( aValue );
        }

    // Abstract Interface

    protected abstract void loadEntry( final int aID, final DataInputStream aInput ) throws IOException;
    }
