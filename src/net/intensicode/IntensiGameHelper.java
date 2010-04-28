package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.graphics.BitmapFontGenerator;
import net.intensicode.util.Log;

public final class IntensiGameHelper
    {
    public static void toggleDebugScreen( final GameSystem aGameSystem )
        {
        //#if DEBUG
        aGameSystem.debug.visible = !aGameSystem.debug.visible;
        //#endif
        }

    public static void toggleCheatScreen( final GameSystem aGameSystem )
        {
        // TODO: Toggle system.cheat screen visibility..
        }

    public static void triggerConfigurationMenu( final GameSystem aGameSystem )
        {
        try
            {
            final SystemContext context = aGameSystem.context;
            final ConfigurationMenu menu = new ConfigurationMenu( "CONFIGURATION", aGameSystem.systemFont );
            menu.add( context.getApplicationValues() );
            menu.add( context.getPlatformValues() );
            menu.add( context.getSystemValues() );
            aGameSystem.stack.pushOnce( menu );
            }
        catch ( Exception e )
            {
            aGameSystem.showError( "failed showing configuration menu", e );
            }
        }

    public IntensiGameHelper( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    public void initGameSystemFromConfigurationFile()
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

    public void deleteConfiguration( final ConfigurationElementsTree aTree )
        {
        if ( aTree == ConfigurationElementsTree.EMPTY ) return;
        try
            {
            myGameSystem.storage.erase( new ConfigurationElementsTreeIO( aTree ) );
            }
        catch ( final Exception e )
            {
            Log.error( "failed deleting configuration elements tree {}", aTree.label, e );
            }
        }

    public void loadConfiguration( final ConfigurationElementsTree aTree )
        {
        if ( aTree == ConfigurationElementsTree.EMPTY ) return;
        try
            {
            myGameSystem.storage.load( new ConfigurationElementsTreeIO( aTree ) );
            }
        catch ( final Exception e )
            {
            Log.info( "failed loading configuration elements tree {} - {} ignored", aTree.label, e );
            }
        }

    public void saveConfiguration( final ConfigurationElementsTree aTree )
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

    private void applySkinConfiguration( final Configuration aConfiguration )
        {
        myGameSystem.skin.apply( aConfiguration );
        }


    private GameSystem myGameSystem;

    private static final int DEFAULT_SCREEN_WIDTH = 240;

    private static final int DEFAULT_SCREEN_HEIGHT = 320;
    }
