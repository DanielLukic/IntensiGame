package net.intensicode.util;

public final class ColorUtils
    {
    public static final int COLOR_CHANNEL_BITS = 8;

    public static final int COLOR_CHANNEL_VALUES = 1 << COLOR_CHANNEL_BITS;

    public static final int BLUE_SHIFT = COLOR_CHANNEL_BITS * 0;

    public static final int GREEN_SHIFT = COLOR_CHANNEL_BITS * 1;

    public static final int RED_SHIFT = COLOR_CHANNEL_BITS * 2;

    public static final int ALPHA_SHIFT = COLOR_CHANNEL_BITS * 3;

    public static final int OPAQUE_COLOR = ( COLOR_CHANNEL_VALUES - 1 ) << ALPHA_SHIFT;


    public static final int BLACK = 0xFF000000;

    public static final int RED = 0xFFFF0000;

    public static final int GREEN = 0xFF00FF00;

    public static final int BLUE = 0xFF0000FF;

    public static final int WHITE = 0xFFFFFFFF;



    public static int alpha( final int aARGB32 )
        {
        return ( aARGB32 >> ALPHA_SHIFT ) & 0xFF;
        }

    public static int red( final int aARGB32 )
        {
        return ( aARGB32 >> RED_SHIFT ) & 0xFF;
        }

    public static int green( final int aARGB32 )
        {
        return ( aARGB32 >> GREEN_SHIFT ) & 0xFF;
        }

    public static int blue( final int aARGB32 )
        {
        return ( aARGB32 >> BLUE_SHIFT ) & 0xFF;
        }

    public static int rgb( final int aRed8, final int aGreen8, final int aBlue8 )
        {
        return OPAQUE_COLOR
                | aRed8 << RED_SHIFT
                | aGreen8 << GREEN_SHIFT
                | aBlue8 << BLUE_SHIFT;
        }

    public static int argb( final int aAlpha8, final int aRed8, final int aGreen8, final int aBlue8 )
        {
        return OPAQUE_COLOR << 8
                | aAlpha8 << ALPHA_SHIFT
                | aRed8 << RED_SHIFT
                | aGreen8 << GREEN_SHIFT
                | aBlue8 << BLUE_SHIFT;
        }

    public static int alphatize( final int aARGB32, final int aAlpha )
        {
        final int alpha = ColorUtils.alpha( aARGB32 );
        final int red = ColorUtils.red( aARGB32 ) * aAlpha / 255;
        final int green = ColorUtils.green( aARGB32 ) * aAlpha / 255;
        final int blue = ColorUtils.blue( aARGB32 ) * aAlpha / 255;
        return ColorUtils.argb( alpha, red, green, blue );
        }

    public static int darker( final int aARGB32 )
        {
        return darker( aARGB32, 220 );
        }

    public static int darker( final int aARGB32, final int aValue8 )
        {
        final int red = ColorUtils.red( aARGB32 ) * aValue8 / 255;
        final int green = ColorUtils.green( aARGB32 ) * aValue8 / 255;
        final int blue = ColorUtils.blue( aARGB32 ) * aValue8 / 255;
        return ColorUtils.rgb( red, green, blue );
        }

    private ColorUtils()
        {
        }
    }
