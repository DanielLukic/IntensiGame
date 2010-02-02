package net.intensicode.core;

public interface FontResource
    {
    int charWidth( char aCharCode );

    int getHeight();

    int substringWidth( String aString, int aOffset, int aLength );
    }
