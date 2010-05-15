package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.graphics.BitmapFontGenerator;
import net.intensicode.util.Log;

public final class IntensiGameHelper
    {
    public IntensiGameHelper( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    public void updateResourcesSubfolder( final int aWidth, final int aHeight )
        {
        final String screenOrientationId = myGameSystem.platform.screenOrientationId();

        final String resourcesFolder = determineResourcesFolder( aWidth, aHeight, screenOrientationId );
        Log.info( "resources folder choosen: {}", resourcesFolder );

        final ResourcesManager resources = myGameSystem.resources;
        resources.clearSubfolders();
        if ( resourcesFolder != null && resourcesFolder.length() > 0 )
            {
            final StringBuffer buffer = new StringBuffer();
            buffer.append( resourcesFolder );
            buffer.append( "/" );
            buffer.append( screenOrientationId );

            resources.addSubfolder( buffer.toString() );
            resources.addSubfolder( resourcesFolder );
            }
        else
            {
            resources.addSubfolder( screenOrientationId );
            }
        }

    public final String determineResourcesFolder( final int aWidth, final int aHeight, final String aScreenOrientationId )
        {
        Log.info( "choosing resources folder for {}x{} and orientation " + aScreenOrientationId, aWidth, aHeight );

        String bestChoice = null;
        int bestWidth = 0;
        int bestHeight = 0;

        final Configuration configuration = myGameSystem.resources.loadConfigurationOrUseDefaults( "resources.properties" );
        final String[] resourcesFolders = configuration.readList( "subfolders", "", "," );
        for ( int idx = 0; idx < resourcesFolders.length; idx++ )
            {
            final String widthAndHeight = resourcesFolders[ idx ];
            Log.info( "considering resources subfolder {}", widthAndHeight );
            final int crossPos = widthAndHeight.indexOf( "x" );
            if ( crossPos == -1 ) continue;
            final int width = Integer.parseInt( widthAndHeight.substring( 0, crossPos ) );
            final int height = Integer.parseInt( widthAndHeight.substring( crossPos + 1 ) );
            if ( aScreenOrientationId == PlatformContext.SCREEN_ORIENTATION_LANDSCAPE && height <= aWidth && width <= aHeight )
                {
                if ( width > bestWidth && height > bestHeight ) bestChoice = widthAndHeight;
                }
            else if ( width <= aWidth && height <= aHeight )
                {
                if ( width > bestWidth && height > bestHeight ) bestChoice = widthAndHeight;
                }
            }

        if ( bestChoice != null ) return bestChoice;

        if ( resourcesFolders.length > 0 ) return resourcesFolders[0];

        return null;
        }

    public final void toggleDebugScreen()
        {
        //#if DEBUG
        myGameSystem.debug.visible = !myGameSystem.debug.visible;
        //#endif
        }

    public final void toggleCheatScreen()
        {
        // TODO: Toggle system.cheat screen visibility..
        }

    public final void triggerConfigurationMenu()
        {
        try
            {
            final SystemContext context = myGameSystem.context;
            final ConfigurationMenu menu = new ConfigurationMenu( "CONFIGURATION", myGameSystem.systemFont );
            menu.add( context.getApplicationValues() );
            menu.add( context.getPlatformValues() );
            menu.add( context.getSystemValues() );
            myGameSystem.stack.pushOnce( menu );
            }
        catch ( Exception e )
            {
            myGameSystem.showError( "failed showing configuration menu", e );
            }
        }

    public final void initGameSystemFromConfigurationFile()
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

    public final void deleteConfiguration( final ConfigurationElementsTree aTree )
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

    public final void loadConfiguration( final ConfigurationElementsTree aTree )
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

    public final void saveConfiguration( final ConfigurationElementsTree aTree )
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
