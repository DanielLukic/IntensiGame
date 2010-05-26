package net.intensicode.core;

public final class NullImageResource implements ImageResource
    {
    public static final ImageResource NULL = new NullImageResource();

    private NullImageResource()
        {
        myGraphics = NullDirectGraphics.NULL;
        }

    public int getWidth()
        {
        return 1;
        }

    public int getHeight()
        {
        return 1;
        }

    public DirectGraphics getGraphics()
        {
        return myGraphics;
        }

    public void getRGB( final int[] aBuffer, final int aOffsetX, final int aScanlineSize, final int aX, final int aY, final int aWidth, final int aHeight )
        {
        }

    public void purge()
        {
        }

    private final DirectGraphics myGraphics;
    }
