package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.graphics.BitmapFontGenerator;
import net.intensicode.util.Log;

import java.io.IOException;

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

    IntensiGameHelper( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    Configuration loadEngineConfiguration()
        {
        return loadConfiguration( "engine.properties" );
        }

    Configuration loadSkinConfiguration()
        {
        return loadConfiguration( "skin.properties" );
        }

    void applyEngineConfiguration( final Configuration aConfiguration )
        {
        // TODO: Move to GameTiming#apply(Configuration)
        final GameTiming timing = myGameSystem.timing;
        timing.ticksPerSecond = aConfiguration.readInt( "GameTiming.ticksPerSecond", timing.ticksPerSecond );
        timing.maxFramesPerSecond = aConfiguration.readInt( "GameTiming.maxFramesPerSecond", timing.maxFramesPerSecond );
        timing.minFramesPerSecond = aConfiguration.readInt( "GameTiming.minFramesPerSecond", timing.minFramesPerSecond );

        // TODO: Move to DirectScreen#apply(Configuration)        
        final int width = aConfiguration.readInt( "DirectScreen.width", 240 );
        final int height = aConfiguration.readInt( "DirectScreen.height", 320 );
        myGameSystem.screen.setTargetSize( width, height );

        // TODO: Move to BitmapFontGenerator#apply(Configuration)                
        BitmapFontGenerator.buffered = aConfiguration.readBoolean( "BitmapFontGenerator.buffered", BitmapFontGenerator.buffered );

        // Register ResourcesManager with BitmapFontGenerator to make buffered blitting possible.
        BitmapFontGenerator.resources = myGameSystem.resources;

        //#if DEBUG
        Log.debug( "GameTiming: tps={} fps={}", timing.ticksPerSecond, timing.maxFramesPerSecond );
        Log.debug( "DirectScreen: {}x{}", width, height );
        //#endif
        }

    void applySkinConfiguration( final Configuration aConfiguration )
        {
        myGameSystem.skin.apply( aConfiguration );
        }

    // Implementation

    private Configuration loadConfiguration( final String aResourcePath )
        {
        try
            {
            final String configurationData = myGameSystem.resources.loadString( aResourcePath );
            return new Configuration( configurationData );
            }
        catch ( final IOException e )
            {
            //#if DEBUG
            Log.debug( "Failed loading {}. Ignored. Using defaults.", aResourcePath );
            //#endif
            return new Configuration();
            }
        }

    private GameSystem myGameSystem;
    }
