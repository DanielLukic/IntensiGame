/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.Color;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.util.FixedMath;
import net.intensicode.util.Sinus;

import javax.microedition.lcdui.Graphics;
import java.util.Random;



public final class SimpleStars extends AbstractScreen
    {
    public static final SimpleStars instance()
        {
        if ( theInstance == null ) theInstance = new SimpleStars( DEFAULT_NUMBER_OF_STARS );
        return theInstance;
        }

    public SimpleStars( final int aNumberOfStars )
        {
        iNumberOfStars = aNumberOfStars;
        iStarsX = new int[iNumberOfStars];
        iStarsY = new int[iNumberOfStars];
        iStarsZ = new int[iNumberOfStars];

        final Random random = new Random();
        for ( int idx = 0; idx < iNumberOfStars; idx++ )
            {
            iStarsX[ idx ] = random.nextInt() % UNIVERSE_HALF;
            iStarsY[ idx ] = random.nextInt() % UNIVERSE_HALF;
            iStarsZ[ idx ] = random.nextInt() % UNIVERSE_HALF;
            }

        SetAxes( true, true, true );
        }

    public final void SetAxes( final boolean aX, final boolean aY, final boolean aZ )
        {
        iMoveX = aX;
        iMoveY = aY;
        iMoveZ = aZ;
        }

    // From AbstractScreen

    public final void onInit( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        super.onInit( aEngine, aScreen );

        iScreenWidth = aScreen.width();
        iScreenHeight = aScreen.height();
        iScreenCenterX = iScreenWidth / 2;
        iScreenCenterY = iScreenHeight / 2;
        }

    public final void onControlTick( final Engine aEngine )
        {
        final int tps = Engine.ticksPerSecond * 64;
        iAnimCounter += FixedMath.toFixed( Sinus.SIN_TABLE_SIZE ) / tps;

        final int anim = FixedMath.toInt( iAnimCounter );
        if ( anim > Sinus.SIN_TABLE_SIZE ) iAnimCounter = 0;

        iViewerX = iViewerY = iViewerZ = 0;
        if ( iMoveX ) iViewerX = mySinus.sin( anim, UNIVERSE_SIZE / 2 );
        if ( iMoveY ) iViewerY = mySinus.sin( anim * 3, UNIVERSE_SIZE / 5 );
        if ( iMoveZ ) iViewerZ = mySinus.sin( anim * 2, UNIVERSE_SIZE / 3 );
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        final int screenWidth = aScreen.width();
        final int screenHeight = aScreen.height();

        final int maxIntensity = Color.COLOR_CHANNEL_VALUES - 1;
        for ( int idx = 0; idx < iNumberOfStars; idx++ )
            {
            myPosition3d[ 0 ] = iStarsX[ idx ] + iViewerX;
            myPosition3d[ 1 ] = iStarsY[ idx ] + iViewerY;
            myPosition3d[ 2 ] = iStarsZ[ idx ] + iViewerZ;

            final int zNew = GetProjected( myPosition3d, myPosition2d );

            final int x2d = myPosition2d[ 0 ];
            final int y2d = myPosition2d[ 1 ];
            if ( x2d < 0 || x2d >= screenWidth ) continue;
            if ( y2d < 0 || y2d >= screenHeight ) continue;

            final int starIntensity = (zNew * maxIntensity * 2 / 3) >> UNIVERSE_SHIFT;
            int intensity = maxIntensity / 5 + starIntensity;
            intensity = Math.max( 0, Math.min( maxIntensity, intensity ) );

            final Graphics gc = aScreen.graphics();
            gc.setColor( 255 << 24 | intensity << 16 | intensity << 8 | intensity );

            final int starSize = 1 + starIntensity / 64;
            gc.fillRect( x2d - starSize / 2, y2d - starSize / 2, starSize, starSize );
            }
        }

    // Implementation

    private final int GetProjected( final int[] aPosition3D, final int[] aPosition2D )
        {
        final int iTempX = aPosition3D[ 0 ] & UNIVERSE_MASK;
        final int iTempY = aPosition3D[ 1 ] & UNIVERSE_MASK;
        final int iTempZ = aPosition3D[ 2 ] & UNIVERSE_MASK;

        int z = (iTempZ - UNIVERSE_HALF) - VIEW_PLANE;
        if ( z == 0 ) z = 1;

        final int x = (iTempX - UNIVERSE_HALF) * iScreenWidth;
        final int y = (iTempY - UNIVERSE_HALF) * iScreenHeight;
        aPosition2D[ 0 ] = x / z + iScreenCenterX;
        aPosition2D[ 1 ] = y / z + iScreenCenterY;

        return iTempZ;
        }



    private int iAnimCounter;

    private int iViewerX;

    private int iViewerY;

    private int iViewerZ;

    private boolean iMoveX;

    private boolean iMoveY;

    private boolean iMoveZ;

    private int iNumberOfStars;

    private final int[] iStarsX;

    private final int[] iStarsY;

    private final int[] iStarsZ;

    private static int iScreenWidth;

    private static int iScreenHeight;

    private static int iScreenCenterX;

    private static int iScreenCenterY;

    private final int[] myPosition3d = new int[3];

    private final int[] myPosition2d = new int[2];

    private static final Sinus mySinus = Sinus.instance();

    private static SimpleStars theInstance;

    private static final int UNIVERSE_SHIFT = FixedMath.FIXED_SHIFT + 4;

    private static final int UNIVERSE_SIZE = 1 << UNIVERSE_SHIFT;

    private static final int UNIVERSE_HALF = UNIVERSE_SIZE >> 1;

    private static final int UNIVERSE_MASK = UNIVERSE_SIZE - 1;

    private static final int VIEW_PLANE = UNIVERSE_SIZE * 2 / 3;

    //#if MIDP1
    //# private static final int DEFAULT_NUMBER_OF_STARS = 32;
    //#else

    private static final int DEFAULT_NUMBER_OF_STARS = 256;

    //#endif
    }
