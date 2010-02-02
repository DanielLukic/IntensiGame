package net.intensicode.graphics;

import net.intensicode.core.*;
import net.intensicode.util.Position;

public final class SpriteGenerator
    {
    public SpriteGenerator( final ImageResource aImage, final int aFrameWidth, final int aFrameHeight )
        {
        myFrameGenerator = CharGenerator.fromSize( aImage, aFrameWidth, aFrameHeight );
        myFrameWidth = aFrameWidth;
        myFrameHeight = aFrameHeight;
        myRawFrameCount = myFrameGenerator.getNumberOfFrames();
        }

    public final int getWidth()
        {
        return myFrameWidth;
        }

    public final int getHeight()
        {
        return myFrameHeight;
        }

    public int getRawFrameCount()
        {
        return myRawFrameCount;
        }

    public final int getFrameSequenceLength()
        {
        return myFrameSequence.length;
        }

    public final void setFrameSequence( final int[] aFrameSequence )
        {
        myFrameSequence = new int[aFrameSequence.length];
        for ( int idx = 0; idx < myFrameSequence.length; idx++ )
            {
            myFrameSequence[ idx ] = aFrameSequence[ idx ];
            }
        }

    public final void setFrame( final int aFrameIndex )
        {
        if ( myFrameSequence == null ) myFrameIndex = aFrameIndex;
        else myFrameIndex = myFrameSequence[ aFrameIndex ];
        }

    public final void defineReferencePixel( final int aOffsetX, final int aOffsetY )
        {
        myRefOffset.x = aOffsetX;
        myRefOffset.y = aOffsetY;
        }

    public final void paint( final DirectGraphics aGraphics, final int aX, final int aY )
        {
        final int x = aX - myRefOffset.x;
        final int y = aY - myRefOffset.y;
        myFrameGenerator.blit( aGraphics, x, y, myFrameIndex );
        }

    private int myFrameIndex;

    private int[] myFrameSequence;

    private int myRawFrameCount;

    private final int myFrameWidth;

    private final int myFrameHeight;

    private final CharGenerator myFrameGenerator;

    private final Position myRefOffset = new Position();
    }
