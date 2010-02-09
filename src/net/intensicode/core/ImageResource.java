package net.intensicode.core;

public interface ImageResource
    {
    int getWidth();

    int getHeight();

    DirectGraphics getGraphics();

    void getRGB( int[] aBuffer, int aOffsetX, int aScanlineSize, int aX, int aY, int aWidth, int aHeight );

    void purge();
    }
