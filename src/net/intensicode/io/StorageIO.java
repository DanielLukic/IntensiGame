package net.intensicode.io;

import java.io.*;

public abstract class StorageIO
    {
    public StorageIO( final String aName )
        {
        myName = aName;
        }

    public final String getName()
        {
        return myName;
        }

    // Abstract Interface

    public abstract void saveTo( final DataOutputStream aOutput ) throws IOException;

    public abstract void loadFrom( final DataInputStream aInput ) throws IOException;

    private final String myName;
    }
