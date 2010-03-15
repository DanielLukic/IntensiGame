package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.graphics.BitmapFontGenerator;
import net.intensicode.util.Log;

class IntensiGameHelper
    {
    static void initGameSystemFromConfigurationFile( final GameSystem aGameSystem )
        {
        final IntensiGameHelper helper = new IntensiGameHelper( aGameSystem );

        final Configuration engineConfiguration = helper.loadEngineConfiguration();
        if ( engineConfiguration != Configuration.NULL_CONFIGURATION )
            {
            helper.applyEngineConfiguration( engineConfiguration );
            }

        final Configuration skinConfiguration = helper.loadSkinConfiguration();
        if ( skinConfiguration != Configuration.NULL_CONFIGURATION )
            {
            helper.applySkinConfiguration( skinConfiguration );
            }
        }

    static void toggleDebugScreen( final GameSystem aGameSystem )
        {
        //#if DEBUG
        aGameSystem.debug.visible = !aGameSystem.debug.visible;
        //#endif
        }

    static void toggleCheatScreen( final GameSystem aGameSystem )
        {
        // TODO: Toggle system.cheat screen visibility..
        }

    // Implementation

    private IntensiGameHelper( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    private Configuration loadEngineConfiguration()
        {
        return myGameSystem.resources.loadConfigurationOrUseDefaults( "engine.properties" );
        }

    private Configuration loadSkinConfiguration()
        {
        return myGameSystem.resources.loadConfigurationOrUseDefaults( "skin.properties" );
        }

    private void applyEngineConfiguration( final Configuration aConfiguration )
        {
        // TODO: Move to GameTiming#apply(Configuration)
        final GameTiming timing = myGameSystem.timing;
        timing.ticksPerSecond = aConfiguration.readInt( "GameTiming.ticksPerSecond", timing.ticksPerSecond );
        timing.maxFramesPerSecond = aConfiguration.readInt( "GameTiming.maxFramesPerSecond", timing.maxFramesPerSecond );

        // TODO: Move to DirectScreen#apply(Configuration)        
        final int width = aConfiguration.readInt( "DirectScreen.width", DEFAULT_SCREEN_WIDTH );
        final int height = aConfiguration.readInt( "DirectScreen.height", DEFAULT_SCREEN_HEIGHT );
        myGameSystem.screen.setTargetSize( width, height );
        final String mode = aConfiguration.readString( "DirectScreen.viewport_scale_mode", DEFAULT_SCREEN_MODE );
        myGameSystem.screen.setViewportMode( getViewportMode( mode ) );

        // TODO: Move to BitmapFontGenerator#apply(Configuration)                
        BitmapFontGenerator.buffered = aConfiguration.readBoolean( "BitmapFontGenerator.buffered", BitmapFontGenerator.buffered );

        // Register ResourcesManager with BitmapFontGenerator to make buffered blitting possible.
        BitmapFontGenerator.resources = myGameSystem.resources;

        Log.info( "GameTiming: tps={} fps={}", timing.ticksPerSecond, timing.maxFramesPerSecond );
        Log.info( "DirectScreen: {}x{}", width, height );
        }

    private int getViewportMode( final String aModeName )
        {
        if ( aModeName.equalsIgnoreCase( "system" ) ) return DirectScreen.VIEWPORT_MODE_SYSTEM;
        if ( aModeName.equalsIgnoreCase( "fullscreen" ) ) return DirectScreen.VIEWPORT_MODE_FULLSCREEN;
        //#if DEBUG
        throw new IllegalArgumentException( aModeName );
        //#else
        //# return DirectScreen.VIEWPORT_MODE_SYSTEM;
        //#endif
        }

    private void applySkinConfiguration( final Configuration aConfiguration )
        {
        myGameSystem.skin.apply( aConfiguration );
        }


    private GameSystem myGameSystem;

    private static final int DEFAULT_SCREEN_WIDTH = 240;

    private static final int DEFAULT_SCREEN_HEIGHT = 320;

    private static final String DEFAULT_SCREEN_MODE = "system";
    }
