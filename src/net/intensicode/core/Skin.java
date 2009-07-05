package net.intensicode.core;

import net.intensicode.util.*;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


/**
 * TODO: Describe this!
 */
public final class Skin implements Runnable
    {
    //#if NOKIA
    public static int transparentRGB = 0x000000;
    //#endif

    public static boolean centerRefDefault = false;

    public static boolean cyclicFramesDefault = false;

    public DynamicArray exceptions = new DynamicArray();



    public static final int getCyclicFrameIndex( final int aCyclicRawIndex, final int aGridSize )
        {
        final int perRow = aGridSize;
        final int quarter = perRow - 1;
        final int all = quarter * 4;

        final int index1 = aCyclicRawIndex % all;
        if ( index1 < quarter ) return index1;

        final int index2 = index1 - quarter;
        if ( index2 < quarter ) return quarter + perRow * index2;

        final int index3 = index2 - quarter;
        if ( index3 < quarter ) return perRow * perRow - index3 - 1;

        final int index4 = index3 - quarter;
        if ( index4 < quarter ) return perRow * ( perRow - index4 - 1 );

        throw new IllegalArgumentException();
        }

    public static int[] createCyclicSequence( final Sprite aSprite )
        {
        final int frameCount = aSprite.getRawFrameCount();
        final int gridCount = IntegerSquareRoot.sqrt( frameCount );
        if ( gridCount * gridCount != frameCount ) throw new RuntimeException( "nyi" );
        final int cyclicCount = ( gridCount - 1 ) * 4;
        final int[] cyclicSequence = new int[cyclicCount];
        for ( int idx = 0; idx < cyclicCount; idx++ )
            {
            cyclicSequence[ idx ] = getCyclicFrameIndex( idx, gridCount );
            }
        return cyclicSequence;
        }

    public Skin( final ResourceLoader aResourceLoader, final Configuration aSkinConfiguration )
        {
        myResourceLoader = aResourceLoader;
        mySkinConfiguration = aSkinConfiguration;
        myImageIDs = mySkinConfiguration.readList( "images", "", "," );

        myLoadThread = new Thread( this );
        myLoadThread.start();

        //#if NOKIA
        transparentRGB = mySkinConfiguration.readHex( "transparentRGB", transparentRGB );
        //#endif
        centerRefDefault = mySkinConfiguration.readBoolean( "center_ref_default", centerRefDefault );
        cyclicFramesDefault = mySkinConfiguration.readBoolean( "cyclic_frames_default", cyclicFramesDefault );
        }

    public final void destroy()
        {
        myLoadThread = null;
        synchronized ( myLoadQueue )
            {
            myLoadQueue.notifyAll();
            }
        }

    public final boolean allImagesLoaded()
        {
        for ( int idx = 0; idx < myImageIDs.length; idx++ )
            {
            if ( isImageLoaded( myImageIDs[ idx ] ) == false ) return false;
            }
        return true;
        }

    public final boolean isImageLoaded( final String aImageID )
        {
        synchronized ( myLoadQueue )
            {
            if ( myCachedImages.containsKey( aImageID ) ) return true;
            if ( myLoadQueue.contains( aImageID ) ) return false;

            myLoadQueue.addElement( aImageID );
            myLoadQueue.notifyAll();
            }
        return false;
        }

    public final void purgeImage( final String aImageID, final boolean aRightNow )
        {
        //#if DEBUG
        if ( myCachedImages.get( aImageID ) == null )
            {
            Log.debug( "Trying to purge non-cached image {}", aImageID );
            }
        else
            {
            Log.debug( "Purging image {}", aImageID );
            }
        //#endif

        myCachedImages.remove( aImageID );
        if ( aRightNow ) System.gc();
        }

    public final Image image( final String aImageID ) throws IOException
        {
        if ( myCachedImages.containsKey( aImageID ) == false )
            {
            final Image image = loadImage( aImageID );
            myCachedImages.put( aImageID, image );
            }
        return (Image) myCachedImages.get( aImageID );
        }

    public final Sprite sprite( final String aImageID ) throws IOException
        {
        if ( myCachedSprites.containsKey( aImageID ) == false )
            {
            final Image image = image( aImageID );
            final int charsPerRow = mySkinConfiguration.readInt( aImageID, "chars_per_row", SPRITE_CHARS_PER_ROW );
            final int charsPerCol = mySkinConfiguration.readInt( aImageID, "chars_per_col", SPRITE_CHARS_PER_COL );
            final int frameWidth = image.getWidth() / charsPerRow;
            final int frameHeight = image.getHeight() / charsPerCol;
            final Sprite sprite = new Sprite( image, frameWidth, frameHeight );

            final boolean cyclicFrames = mySkinConfiguration.readBoolean( aImageID, "cyclic_frames", cyclicFramesDefault );
            if ( cyclicFrames )
                {
                final int[] sequence = createCyclicSequence( sprite );
                sprite.setFrameSequence( sequence );
                }

            final boolean centeredReference = mySkinConfiguration.readBoolean( aImageID, "center_ref", centerRefDefault );
            if ( centeredReference )
                {
                sprite.defineReferencePixel( frameWidth / 2, frameHeight / 2 );
                }

            myCachedSprites.put( aImageID, sprite );
            }
        return (Sprite) myCachedSprites.get( aImageID );
        }

    public final CharGen charGen( final String aImageID ) throws IOException
        {
        if ( myCachedCharGens.containsKey( aImageID ) == false )
            {
            final Image charData = image( aImageID );
            final int charsPerRow = mySkinConfiguration.readInt( aImageID, "chars_per_row", CHARGEN_CHARS_PER_ROW );
            final int charsPerCol = mySkinConfiguration.readInt( aImageID, "chars_per_col", CHARGEN_CHARS_PER_COL );
            myCachedCharGens.put( aImageID, CharGen.fromLayout( charData, charsPerRow, charsPerCol ) );
            }
        return (CharGen) myCachedCharGens.get( aImageID );
        }

    public final BitmapFontGen font( final String aFontID ) throws IOException
        {
        if ( myCachedFontGens.containsKey( aFontID ) == false )
            {
            final CharGen charGen = charGen( aFontID );

            myNameBuffer.setLength( 0 );
            myNameBuffer.append( "/" );
            myNameBuffer.append( aFontID );
            myNameBuffer.append( ".dst" );

            final byte[] widths = myResourceLoader.loadData( myNameBuffer.toString() );
            if ( widths == null )
                {
                myCachedFontGens.put( aFontID, new BitmapFontGen( charGen ) );
                }
            else
                {
                myCachedFontGens.put( aFontID, new BitmapFontGen( charGen, widths ) );
                }
            }
        return (BitmapFontGen) myCachedFontGens.get( aFontID );
        }

    // Implementation

    private final Image loadImage( final String aImageID ) throws IOException
        {
        try
            {
            if ( aImageID.endsWith( ".png" ) ) return myResourceLoader.loadImage( aImageID );

            myNameBuffer.setLength( 0 );
            myNameBuffer.append( "/" );
            myNameBuffer.append( aImageID );
            myNameBuffer.append( ".png" );
            return myResourceLoader.loadImage( myNameBuffer.toString() );
            }
        catch ( final NullPointerException e )
            {
            final String msg = Log.format( "Image not found: {}", new Object[]{ aImageID } );
            throw new IOException( msg );
            }
        }

    // From Runnable

    public final void run()
        {
        while ( myLoadThread != null )
            {
            final Object handle;

            synchronized ( myLoadQueue )
                {
                try
                    {
                    while ( myLoadQueue.size() == 0 && myLoadThread != null )
                        {
                        myLoadQueue.wait();
                        }
                    }
                catch ( final InterruptedException e )
                    {
                    break;
                    }
                if ( myLoadThread == null ) break;

                handle = myLoadQueue.elementAt( 0 );
                }

            try
                {
                final Image image = loadImage( handle.toString() );
                synchronized ( myLoadQueue )
                    {
                    myCachedImages.put( handle, image );
                    myLoadQueue.removeElement( handle );
                    }
                }
            catch ( Throwable t )
                {
                // To avoid stalling..
                if ( myDummyImage == null ) myDummyImage = Image.createImage( 8, 8 );
                myCachedImages.put( handle, myDummyImage );
                myLoadQueue.removeElement( handle );

                //#if DEBUG
                t.printStackTrace();
                //#endif

                exceptions.add( t );
                }
            }
        }



    private Image myDummyImage;

    private Thread myLoadThread;

    private final String[] myImageIDs;

    private final ResourceLoader myResourceLoader;

    private final Configuration mySkinConfiguration;

    private final Vector myLoadQueue = new Vector();

    private final Hashtable myCachedImages = new Hashtable();

    private final Hashtable myCachedSprites = new Hashtable();

    private final Hashtable myCachedCharGens = new Hashtable();

    private final Hashtable myCachedFontGens = new Hashtable();

    private final StringBuffer myNameBuffer = new StringBuffer();


    private static final int SPRITE_CHARS_PER_ROW = 1;

    private static final int SPRITE_CHARS_PER_COL = 1;

    private static final int CHARGEN_CHARS_PER_ROW = 16;

    private static final int CHARGEN_CHARS_PER_COL = 8;
    }
