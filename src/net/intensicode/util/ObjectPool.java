package net.intensicode.util;

import net.intensicode.core.ChainedException;

public class ObjectPool
    {
    public ObjectPool( final String aObjectClassName ) throws ClassNotFoundException
        {
        this( Class.forName( aObjectClassName ) );
        }

    public ObjectPool( final Class aObjectClass )
        {
        myObjectClass = aObjectClass;
        }

    public final Object getOrCreateInstance()
        {
        try
            {
            if ( myReleasedInstances.size == 0 ) return myObjectClass.newInstance();
            return myReleasedInstances.removeLast();
            }
        catch ( final Exception e )
            {
            throw new ChainedException( e );
            }
        }

    public final void addReleasedInstance( final Object aReleasedInstance )
        {
        Assert.equals( "class matches", myObjectClass, aReleasedInstance.getClass() );
        myReleasedInstances.add( aReleasedInstance );
        Assert.isFalse( "too many pooled instances", myReleasedInstances.size > POOLED_INSTANCES_THRESHOLD );
        }


    private final Class myObjectClass;

    private final DynamicArray myReleasedInstances = new DynamicArray();

    private static final int POOLED_INSTANCES_THRESHOLD = 256;
    }
