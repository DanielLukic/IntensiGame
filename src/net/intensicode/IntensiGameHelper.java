package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.graphics.BitmapFontGenerator;
import net.intensicode.util.Log;

class IntensiGameHelper
    {
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

    IntensiGameHelper( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    void initGameSystemFromConfigurationFile()
        {
        final Configuration engineConfiguration = loadEngineConfiguration();
        if ( engineConfiguration != Configuration.NULL_CONFIGURATION )
            {
            applyEngineConfiguration( engineConfiguration );
            }

        final Configuration skinConfiguration = loadSkinConfiguration();
        if ( skinConfiguration != Configuration.NULL_CONFIGURATION )
            {
            applySkinConfiguration( skinConfiguration );
            }
        }

    void loadConfiguration( final ConfigurationElementsTree aTree )
        {
        if ( aTree == ConfigurationElementsTree.EMPTY ) return;
        try
            {
            myGameSystem.storage.load( new ConfigurationElementsTreeIO( aTree ) );
            }
        catch ( final Exception e )
            {
            Log.error( "failed loading configuration elements tree {}", aTree.label, e );
            }
        }

    void saveConfiguration( final ConfigurationElementsTree aTree )
        {
        if ( aTree == ConfigurationElementsTree.EMPTY ) return;
        try
            {
            myGameSystem.storage.save( new ConfigurationElementsTreeIO( aTree ) );
            }
        catch ( final Exception e )
            {
            Log.error( "failed saving configuration elements tree {}", aTree.label, e );
            }
        }

    // Implementation

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
