package net.intensicode.core;

import net.intensicode.graphics.*;
import net.intensicode.util.*;

import java.io.IOException;
import java.util.*;

public final class SkinManager implements Runnable
    {
    //#if NOKIA
    public static int transparentRGB = 0x000000;
    //#endif

    public static boolean centerRefDefault = false;

    public static boolean cyclicFramesDefault = false;

    public DynamicArray exceptionsFromLoaderThread = new DynamicArray();

    public ImageResource imageNotFound;


    public static int getCyclicFrameIndex( final int aCyclicRawIndex, final int aGridSize )
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

    public static int[] createCyclicSequence( final SpriteGenerator aSprite )
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

    public SkinManager( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;

        myLoaderThread = new Thread( this );
        myLoaderThread.start();
        }

    public final void apply( final Configuration aConfiguration )
        {
        mySkinConfiguration = aConfiguration;
        myImageIDs = mySkinConfiguration.readList( "images", "", "," );

        //#if NOKIA
        transparentRGB = mySkinConfiguration.readHex( "transparentRGB", transparentRGB );
        //#endif

        centerRefDefault = mySkinConfiguration.readBoolean( "center_ref_default", centerRefDefault );
        cyclicFramesDefault = mySkinConfiguration.readBoolean( "cyclic_frames_default", cyclicFramesDefault );
        }

    public final void destroy()
        {
        myLoaderThread = null;
        synchronized ( myLoadQueue )
            {
            myLoadQueue.notifyAll();
            }
        purgeAll();
        }

    public final boolean allImagesLoaded()
        {
        if ( myImageIDs == null ) myImageIDs = new String[0];
        for ( int idx = 0; idx < myImageIDs.length; idx++ )
            {
            if ( !isImageLoaded( myImageIDs[ idx ], true ) ) return false;
            }
        return true;
        }

    public final boolean isImageLoaded( final String aImageID, final boolean aTriggerLoading )
        {
        synchronized ( myLoadQueue )
            {
            if ( myCachedImages.containsKey( aImageID ) ) return true;
            if ( myLoadQueue.contains( aImageID ) ) return false;

            if ( aTriggerLoading )
                {
                myLoadQueue.addElement( aImageID );
                myLoadQueue.notifyAll();
                }
            }
        return false;
        }

    public final void purgeAll()
        {
        final Enumeration keys = myCachedImages.keys();
        while ( keys.hasMoreElements() )
            {
            purgeImage( (String) keys.nextElement(), false );
            }
        System.gc();
        }

    public final void purgeImage( final String aImageID, final boolean aRightNow )
        {
        //#if DEBUG
        Log.debug( "Skin purging image data for {}", aImageID );
        //#endif

        if ( myCachedImages.containsKey( aImageID ) )
            {
            final ImageResource resource = (ImageResource) myCachedImages.get( aImageID );
            resource.purge();
            }

        myCachedCharGens.remove( aImageID );
        myCachedFontGens.remove( aImageID );
        myCachedSprites.remove( aImageID );
        myCachedImages.remove( aImageID );
        if ( aRightNow ) System.gc();
        }

    public final void purgeImage( final Object aSkinObject, final boolean aRightNow )
        {
        //#if DEBUG
        Log.debug( "Skin purging data for {}", aSkinObject );
        //#endif

        final String id = findObjectID( aSkinObject );
        purgeImage( id, aRightNow );
        }

    public final ImageResource image( final String aImageID ) throws IOException
        {
        if ( !myCachedImages.containsKey( aImageID ) )
            {
            final ImageResource image = loadImage( aImageID );
            myCachedImages.put( aImageID, image );
            }
        return (ImageResource) myCachedImages.get( aImageID );
        }

    public final SpriteGenerator sprite( final String aImageID ) throws IOException
        {
        if ( !myCachedSprites.containsKey( aImageID ) )
            {
            final ImageResource image = image( aImageID );
            final int charsPerRow = mySkinConfiguration.readInt( aImageID, "chars_per_row", SPRITE_CHARS_PER_ROW );
            final int charsPerCol = mySkinConfiguration.readInt( aImageID, "chars_per_col", SPRITE_CHARS_PER_COL );
            final int frameWidth = image.getWidth() / charsPerRow;
            final int frameHeight = image.getHeight() / charsPerCol;
            final SpriteGenerator sprite = new SpriteGenerator( image, frameWidth, frameHeight );

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
        return (SpriteGenerator) myCachedSprites.get( aImageID );
        }

    public final CharGenerator charGen( final String aImageID ) throws IOException
        {
        if ( !myCachedCharGens.containsKey( aImageID ) )
            {
            final ImageResource charData = image( aImageID );
            final int charsPerRow = mySkinConfiguration.readInt( aImageID, "chars_per_row", CHARGEN_CHARS_PER_ROW );
            final int charsPerCol = mySkinConfiguration.readInt( aImageID, "chars_per_col", CHARGEN_CHARS_PER_COL );
            myCachedCharGens.put( aImageID, CharGenerator.fromLayout( charData, charsPerRow, charsPerCol ) );
            }
        return (CharGenerator) myCachedCharGens.get( aImageID );
        }

    public final BitmapFontGenerator font( final String aFontID ) throws IOException
        {
        if ( !myCachedFontGens.containsKey( aFontID ) )
            {
            final CharGenerator charGen = charGen( aFontID );

            final String charWidthsResourceName = getCharWidthsResourcePath( aFontID );

            final boolean monospaced = mySkinConfiguration.readBoolean( aFontID, "monospaced", false );
            //#if DEBUG
            Log.debug( "Font {} monospaced? " + monospaced, aFontID );
            //#endif
            if ( !monospaced && resources().doesResourceExist( charWidthsResourceName ) )
                {
                final byte[] widths = resources().loadData( charWidthsResourceName );
                myCachedFontGens.put( aFontID, new BitmapFontGenerator( charGen, widths ) );
                }
            else
                {
                myCachedFontGens.put( aFontID, new BitmapFontGenerator( charGen ) );
                }
            }
        return (BitmapFontGenerator) myCachedFontGens.get( aFontID );
        }

    // From Runnable

    public final void run()
        {
        while ( myLoaderThread != null )
            {
            final Object handle;

            synchronized ( myLoadQueue )
                {
                try
                    {
                    while ( myLoadQueue.size() == 0 && myLoaderThread != null )
                        {
                        myLoadQueue.wait();
                        }
                    }
                catch ( final InterruptedException e )
                    {
                    break;
                    }
                if ( myLoaderThread == null ) break;

                handle = myLoadQueue.elementAt( 0 );
                }

            try
                {
                final ImageResource image = loadImage( handle.toString() );
                synchronized ( myLoadQueue )
                    {
                    if ( image != null ) myCachedImages.put( handle, image );
                    myLoadQueue.removeElement( handle );
                    }
                }
            catch ( final Throwable t )
                {
                // To avoid stalling..
                if ( imageNotFound == null ) imageNotFound = resources().createImageResource( 8, 8 );
                myCachedImages.put( handle, imageNotFound );
                myLoadQueue.removeElement( handle );

                //#if DEBUG
                Log.error( t );
                //#endif

                exceptionsFromLoaderThread.add( t );
                }
            }
        }

    // Implementation

    private String findObjectID( final Object aSkinObject )
        {
        if ( myCachedImages.contains( aSkinObject ) ) return findId( myCachedImages, aSkinObject );
        if ( myCachedCharGens.contains( aSkinObject ) ) return findId( myCachedCharGens, aSkinObject );
        if ( myCachedFontGens.contains( aSkinObject ) ) return findId( myCachedFontGens, aSkinObject );
        if ( myCachedSprites.contains( aSkinObject ) ) return findId( myCachedSprites, aSkinObject );
        throw new IllegalArgumentException( String.valueOf( aSkinObject ) );
        }

    private String findId( final Hashtable aHashtable, final Object aObject )
        {
        final Enumeration keys = aHashtable.keys();
        while ( keys.hasMoreElements() )
            {
            final Object key = keys.nextElement();
            if ( aHashtable.get( key ) == aObject ) return (String) key;
            }
        throw new IllegalArgumentException( String.valueOf( aObject ) );
        }

    private String getCharWidthsResourcePath( final String aFontID )
        {
        myNameBuffer.setLength( 0 );
        myNameBuffer.append( aFontID );
        myNameBuffer.append( ".dst" );

        return myNameBuffer.toString();
        }

    private ImageResource loadImage( final String aImageID ) throws IOException
        {
        try
            {
            if ( aImageID.endsWith( ".png" ) ) return resources().loadImageResource( aImageID );

            myNameBuffer.setLength( 0 );
            myNameBuffer.append( aImageID );
            myNameBuffer.append( ".png" );

            return resources().loadImageResource( myNameBuffer.toString() );
            }
        catch ( final NullPointerException e )
            {
            //#if DEBUG
            Log.debug( "ImageResource not found: {}", aImageID );
            //#endif
            if ( imageNotFound == null ) imageNotFound = resources().createImageResource( 8, 8 );
            return imageNotFound;
            }
        }

    private ResourcesManager resources()
        {
        return myGameSystem.resources;
        }


    private String[] myImageIDs;

    private Thread myLoaderThread;

    private Configuration mySkinConfiguration = Configuration.NULL_CONFIGURATION;

    private final GameSystem myGameSystem;

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
