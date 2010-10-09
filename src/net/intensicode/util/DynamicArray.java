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

    public final boolean empty()
        {
        return size == 0;
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

    public final boolean contains( final Object aObject )
        {
        for ( int idx = 0; idx < size; idx++ )
            {
            if ( objects[ idx ] == aObject ) return true;
            }
        return false;
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

    public final Object removeLast()
        {
        if ( empty() ) throw new IllegalStateException();
        return remove( size - 1 );
        }

    public final Object remove( final int aIndex )
        {
        if ( aIndex < 0 || aIndex >= size ) throw new IndexOutOfBoundsException();

        final Object result = objects[ aIndex ];

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
        while ( remove( aObject ) ) ;
        }

    public final Object last()
        {
        if ( size == 0 ) return null;
        return objects[ size - 1 ];
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

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        for ( int idx = 0; idx < size; idx++ )
            {
            buffer.append( objects[ idx ] );
            buffer.append( ',' );
            }
        if ( buffer.length() > 0 ) buffer.setLength( buffer.length() - 1 );
        return buffer.toString();
        }

    // Implementation

    private void increaseSize()
        {
        final Object[] newObjects = new Object[size + extend_step];
        System.arraycopy( objects, 0, newObjects, 0, size );
        objects = newObjects;
        }
    }
