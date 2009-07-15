package net.intensicode.net.connection;

import net.intensicode.net.Request;
import net.intensicode.net.Response;
import net.intensicode.util.RecordStorage;

import java.io.IOException;

public final class LastModifiedCachingConnection implements TransportConnection
    {
    public LastModifiedCachingConnection( final TransportConnection aChainedConnection, final CachingControl aCachingControl )
        {
        myChainedConnection = aChainedConnection;
        myCachingControl = aCachingControl;
        }

    // From TransportConnection

    public final boolean hasBeenInterrupted()
        {
        return myChainedConnection.hasBeenInterrupted();
        }

    public void prepare( final Request aRequest ) throws IOException
        {
        myResponse = Response.NULL;
        prepareCachedResponse( aRequest );
        if ( myResponse == Response.NULL ) myChainedConnection.prepare( aRequest );
        }

    public final Response process( final Request aRequest ) throws IOException, InterruptedException
        {
        if ( myResponse != Response.NULL ) return myResponse;

        final Response response = myChainedConnection.process( aRequest );
        if ( myCachingControl.canBeCached( aRequest ) ) syncFromOrWithCache( aRequest, response );
        return response;
        }

    public final void cancel()
        {
        myChainedConnection.cancel();
        }

    public final void close()
        {
        myChainedConnection.close();
        }

    // Implementation

    private void prepareCachedResponse( final Request aRequest )
        {
        if ( !myCachingControl.canBeCached( aRequest ) ) return;

        setLastModifiedIfNotDirty( aRequest );

        if ( !cachedCategoryIconRequested( aRequest ) ) return;

        createResponseFromCachedData( aRequest );
        }

    private void setLastModifiedIfNotDirty( final Request aRequest )
        {
        if ( myCachingControl.isDirtyData( aRequest ) ) return;
        aRequest.lastModified = getLastModifiedOrNull( aRequest );
        }

    private boolean cachedCategoryIconRequested( final Request aRequest )
        {
        return aRequest.lastModified != null && aRequest.id.indexOf( "images/categories/" ) != -1;
        }

    private Object retrieveFromCache( final Request aRequest )
        {
        return myStorage.retrieveOrNull( aRequest.id );
        }

    private void createResponseFromCachedData( final Request aRequest )
        {
        final Object data = retrieveFromCache( aRequest );
        if ( data == null ) return;

        myResponse = new Response();
        myResponse.setParameter( Response.IMAGE_DATA_KEY, data );
        myResponse.code = Response.HTTP_OK;
        }

    private void syncFromOrWithCache( final Request aRequest, final Response aResponse )
        {
        if ( aResponse.code == Response.HTTP_OK )
            {
            storeResponse( aRequest, aResponse );
            }
        else if ( aResponse.code == Response.HTTP_NOT_MODIFIED )
            {
            updateResponseFromCache( aRequest, aResponse );
            }
        }

    private void updateResponseFromCache( final Request aRequest, final Response aResponse )
        {
        final Object data = retrieveFromCache( aRequest );
        aResponse.setParameter( Response.IMAGE_DATA_KEY, data );
        aResponse.code = Response.HTTP_OK;
        }

    private String getLastModifiedOrNull( final Request aRequest )
        {
        return myStorage.getStringOrNull( aRequest.id + TIMESTAMP );
        }

    private void storeResponse( final Request aRequest, final Response aResponse )
        {
        final byte[] imageData = aResponse.getImageData();
        myStorage.put( aRequest.id + TIMESTAMP, aResponse.getParameter( LAST_MODIFIED ) );
        myStorage.storeOrIgnore( aRequest.id, imageData );
        }



    private Response myResponse;

    private final TransportConnection myChainedConnection;

    private final CachingControl myCachingControl;

    private final RecordStorage myStorage = new RecordStorage( "LAST_MODIFIED_CACHE" );

    private static final String TIMESTAMP = ".timestamp";

    private static final String LAST_MODIFIED = "last-modified";
    }
