package net.intensicode;

import net.intensicode.util.Log;

import javax.microedition.lcdui.Canvas;

public final class PlatformConfiguration
    {
    public static final String PLATFORM_SUN = "sun";

    public static final String PLATFORM_MOTOROLA = "motorola";

    public static final String PLATFORM_NOKIA = "nokia";

    public static final String PLATFORM_SONY_ERICSSON = "sonyericsson";

    public static final String PLATFORM_SIEMENS = "siemens";

    public static final String PLATFORM_SAMSUNG = "samsung";

    public static final String PLATFORM_LG = "lg";

    public static final String PLATFORM_INTENT = "intent";

    public static final String PLATFORM_NOT_DEFINED = "NA";

    public static final int KEY_UP = Canvas.UP;

    public static final int KEY_DOWN = Canvas.DOWN;

    public static final int KEY_LEFT = Canvas.LEFT;

    public static final int KEY_RIGHT = Canvas.RIGHT;

    public static final int KEY_FIRE = Canvas.FIRE;

    public static final int KEY_GAME_A = Canvas.GAME_A;

    public static final int KEY_GAME_B = Canvas.GAME_B;

    public static final int KEY_GAME_C = Canvas.GAME_C;

    public static final int KEY_GAME_D = Canvas.GAME_D;

    public static final int KEY_SOFT_LEFT = -6;

    public static final int KEY_SOFT_RIGHT = -7;

    public static final int KEY_DELETE = -8;

    public static final int KEY_BACK = -9;

    public static final int KEY_PENCIL = -10;

    public static final int KEY_0 = Canvas.KEY_NUM0;

    public static final int KEY_1 = Canvas.KEY_NUM1;

    public static final int KEY_2 = Canvas.KEY_NUM2;

    public static final int KEY_3 = Canvas.KEY_NUM3;

    public static final int KEY_4 = Canvas.KEY_NUM4;

    public static final int KEY_5 = Canvas.KEY_NUM5;

    public static final int KEY_6 = Canvas.KEY_NUM6;

    public static final int KEY_7 = Canvas.KEY_NUM7;

    public static final int KEY_8 = Canvas.KEY_NUM8;

    public static final int KEY_9 = Canvas.KEY_NUM9;

    public static final int KEY_POUND = Canvas.KEY_POUND;

    public static final int KEY_STAR = Canvas.KEY_STAR;

    public static final int KEY_UNDEFINED = 0;

    public final String platformName;

    public final int softKeyLeft;

    public final int softKeyRight;

    public final int softKeyDelete;

    public final int softKeyBack;



    public PlatformConfiguration( final Canvas aCanvas )
        {
        platformName = getPlatform( aCanvas );
        softKeyLeft = getLeftSoftkeyCode( aCanvas );
        softKeyRight = getRightSoftkeyCode( aCanvas );
        softKeyDelete = getDeleteKeyCode( aCanvas );
        softKeyBack = getBackKeyCode();
        //#if DEBUG
        Log.debug( "platformName {}", platformName );
        Log.debug( "softKeyLeft {}", softKeyLeft );
        Log.debug( "softKeyRight {}", softKeyRight );
        Log.debug( "softKeyDelete {}", softKeyDelete );
        Log.debug( "softKeyBack {}", softKeyBack );
        //#endif
        }

    public int adoptKeyCode( final Canvas aCanvas, final int keyCode )
        {
        switch ( keyCode )
            {
            case Canvas.KEY_NUM0:
                return KEY_0;
            case Canvas.KEY_NUM1:
                return KEY_1;
            case Canvas.KEY_NUM2:
                return KEY_2;
            case Canvas.KEY_NUM3:
                return KEY_3;
            case Canvas.KEY_NUM4:
                return KEY_4;
            case Canvas.KEY_NUM5:
                return KEY_5;
            case Canvas.KEY_NUM6:
                return KEY_6;
            case Canvas.KEY_NUM7:
                return KEY_7;
            case Canvas.KEY_NUM8:
                return KEY_8;
            case Canvas.KEY_NUM9:
                return KEY_9;
            case Canvas.KEY_STAR:
                return KEY_STAR;
            case Canvas.KEY_POUND:
                return KEY_POUND;
            default:
                if ( keyCode == softKeyLeft ) return KEY_SOFT_LEFT;
                else if ( keyCode == softKeyRight ) return KEY_SOFT_RIGHT;
                else if ( keyCode == softKeyDelete || keyCode == DELETE_KEY_ME4SE ) return KEY_DELETE;
                else if ( keyCode == softKeyBack ) return KEY_BACK;
                else if ( keyCode == PENCIL_KEY_NOKIA ) return KEY_PENCIL;
                else
                    {
                    try
                        {
                        final int gameAction = aCanvas.getGameAction( keyCode );
                        if ( gameAction == Canvas.UP ) return KEY_UP;
                        else if ( gameAction == Canvas.DOWN ) return KEY_DOWN;
                        else if ( gameAction == Canvas.LEFT ) return KEY_LEFT;
                        else if ( gameAction == Canvas.RIGHT ) return KEY_RIGHT;
                        else if ( gameAction == Canvas.FIRE ) return KEY_FIRE;
                        else if ( gameAction == Canvas.GAME_A ) return KEY_GAME_A;
                        else if ( gameAction == Canvas.GAME_B ) return KEY_GAME_B;
                        else if ( gameAction == Canvas.GAME_C ) return KEY_GAME_C;
                        else if ( gameAction == Canvas.GAME_D ) return KEY_GAME_D;
                        }
                    catch ( IllegalArgumentException e )
                        {
                        }
                    }
                break;
            }

        if ( keyCode >= 32 && keyCode < 128 ) return keyCode;

        //#if DEBUG
        Log.debug( "platform keyCode: {}", keyCode );
        //#endif

        return KEY_UNDEFINED;
        }

    // Implementation

    private String getPlatform( final Canvas aCanvas )
        {
        // Detecting NOKIA / SonyEricsson / Sun WTK emulator / Intent
        try
            {
            final String currentPlatform = System.getProperty( "microedition.platform" );
            if ( currentPlatform.indexOf( "Nokia" ) != -1 ) return PLATFORM_NOKIA;
            else if ( currentPlatform.indexOf( "SonyEricsson" ) != -1 ) return PLATFORM_SONY_ERICSSON;
            else if ( currentPlatform.indexOf( "SunMicrosystems" ) != -1 ) return PLATFORM_SUN;
            else if ( currentPlatform.indexOf( "intent" ) != -1 ) return PLATFORM_INTENT;
            }
        catch ( final Throwable ex )
            {
            try
                {
                Class.forName( "com.nokia.mid.ui.FullCanvas" );
                return PLATFORM_NOKIA;
                }
            catch ( final Throwable ex2 )
                {
                }
            }
        // Detecting SAMSUNG
        try
            {
            Class.forName( "com.samsung.util.Vibration" );
            return PLATFORM_SAMSUNG;
            }
        catch ( final Throwable ex )
            {
            }
        // Detecting MOTOROLA
        try
            {
            Class.forName( "com.motorola.multimedia.Vibrator" );
            return PLATFORM_MOTOROLA;
            }
        catch ( final Throwable ex )
            {
            try
                {
                Class.forName( "com.motorola.graphics.j3d.Effect3D" );
                return PLATFORM_MOTOROLA;
                }
            catch ( final Throwable ex2 )
                {
                try
                    {
                    Class.forName( "com.motorola.multimedia.Lighting" );
                    return PLATFORM_MOTOROLA;
                    }
                catch ( final Throwable ex3 )
                    {
                    try
                        {
                        Class.forName( "com.motorola.multimedia.FunLight" );
                        return PLATFORM_MOTOROLA;
                        }
                    catch ( final Throwable ex4 )
                        {
                        }
                    }
                }
            }
        try
            {
            if ( aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA ).toUpperCase().indexOf( SOFT_WORD ) > -1 )
                {
                return PLATFORM_MOTOROLA;
                }
            }
        catch ( Throwable e )
            {
            try
                {
                if ( aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA1 ).toUpperCase().indexOf( SOFT_WORD ) > -1 )
                    {
                    return PLATFORM_MOTOROLA;
                    }
                }
            catch ( Throwable e1 )
                {
                try
                    {
                    if ( aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA2 ).toUpperCase().indexOf( SOFT_WORD ) > -1 )
                        {
                        return PLATFORM_MOTOROLA;
                        }
                    }
                catch ( Throwable e2 )
                    {
                    }
                }
            }
        // Detecting SIEMENS
        try
            {
            Class.forName( "com.siemens.mp.io.File" );
            return PLATFORM_SIEMENS;
            }
        catch ( final Throwable ex )
            {
            }
        // Detecting LG
        try
            {
            Class.forName( "mmpp.media.MediaPlayer" );
            return PLATFORM_LG;
            }
        catch ( final Throwable ex )
            {
            try
                {
                Class.forName( "mmpp.phone.Phone" );
                return PLATFORM_LG;
                }
            catch ( final Throwable ex1 )
                {
                try
                    {
                    Class.forName( "mmpp.lang.MathFP" );
                    return PLATFORM_LG;
                    }
                catch ( final Throwable ex2 )
                    {
                    try
                        {
                        Class.forName( "mmpp.media.BackLight" );
                        return PLATFORM_LG;
                        }
                    catch ( final Throwable ex3 )
                        {
                        }
                    }
                }
            }
        return PLATFORM_NOT_DEFINED;
        }

    private int getLeftSoftkeyCode( final Canvas aCanvas )
        {
        int keyCode = 0;
        try
            {
            if ( platformName.equals( PLATFORM_MOTOROLA ) )
                {
                String softkeyLeftMoto = "";
                try
                    {
                    softkeyLeftMoto = aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA ).toUpperCase();
                    }
                catch ( IllegalArgumentException ilae )
                    {
                    }
                String softkeyLeftMoto1 = "";
                try
                    {
                    softkeyLeftMoto1 = aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA1 ).toUpperCase();
                    }
                catch ( IllegalArgumentException ilae )
                    {
                    }
                String softkeyLeftMoto2 = "";
                try
                    {
                    softkeyLeftMoto2 = aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA2 ).toUpperCase();
                    }
                catch ( IllegalArgumentException ilae )
                    {
                    }
                if ( softkeyLeftMoto.indexOf( SOFT_WORD ) >= 0 && softkeyLeftMoto.indexOf( "1" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA;
                    }
                else if ( softkeyLeftMoto1.indexOf( SOFT_WORD ) >= 0 && softkeyLeftMoto1.indexOf( "1" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA1;
                    }
                else if ( softkeyLeftMoto2.indexOf( SOFT_WORD ) >= 0 && softkeyLeftMoto2.indexOf( "1" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA2;
                    }
                else if ( softkeyLeftMoto.indexOf( SOFT_WORD ) >= 0 && softkeyLeftMoto.indexOf( "LEFT" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA;
                    }
                else if ( softkeyLeftMoto1.indexOf( SOFT_WORD ) >= 0 && softkeyLeftMoto1.indexOf( "LEFT" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA1;
                    }
                else if ( softkeyLeftMoto2.indexOf( SOFT_WORD ) >= 0 && softkeyLeftMoto2.indexOf( "LEFT" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA2;
                    }
                }
            else if ( platformName.equals( PLATFORM_NOKIA ) )
                {
                return SOFT_KEY_LEFT_NOKIA;
                }
            else if ( platformName.equals( PLATFORM_SAMSUNG ) )
                {
                return SOFT_KEY_LEFT_SAMSUNG;
                }
            else if ( platformName.equals( PLATFORM_SIEMENS ) )
                {
                String leftKeySiemensName = aCanvas.getKeyName( SOFT_KEY_LEFT_SIEMENS ).toUpperCase();
                if ( leftKeySiemensName.indexOf( SOFT_WORD ) >= 0 )
                    {
                    if ( leftKeySiemensName.indexOf( "1" ) >= 0 )
                        {
                        return SOFT_KEY_LEFT_SIEMENS;
                        }
                    else if ( leftKeySiemensName.indexOf( "LEFT" ) >= 0 )
                        {
                        return SOFT_KEY_LEFT_SIEMENS;
                        }
                    }
                }
            else if ( platformName.equals( PLATFORM_SONY_ERICSSON ) )
                {
                return SOFT_KEY_LEFT_SE;
                }
            else if ( platformName.equals( PLATFORM_SUN ) )
                {
                return SOFT_KEY_LEFT_DEFAULT;
                }
            else if ( platformName.equals( PLATFORM_INTENT ) )
                {
                return SOFT_KEY_LEFT_INTENT;
                }
            else if ( platformName.equals( PLATFORM_NOT_DEFINED ) )
                {
                for ( int i = -125; i <= 125; i++ )
                    {
                    if ( i == 0 )
                        {
                        i++;
                        }
                    final String s = aCanvas.getKeyName( i ).toUpperCase();
                    if ( s.indexOf( SOFT_WORD ) >= 0 )
                        {
                        if ( s.indexOf( "1" ) >= 0 )
                            {
                            keyCode = i;
                            break;
                            }
                        if ( s.indexOf( "LEFT" ) >= 0 )
                            {
                            keyCode = i;
                            break;
                            }
                        }
                    }
                }
            if ( keyCode == 0 ) return SOFT_KEY_LEFT_DEFAULT;
            }
        catch ( final Throwable iaEx )
            {
            return SOFT_KEY_LEFT_DEFAULT;
            }
        return keyCode;
        }

    private int getRightSoftkeyCode( final Canvas aCanvas )
        {
        int keyCode = 0;
        try
            {
            if ( platformName.equals( PLATFORM_MOTOROLA ) )
                {
                String rightSoftMoto1 = "";
                try
                    {
                    rightSoftMoto1 = aCanvas.getKeyName( SOFT_KEY_LEFT_MOTOROLA1 ).toUpperCase();
                    }
                catch ( IllegalArgumentException ilae )
                    {
                    }
                String rightSoftMoto = "";
                try
                    {
                    rightSoftMoto = aCanvas.getKeyName( SOFT_KEY_RIGHT_MOTOROLA ).toUpperCase();
                    }
                catch ( IllegalArgumentException ilae )
                    {
                    }
                String rightSoftMoto2 = "";
                try
                    {
                    rightSoftMoto2 = aCanvas.getKeyName( SOFT_KEY_RIGHT_MOTOROLA1 ).toUpperCase();
                    }
                catch ( IllegalArgumentException ilae )
                    {
                    }
                if ( rightSoftMoto.indexOf( SOFT_WORD ) >= 0 && rightSoftMoto.indexOf( "2" ) >= 0 )
                    {
                    return SOFT_KEY_RIGHT_MOTOROLA;
                    }
                else if ( rightSoftMoto1.indexOf( SOFT_WORD ) >= 0 && rightSoftMoto1.indexOf( "2" ) >= 0 )
                    {
                    return SOFT_KEY_RIGHT_MOTOROLA;
                    }
                else if ( rightSoftMoto2.indexOf( SOFT_WORD ) >= 0 && rightSoftMoto2.indexOf( "2" ) >= 0 )
                    {
                    return SOFT_KEY_RIGHT_MOTOROLA1;
                    }
                else if ( rightSoftMoto.indexOf( SOFT_WORD ) >= 0 && rightSoftMoto.indexOf( "RIGHT" ) >= 0 )
                    {
                    return SOFT_KEY_LEFT_MOTOROLA;
                    }
                else if ( rightSoftMoto1.indexOf( SOFT_WORD ) >= 0 && rightSoftMoto1.indexOf( "RIGHT" ) >= 0 )
                    {
                    return SOFT_KEY_RIGHT_MOTOROLA1;
                    }
                else if ( rightSoftMoto2.indexOf( SOFT_WORD ) >= 0 && rightSoftMoto2.indexOf( "RIGHT" ) >= 0 )
                    {
                    return SOFT_KEY_RIGHT_MOTOROLA;
                    }
                }
            else if ( platformName.equals( PLATFORM_NOKIA ) )
                {
                return SOFT_KEY_RIGHT_NOKIA;
                }
            else if ( platformName.equals( PLATFORM_SAMSUNG ) )
                {
                return SOFT_KEY_RIGHT_SAMSUNG;
                }
            else if ( platformName.equals( PLATFORM_SIEMENS ) )
                {
                String rightSoftSiemens = aCanvas.getKeyName( SOFT_KEY_RIGHT_SIEMENS ).toUpperCase();
                if ( rightSoftSiemens.indexOf( SOFT_WORD ) >= 0 )
                    {
                    if ( rightSoftSiemens.indexOf( "4" ) >= 0 )
                        {
                        return SOFT_KEY_RIGHT_SIEMENS;
                        }
                    else if ( rightSoftSiemens.indexOf( "RIGHT" ) >= 0 )
                        {
                        return SOFT_KEY_RIGHT_SIEMENS;
                        }
                    }
                }
            else if ( platformName.equals( PLATFORM_SONY_ERICSSON ) )
                {
                return SOFT_KEY_RIGHT_SE;
                }
            else if ( platformName.equals( PLATFORM_SUN ) )
                {
                return SOFT_KEY_RIGHT_DEFAULT;
                }
            else if ( platformName.equals( PLATFORM_INTENT ) )
                {
                return SOFT_KEY_RIGHT_INTENT;
                }
            else if ( platformName.equals( PLATFORM_NOT_DEFINED ) )
                {
                for ( int i = -125; i <= 125; i++ )
                    {
                    if ( i == 0 )
                        {
                        i++;
                        }
                    String keyName = aCanvas.getKeyName( i ).toUpperCase();
                    if ( keyName.indexOf( SOFT_WORD ) >= 0 )
                        {
                        if ( keyName.indexOf( "2" ) >= 0 )
                            {
                            keyCode = i;
                            break;
                            }
                        else if ( keyName.indexOf( "4" ) >= 0 )
                            {
                            keyCode = i;
                            break;
                            }
                        else if ( keyName.indexOf( "RIGHT" ) >= 0 )
                            {
                            keyCode = i;
                            break;
                            }
                        }
                    }
                }
            if ( keyCode == 0 )
                {
                return SOFT_KEY_RIGHT_DEFAULT;
                }
            }
        catch ( Throwable iaEx )
            {
            return SOFT_KEY_RIGHT_DEFAULT;
            }
        return keyCode;
        }

    private int getDeleteKeyCode( final Canvas aCanvas )
        {
        try
            {
            if ( platformName.equals( PLATFORM_NOKIA ) )
                {
                return DELETE_KEY_NOKIA;
                }
            else if ( platformName.equals( PLATFORM_SAMSUNG ) )
                {
                if ( aCanvas.getKeyName( DELETE_KEY_SAMSUNG ).toUpperCase().indexOf( "CLEAR" ) >= 0 )
                    {
                    return DELETE_KEY_SAMSUNG;
                    }
                }
            else if ( platformName.equals( PLATFORM_SONY_ERICSSON ) )
                {
                return DELETE_KEY_SE;
                }
            else if ( platformName.equals( PLATFORM_SUN ) )
                {
                return DELETE_KEY_DEFAULT;
                }
            }
        catch ( final Throwable e )
            {
            return DELETE_KEY_DEFAULT;
            }
        return 0;
        }

    private int getBackKeyCode()
        {
        if ( platformName.equals( PLATFORM_SONY_ERICSSON ) )
            {
            return BACK_KEY_SE;
            }
        return 0;
        }



    private static final int SOFT_KEY_LEFT_DEFAULT = -6;

    private static final int SOFT_KEY_RIGHT_DEFAULT = -7;

    private static final int DELETE_KEY_DEFAULT = -8;

    private static final int SOFT_KEY_LEFT_SE = -6;

    private static final int SOFT_KEY_RIGHT_SE = -7;

    private static final int DELETE_KEY_SE = -8;

    private static final int BACK_KEY_SE = -11;

    private static final int SOFT_KEY_LEFT_SAMSUNG = -6;

    private static final int SOFT_KEY_RIGHT_SAMSUNG = -7;

    private static final int DELETE_KEY_SAMSUNG = -8;

    private static final int SOFT_KEY_LEFT_SIEMENS = -1;

    private static final int SOFT_KEY_RIGHT_SIEMENS = -4;

    private static final int SOFT_KEY_LEFT_NOKIA = -6;

    private static final int SOFT_KEY_RIGHT_NOKIA = -7;

    private static final int DELETE_KEY_NOKIA = -8;

    private static final int DELETE_KEY_ME4SE = -8;

    private static final int PENCIL_KEY_NOKIA = -50;

    private static final int SOFT_KEY_LEFT_MOTOROLA = -21;

    private static final int SOFT_KEY_RIGHT_MOTOROLA = -22;

    private static final int SOFT_KEY_LEFT_MOTOROLA2 = -20;

    private static final int SOFT_KEY_LEFT_MOTOROLA1 = 21;

    private static final int SOFT_KEY_RIGHT_MOTOROLA1 = 22;

    private static final int SOFT_KEY_LEFT_INTENT = 57345;

    private static final int SOFT_KEY_RIGHT_INTENT = 57346;

    private static final String SOFT_WORD = "SOFT";
    }
