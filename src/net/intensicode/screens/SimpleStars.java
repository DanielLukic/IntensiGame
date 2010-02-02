package net.intensicode.screens;

import net.intensicode.core.GameSystem;
import net.intensicode.util.*;

public final class SimpleStars extends ScreenBase
    {
    public static SimpleStars instance()
        {
        if ( theInstance == null ) theInstance = new SimpleStars( 128 );
        return theInstance;
        }

    public SimpleStars( final int aNumberOfStars )
        {
        myNumberOfStars = aNumberOfStars;
        myStarsX = new int[myNumberOfStars];
        myStarsY = new int[myNumberOfStars];
        myStarsZ = new int[myNumberOfStars];

        final Random random = new Random();
        for ( int idx = 0; idx < myNumberOfStars; idx++ )
            {
            myStarsX[ idx ] = random.nextInt() % UNIVERSE_HALF;
            myStarsY[ idx ] = random.nextInt() % UNIVERSE_HALF;
            myStarsZ[ idx ] = random.nextInt() % UNIVERSE_HALF;
            }

        setAnimatedAxes( true, true, true );
        }

    public final void setAnimatedAxes( final boolean aX, final boolean aY, final boolean aZ )
        {
        myMoveX = aX;
        myMoveY = aY;
        myMoveZ = aZ;
        }

    // From ScreenBase

    public final void onInit( final GameSystem aGameSystem ) throws Exception
        {
        super.onInit( aGameSystem );

        myScreenWidth = screen().width();
        myScreenHeight = screen().height();
        myScreenCenterX = myScreenWidth / 2;
        myScreenCenterY = myScreenHeight / 2;
        }

    public final void onControlTick()
        {
        final int tps = timing().ticksPerSecond * 64;
        myAnimCounter += FixedMath.toFixed( Sinus.SIN_TABLE_SIZE ) / tps;

        final int anim = FixedMath.toInt( myAnimCounter );
        if ( anim > Sinus.SIN_TABLE_SIZE ) myAnimCounter = 0;

        myViewerX = myViewerY = myViewerZ = 0;
        if ( myMoveX ) myViewerX = mySinus.sin( anim, UNIVERSE_SIZE / 2 );
        if ( myMoveY ) myViewerY = mySinus.sin( anim * 3, UNIVERSE_SIZE / 5 );
        if ( myMoveZ ) myViewerZ = mySinus.sin( anim * 2, UNIVERSE_SIZE / 3 );
        }

    public final void onDrawFrame()
        {
        final int screenWidth = screen().width();
        final int screenHeight = screen().height();

        final int maxIntensity = 255;
        for ( int idx = 0; idx < myNumberOfStars; idx++ )
            {
            myPosition3d[ 0 ] = myStarsX[ idx ] + myViewerX;
            myPosition3d[ 1 ] = myStarsY[ idx ] + myViewerY;
            myPosition3d[ 2 ] = myStarsZ[ idx ] + myViewerZ;

            final int[] xyz2d = doProjection( myPosition3d );

            final int x2d = xyz2d[ 0 ];
            final int y2d = xyz2d[ 1 ];
            if ( x2d < 0 || x2d >= screenWidth ) continue;
            if ( y2d < 0 || y2d >= screenHeight ) continue;

            final int zNew = xyz2d[ 2 ];
            final int starIntensity = ( zNew * maxIntensity * 2 / 3 ) >> UNIVERSE_SHIFT;
            int intensity = maxIntensity / 5 + starIntensity;
            intensity = Math.max( 0, Math.min( maxIntensity, intensity ) );

            graphics().setColorRGB24( 255 << 24 | intensity << 16 | intensity << 8 | intensity );

            final int starSize = 1 + starIntensity / 64;
            graphics().fillRect( x2d - starSize / 2, y2d - starSize / 2, starSize, starSize );
            }
        }

    // Implementation

    private int[] doProjection( final int[] aPosition3D )
        {
        final int xTemp = aPosition3D[ 0 ] & UNIVERSE_MASK;
        final int yTemp = aPosition3D[ 1 ] & UNIVERSE_MASK;
        final int zTemp = aPosition3D[ 2 ] & UNIVERSE_MASK;

        int z = ( zTemp - UNIVERSE_HALF ) - VIEW_PLANE;
        if ( z == 0 ) z = 1;

        final int x = ( xTemp - UNIVERSE_HALF ) * myScreenWidth;
        final int y = ( yTemp - UNIVERSE_HALF ) * myScreenHeight;
        myPosition2dPlusNewZ[ 0 ] = x / z + myScreenCenterX;
        myPosition2dPlusNewZ[ 1 ] = y / z + myScreenCenterY;
        myPosition2dPlusNewZ[ 2 ] = zTemp;
        return myPosition2dPlusNewZ;
        }



    private int myAnimCounter;

    private int myViewerX;

    private int myViewerY;

    private int myViewerZ;

    private boolean myMoveX;

    private boolean myMoveY;

    private boolean myMoveZ;

    private int myNumberOfStars;

    private final int[] myStarsX;

    private final int[] myStarsY;

    private final int[] myStarsZ;

    private static int myScreenWidth;

    private static int myScreenHeight;

    private static int myScreenCenterX;

    private static int myScreenCenterY;

    private final int[] myPosition3d = new int[3];

    private final int[] myPosition2dPlusNewZ = new int[3];

    private static final Sinus mySinus = Sinus.instance();

    private static SimpleStars theInstance;

    private static final int UNIVERSE_SHIFT = FixedMath.FIXED_SHIFT + 4;

    private static final int UNIVERSE_SIZE = 1 << UNIVERSE_SHIFT;

    private static final int UNIVERSE_HALF = UNIVERSE_SIZE >> 1;

    private static final int UNIVERSE_MASK = UNIVERSE_SIZE - 1;

    private static final int VIEW_PLANE = UNIVERSE_SIZE * 2 / 3;
    }
