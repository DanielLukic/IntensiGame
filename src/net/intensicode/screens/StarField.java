package net.intensicode.screens;

import net.intensicode.core.GameSystem;
import net.intensicode.graphics.*;
import net.intensicode.util.*;


public final class StarField extends ScreenBase
    {
    public StarField( final int aNumberOfStars, final CharGenerator aStarsGenerator )
        {
        final int intensitySteps = aStarsGenerator.charsPerRow * aStarsGenerator.charsPerColumn;
        //#if DEBUG
        Assert.isTrue( "stars generator should have at least one frame", intensitySteps > 0 );
        //#endif

        myStars = new CharData[intensitySteps];
        for ( int idx = 0; idx < myStars.length; idx++ )
            {
            myStars[ idx ] = aStarsGenerator.getCharData( idx );
            }

        myNumberOfStars = aNumberOfStars;
        myStarsX = new float[myNumberOfStars];
        myStarsY = new float[myNumberOfStars];
        myStarsZ = new float[myNumberOfStars];

        final Random random = new Random();
        for ( int idx = 0; idx < myNumberOfStars; idx++ )
            {
            myStarsX[ idx ] = random.nextFloat( UNIVERSE_HALF );
            myStarsY[ idx ] = random.nextFloat( UNIVERSE_HALF );
            myStarsZ[ idx ] = random.nextFloat( UNIVERSE_HALF );
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
        myAnimCounter += Sinus.SIN_TABLE_SIZE / (float) tps;

        final int anim = (int) myAnimCounter;
        if ( anim > Sinus.SIN_TABLE_SIZE ) myAnimCounter = 0;

        myViewerX = myViewerY = myViewerZ = 0;
        if ( myMoveX ) myViewerX = MathExtended.sin( anim ) * UNIVERSE_SIZE / 2;
        if ( myMoveY ) myViewerY = MathExtended.sin( anim * 3 ) * UNIVERSE_SIZE / 5;
        if ( myMoveZ ) myViewerZ = MathExtended.sin( anim * 2 ) * UNIVERSE_SIZE / 3;
        }

    public final void onDrawFrame()
        {
        final int screenWidth = screen().width();
        final int screenHeight = screen().height();

        int maxInt = 0;
        int minInt = 0;

        final int maxIntensity = myStars.length - 1;
        for ( int idx = 0; idx < myNumberOfStars; idx++ )
            {
            myPosition3d[ 0 ] = myStarsX[ idx ] + myViewerX;
            myPosition3d[ 1 ] = myStarsY[ idx ] + myViewerY;
            myPosition3d[ 2 ] = myStarsZ[ idx ] + myViewerZ;

            final float[] xyz2d = doProjection( myPosition3d );

            final float x2d = xyz2d[ 0 ];
            final float y2d = xyz2d[ 1 ];
            if ( x2d < 0 || x2d >= screenWidth ) continue;
            if ( y2d < 0 || y2d >= screenHeight ) continue;

            final float zNew = xyz2d[ 2 ];
            final float starIntensity = ( zNew * maxIntensity ) / UNIVERSE_SIZE;
            final int shiftedIntensity = (int) ( maxIntensity / 3 + starIntensity );
            final int intensity = Math.max( 0, Math.min( maxIntensity, shiftedIntensity ) );
            myStars[ intensity ].blit( graphics(), (int) x2d, (int) y2d );

            maxInt = Math.max( maxInt, intensity );
            minInt = Math.min( minInt, intensity );
            }
        }

    // Implementation

    private float[] doProjection( final float[] aPosition3D )
        {
        final float xTemp = aPosition3D[ 0 ] % UNIVERSE_SIZE;
        final float yTemp = aPosition3D[ 1 ] % UNIVERSE_SIZE;
        final float zTemp = aPosition3D[ 2 ] % UNIVERSE_SIZE;

        float z = ( zTemp - UNIVERSE_HALF ) - VIEW_PLANE;
        if ( z == 0 ) z = 1;

        final float x = ( xTemp - UNIVERSE_HALF ) * myScreenWidth;
        final float y = ( yTemp - UNIVERSE_HALF ) * myScreenHeight;
        myPosition2dPlusNewZ[ 0 ] = x / z + myScreenCenterX;
        myPosition2dPlusNewZ[ 1 ] = y / z + myScreenCenterY;
        myPosition2dPlusNewZ[ 2 ] = zTemp;
        return myPosition2dPlusNewZ;
        }


    private float myAnimCounter;

    private float myViewerX;

    private float myViewerY;

    private float myViewerZ;

    private boolean myMoveX;

    private boolean myMoveY;

    private boolean myMoveZ;

    private int myScreenWidth;

    private int myScreenHeight;

    private int myScreenCenterX;

    private int myScreenCenterY;

    private int myNumberOfStars;

    private final float[] myStarsX;

    private final float[] myStarsY;

    private final float[] myStarsZ;

    private final CharData[] myStars;

    private final float[] myPosition3d = new float[3];

    private final float[] myPosition2dPlusNewZ = new float[3];

    private static final float UNIVERSE_SIZE = 16f;

    private static final float UNIVERSE_HALF = UNIVERSE_SIZE / 2;

    private static final float VIEW_PLANE = UNIVERSE_SIZE * 2 / 3;
    }
