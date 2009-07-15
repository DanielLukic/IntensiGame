package net.intensicode.util;

public final class DynamicArray
    {
    public static final int DEFAULT_CAPACITY = 10;

    public static final int DEFAULT_EXTEND_STEP = 10;

    public Object[] objects;

    public int extend_step;

    public int size;



    public DynamicArray()
        {
        this( 5, 5 );
        }

    public DynamicArray( final int aInitialCapacity, final int aExtendStep )
        {
        if ( aInitialCapacity < 1 ) throw new IllegalArgumentException();
        if ( aExtendStep < 0 ) throw new IllegalArgumentException();
        objects = new Object[aInitialCapacity];
        extend_step = aExtendStep;
        }

    public static DynamicArray fromArray( final Object[] aArray )
        {
        if ( aArray == null || aArray.length == 0 ) return new DynamicArray();

        final DynamicArray array = new DynamicArray( aArray.length, aArray.length );
        System.arraycopy( aArray, 0, array.objects, 0, aArray.length );
        array.size = aArray.length;
        return array;
        }

    public final Object[] toArray()
        {
        final Object[] output = new Object[size];
        toArray( output );
        return output;
        }

    public final void toArray( final Object[] aArray )
        {
        System.arraycopy( objects, 0, aArray, 0, size );
        }

    public final void clear()
        {
        for ( int idx = 0; idx < size; idx++ )
            {
            objects[ idx ] = null;
            }
        size = 0;
        }

    public final void clearNullObjects()
        {
        for ( int idx = size - 1; idx >= 0; idx-- )
            {
            if ( objects[ idx ] == null ) remove( idx );
            }
        }

    /**
     * Search aObject using == comparison.
     *
     * @param aObjectByReference the object (reference) to search for.
     * @return true if the object is stored in the array.
     */
    public final boolean contains( final Object aObjectByReference )
        {
        for ( int idx = 0; idx < size; idx++ )
            {
            if ( objects[ idx ] == aObjectByReference ) return true;
            }
        return false;
        }

    /**
     * Search aObject using Object#equals comparison.
     *
     * @param aObjectByEquals the object to search for.
     * @return index of the object that equals aObjectByEquals or -1 if no equal object found.
     */
    public int find( final Object aObjectByEquals )
        {
        for ( int idx = 0; idx < size; idx++ )
            {
            final Object object = objects[ idx ];
            if ( object != null && object.equals( aObjectByEquals ) ) return idx;
            if ( object == aObjectByEquals ) return idx;
            }
        return -1;
        }

    public final int index( final Object aObject )
        {
        for ( int idx = 0; idx < size; idx++ )
            {
            if ( objects[ idx ] == aObject ) return idx;
            }
        return -1;
        }

    public final int lastIndex( final Object aObject )
        {
        for ( int idx = size - 1; idx >= 0; idx-- )
            {
            if ( objects[ idx ] == aObject ) return idx;
            }
        return -1;
        }

    public final Object get( final int aIndex )
        {
        if ( aIndex < 0 || aIndex >= size ) throw new IndexOutOfBoundsException();
        return objects[ aIndex ];
        }

    public final void set( final int aIndex, final Object aObject )
        {
        if ( aIndex < 0 || aIndex >= size ) throw new IndexOutOfBoundsException();
        objects[ aIndex ] = aObject;
        }

    public final void add( final Object aObject )
        {
        if ( size >= objects.length ) increaseSize();
        objects[ size++ ] = aObject;
        }

    public final void addAll( final DynamicArray aArray )
        {
        for ( int idx = 0; idx < aArray.size; idx++ )
            {
            add( aArray.objects[ idx ] );
            }
        }

    public final void insert( final int aIndex, final Object aObject )
        {
        if ( size == objects.length ) increaseSize();
        // This check is required for if extend_step is 0:
        if ( size < objects.length ) size++;
        for ( int idx = size - 1; idx > aIndex; idx-- )
            {
            objects[ idx ] = objects[ idx - 1 ];
            }
        objects[ aIndex ] = aObject;
        }

    public final void insertAll( final int aIndex, final DynamicArray aArray )
        {
        for ( int idx = aArray.size - 1; idx >= 0; idx-- )
            {
            insert( aIndex, aArray.objects[ idx ] );
            }
        }

    public final Object remove( final int aIndex )
        {
        if ( aIndex < 0 || aIndex >= size ) throw new IndexOutOfBoundsException();

        final Object result = objects[ aIndex ];

        // Seen buggy implementations of System.arraycopy when input and output array are the same..
        //System.arraycopy( objects, aIndex + 1, objects, aIndex, lastIndex - aIndex );

        final int lastIndex = --size;
        for ( int idx = aIndex; idx < lastIndex; idx++ )
            {
            objects[ idx ] = objects[ idx + 1 ];
            }
        objects[ lastIndex ] = null;

        return result;
        }

    public final boolean remove( final Object aObject )
        {
        for ( int idx = 0; idx < size; idx++ )
            {
            if ( objects[ idx ] == aObject )
                {
                remove( idx );
                return true;
                }
            }
        return false;
        }

    public final void removeAll( final Object aObject )
        {
        for ( int idx = size - 1; idx >= 0; idx-- )
            {
            if ( objects[ idx ] == aObject ) remove( idx );
            }
        }

    public final Object removeLast()
        {
        if ( size == 0 ) return null;
        return remove( size - 1 );
        }

    public final Object last()
        {
        if ( size == 0 ) return null;
        return objects[ size - 1 ];
        }

    public final void sort()
        {
        quicksort( 0, size - 1 );
        }

    public final void dump()
        {
        //#if DEBUG
        Log.debug( "DYNAMIC ARRAY DUMP TOP DOWN:" );
        for ( int idx = size - 1; idx >= 0; idx-- )
            {
            Log.debug( "> {}", get( idx ) );
            }
        //#endif
        }

    public final void each( final Visitor aVisitor )
        {
        aVisitor.init();
        for ( int idx = 0; idx < size; idx++ )
            {
            aVisitor.visit( objects[ idx ] );
            }
        aVisitor.done();
        }

    // From Object

    //#if LOGGING || DEBUG

    public final String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        for ( int idx = 0; idx < size; idx++ )
            {
            buffer.append( get( idx ) );
            buffer.append( ',' );
            }
        if ( buffer.length() > 0 ) buffer.setLength( buffer.length() - 1 );
        return buffer.toString();
        }

    //#endif

    // Implementation

    private void increaseSize()
        {
        final Object[] newObjects = new Object[size + extend_step];
        System.arraycopy( objects, 0, newObjects, 0, size );
        objects = newObjects;
        }

    private void quicksort( final int aLeft, final int aRight )
        {
        if ( aRight <= aLeft ) return;

        final int pivotIndex = aLeft + ( ( aRight - aLeft ) / 2 );
        final Comparable pivotItem = (Comparable) objects[ pivotIndex ];

        swap( pivotIndex, aRight );

        int index = aLeft;
        for ( int idx = index; idx < aRight; idx++ )
            {
            final Comparable item = (Comparable) objects[ idx ];
            if ( item.compareTo( pivotItem ) < 0 ) swap( index++, idx );
            }
        swap( index, aRight );

        quicksort( aLeft, index );
        quicksort( index + 1, aRight );
        }

    private void swap( final int aIndex1, final int aIndex2 )
        {
        final Object temp = objects[ aIndex1 ];
        objects[ aIndex1 ] = objects[ aIndex2 ];
        objects[ aIndex2 ] = temp;
        }
    }
