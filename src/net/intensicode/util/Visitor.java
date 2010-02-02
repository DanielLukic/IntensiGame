package net.intensicode.util;

public abstract class Visitor
    {
    public void init()
        {
        }

    public void done()
        {
        }

    public abstract void visit( Object aObject );
    }
