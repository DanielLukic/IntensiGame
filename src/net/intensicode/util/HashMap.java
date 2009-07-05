/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.util;

import java.util.Enumeration;
import java.util.Hashtable;



/**
 * Safe but slow hashmap working with equals-based key matching.
 */
public final class HashMap
{
    public final void clear()
    {
        iData.clear();
    }

    public final boolean containsKey( final String aKey )
    {
        return findKey( aKey ) != aKey;
    }

    public final void remove( final String aKey )
    {
        final Object key = findKey( aKey );
        iData.remove( key );
    }

    public final void put( final Object aKey, final Object aValue )
    {
        final Object key = findKey( aKey );
        iData.put( key, aValue );
    }

    public final Object get( final Object aKey )
    {
        final Object key = findKey( aKey );
        return iData.get( key );
    }

    // Implementation

    private final Object findKey( final Object aKey )
    {
        final Enumeration keys = iData.keys();
        while ( keys.hasMoreElements() )
        {
            final Object key = keys.nextElement();
            if ( key.equals( aKey ) )
            {
                return key;
            }
        }
        return aKey;
    }



    private final Hashtable iData = new Hashtable();
}
