package net.intensicode.util;

public final class GoogleProjection
    {
    // See this link for details about the Google projection:
    // http://cfis.savagexi.com/articles/2006/05/03/google-maps-deconstructed

    public static final int DEFAULT_TILE_SIZE = 256;

    public int tileSize = DEFAULT_TILE_SIZE;

    public GoogleProjection()
        {
        this( DEFAULT_TILE_SIZE );
        }

    public GoogleProjection( final int aTileSize )
        {
        tileSize = aTileSize;
        }

    public final int getX( final double longitude, final int zoom )
        {
        return (int) ( 0.5 + radius( zoom ) * Math.toRadians( longitude ) + falseNorthing( zoom ) );
        }

    public final int getY( final double latitude, final int zoom )
        {
        double sinus = Math.sin( Math.toRadians( latitude ) );
        double pixel = radius( zoom ) / 2.0 * MathExtended.log( ( 1.0 + sinus ) / ( 1.0 - sinus ) );
        return (int) ( 0.5 + ( pixel + falseEasting( zoom ) ) * -1.0 );
        }

    public final int getDistanceX( final double longitude1, final double longitude2, final int zoom )
        {
        return getX( longitude2, zoom ) - getX( longitude1, zoom );
        }

    public final int getDistanceY( final double latitude1, final double latitude2, final int zoom )
        {
        return getY( latitude2, zoom ) - getY( latitude1, zoom );
        }

    public final int getDistance( final double aLon1, final double aLat1, final double aLon2, final double aLat2, final int aZoomLevel )
        {
        final double distX = getDistanceX( aLon1, aLon2, aZoomLevel );
        final double distY = getDistanceY( aLat1, aLat2, aZoomLevel );
        return (int) Math.sqrt( distX * distX + distY * distY );
        }

    public final double getLongitude( final int x, final int zoom )
        {
        double degree = Math.toDegrees( ( x + falseNorthing( zoom ) ) / radius( zoom ) );
        double rotation = Math.floor( ( degree + 180.0 ) / 360.0 );
        return degree - ( rotation * 360.0 );
        }

    public final double getLatitude( final int y, final int zoom )
        {
        final double value = MathExtended.exp( -1.0 * ( y + falseEasting( zoom ) ) / radius( zoom ) );
        return ( -1.0 * Math.toDegrees( ( Math.PI / 2.0 ) - ( 2.0 * MathExtended.atan( value ) ) ) );
        }

    public final double getDeltaLongitude( final int x1, final int x2, final int zoom )
        {
        return getLongitude( x2, zoom ) - getLongitude( x1, zoom );
        }

    public final double getDeltaLatitude( final int y1, final int y2, final int zoom )
        {
        return getLatitude( y2, zoom ) - getLatitude( y1, zoom );
        }

    public final double getDistanceByZoom( final double aDistanceInMeters, final int aZoomLevel )
        {
        return aDistanceInMeters * tileSize / ZOOM_PER_PIXEL_IN_METERS[ aZoomLevel ];
        }

    public final double getDistanceInMeters( final double aDistanceXY, final int aZoomLevel )
        {
        return aDistanceXY * ZOOM_PER_PIXEL_IN_METERS[ aZoomLevel ] / tileSize;
        }

    // Implementation

    private double falseNorthing( final int zoom )
        {
        return circumference( zoom ) / 2.0;
        }

    private double falseEasting( final int zoom )
        {
        return circumference( zoom ) / -2.0;
        }

    private double circumference( final int zoom )
        {
        return tileSize * ( 1 << zoom );
        }

    private double radius( final int zoom )
        {
        return circumference( zoom ) / ( 2.0 * Math.PI );
        }



    private final double[] ZOOM_PER_PIXEL_IN_METERS = { 20088000.56607700,
                                                        10044000.28303850,
                                                        5022000.14151925,
                                                        2511000.07075963,
                                                        1255500.03537981,
                                                        627750.01768991,
                                                        313875.00884495,
                                                        156937.50442248,
                                                        78468.75221124,
                                                        39234.37610562,
                                                        19617.18805281,
                                                        9808.59402640,
                                                        4909.29701320,
                                                        2452.14850660,
                                                        1226.07425330,
                                                        613.03712665,
                                                        306.51856332,
                                                        153.25928166,
                                                        76.62964083,
                                                        38.31482042 };
    }
