package net.intensicode.util;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

public final class RecordStorage
    {
    public RecordStorage( final String aNameSpace )
        {
        myNameSpace = aNameSpace;
        myCache = new Hashtable();
        }

    public final String getString( final String aKey )
        {
        return (String) myCache.get( aKey );
        }

    public final void put( final String aKey, final String aValue )
        {
        if ( aValue != null ) myCache.put( aKey, aValue );
        else myCache.remove( aKey );
        }

    public Object retrieveOrNull( final String aKey )
        {
        try
            {
            if ( !myOpenFlag ) openAndLoad();
            final int storageId = findStorageId( aKey );
            if ( storageId == NO_STORAGE_ID ) return null;
            return retrieve( storageId );
            }
        catch ( final StorageException e )
            {
            Log.error( "failed retrieving {}", aKey, e );
            }
        catch ( RecordStoreException e )
            {
            Log.error( "failed retrieving {}", aKey, e );
            }
        return null;
        }

    public void storeOrIgnore( final String aKey, final byte[] aData )
        {
        try
            {
            if ( !myOpenFlag ) openAndLoad();
            final int storageId = findOrCreateStorageId( aKey );
            store( storageId, aData );
            put( aKey, Integer.toString( storageId ) );
            saveAndClose();
            }
        catch ( final StorageException e )
            {
            Log.error( "failed storing {}", aKey, e );
            }
        catch ( RecordStoreException e )
            {
            Log.error( "failed storing {}", aKey, e );
            }
        }

    public final void openAndLoad() throws StorageException
        {
        try
            {
            open();

            myCache.clear();

            if ( myRecordStore.getNumRecords() == 0 ) myRecordStore.addRecord( EMPTY_CACHE_RECORD, 0, EMPTY_CACHE_RECORD.length );

            final byte[] buffer = myRecordStore.getRecord( RECORD_ID );
            final ByteArrayInputStream byteInput = new ByteArrayInputStream( buffer );
            final DataInputStream input = new DataInputStream( byteInput );

            final int entries = input.readInt();
            for ( int idx = 0; idx < entries; idx++ )
                {
                final String key = input.readUTF();
                final String value = input.readUTF();
                myCache.put( key, value );
                }

            input.close();
            }
        catch ( final IOException e )
            {
            Log.error( "failed loading from record store", e );
            throw new StorageException( e );
            }
        catch ( final RecordStoreException e )
            {
            Log.error( "record store operation failed", e );
            throw new StorageException( e );
            }
        }

    public final void saveAndClose() throws StorageException
        {
        try
            {
            open();

            final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            final DataOutputStream output = new DataOutputStream( byteOutput );
            output.writeInt( myCache.size() );

            final Enumeration keys = myCache.keys();
            while ( keys.hasMoreElements() )
                {
                final String key = (String) keys.nextElement();
                final String value = (String) myCache.get( key );
                output.writeUTF( key );
                output.writeUTF( value );
                }
            output.close();

            while ( myRecordStore.getNextRecordID() <= RECORD_ID )
                {
                myRecordStore.addRecord( NULL_BUFFER, 0, NULL_BUFFER.length );
                }

            final byte[] data = byteOutput.toByteArray();
            myRecordStore.setRecord( RECORD_ID, data, 0, data.length );

            close();
            }
        catch ( final IOException e )
            {
            Log.error( "failed saving to record store", e );
            throw new StorageException( e );
            }
        catch ( final RecordStoreException e )
            {
            Log.error( "record store operation failed", e );
            throw new StorageException( e );
            }
        }

    public final String getStringOrNull( final String aKey )
        {
        try
            {
            if ( !myOpenFlag ) openAndLoad();
            return (String) myCache.get( aKey );
            }
        catch ( StorageException e )
            {
            Log.error( "failed opening storage for {}", aKey, e );
            }
        return null;
        }

    // From String

    public final String toString()
        {
        return myNameSpace;
        }

    // Implementation

    private void open() throws RecordStoreException, StorageException
        {
        if ( myOpenFlag ) return;

        myRecordStore = RecordStore.openRecordStore( myNameSpace, true, RecordStore.AUTHMODE_PRIVATE, false );
        if ( myRecordStore == null ) throw new StorageException( "failed opening storage " + myNameSpace );

        myOpenFlag = true;
        }

    private void close() throws RecordStoreException
        {
        if ( !myOpenFlag ) return;

        myRecordStore.closeRecordStore();
        myOpenFlag = false;
        }

    private int findStorageId( final String aKey )
        {
        final String storageId = getString( aKey );
        if ( storageId == null ) return NO_STORAGE_ID;
        return Integer.parseInt( storageId );
        }

    private int findOrCreateStorageId( final String aKey ) throws RecordStoreException
        {
        final int knownId = findStorageId( aKey );
        if ( knownId != NO_STORAGE_ID ) return knownId;
        final int nextId = myRecordStore.getNextRecordID();
        if ( nextId == RECORD_ID ) throw new IllegalStateException();
        return nextId;
        }

    private byte[] retrieve( final int aStorageId ) throws RecordStoreException
        {
        return myRecordStore.getRecord( aStorageId );
        }

    private void store( final int aStorageId, final byte[] aData ) throws RecordStoreException
        {
        if ( aStorageId == myRecordStore.getNextRecordID() )
            {
            final int recordId = myRecordStore.addRecord( aData, 0, aData.length );
            if ( recordId != aStorageId ) throw new IllegalStateException();
            }
        else
            {
            myRecordStore.setRecord( aStorageId, aData, 0, aData.length );
            }
        }



    private boolean myOpenFlag;

    private RecordStore myRecordStore;

    private final Hashtable myCache;

    private final String myNameSpace;

    private final byte[] NULL_BUFFER = new byte[0];

    private static final int RECORD_ID = 1;

    private static final int NO_STORAGE_ID = 0;

    private static final byte[] EMPTY_CACHE_RECORD = new byte[]{0,0,0,0};
    }
