/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode;

import net.intensicode.core.*;
import net.intensicode.util.BitmapFontGen;
import net.intensicode.util.Log;
import net.intensicode.util.Size;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.io.IOException;



/**
 * TODO: Describe this!
 */
public abstract class IntensiGame extends MIDlet implements SystemContext
    {
    public IntensiGame() throws Exception
        {
        myLoader = new ResourceLoader( getClass() );
        }

    public final void init()
        {
        initConfiguration();
        initEngine();
        }

    // From MIDlet

    public final void startApp() throws MIDletStateChangeException
        {
        try
            {
            if ( myEngine == null ) init();
            //display().setCurrent( myDisplayable );
            myEngine.start();
            }
        catch ( final Exception e )
            {
            //#if DEBUG
            e.printStackTrace();
            //#endif

            throw new MIDletStateChangeException( e.toString() );
            }
        }

    public final void pauseApp()
        {
        myEngine.stop();
        //display().setCurrent( null );
        notifyPaused();
        }

    public final void destroyApp( final boolean unconditional )
        {
        myEngine.stop();
        //display().setCurrent( null );
        notifyDestroyed();
        }

    // From SystemContext

    public final void exit()
        {
        destroyApp( true );

        //#if RUNME
        System.exit( 0 );
        //#endif
        }

    public final void pause()
        {
        pauseApp();
        }

    public final Display getDisplay()
        {
        return Display.getDisplay( this );
        }

    public final ResourceLoader getResourceLoader()
        {
        return myLoader;
        }

    // Protected Interface

    protected void initConfiguration()
        {
        try
            {
            final byte[] data = myLoader.loadData( "/engine.properties" );
            myConfiguration = new Configuration( new String( data ) );
            }
        catch ( final IOException e )
            {
            //#if DEBUG
            Log.debug( "Failed loading '/engine.properties': {}", e );
            //#endif

            myConfiguration = new Configuration();
            }
        }

    protected void initEngine()
        {
        Engine.limitFps = myConfiguration.readInt( "Engine.limitFps", Engine.limitFps );
        Engine.ticksPerSecond = myConfiguration.readInt( "Engine.ticksPerSecond", Engine.ticksPerSecond );
        Engine.limitTpsPerLoop = myConfiguration.readInt( "Engine.limitTpsPerLoop", Engine.limitTpsPerLoop );
        DirectScreen.scaleToFitPixelMargin = myConfiguration.readInt( "DirectScreen.scaleToFitPixelMargin", DirectScreen.scaleToFitPixelMargin );

        Engine.showStats = myConfiguration.readBoolean( "Engine.showStats", Engine.showStats );
        BitmapFontGen.buffered = myConfiguration.readBoolean( "BitmapFontGen.buffered", BitmapFontGen.buffered );

        final int width = myConfiguration.readInt( "DirectScreen.width", 176 );
        final int height = myConfiguration.readInt( "DirectScreen.height", 208 );
        //#if DEBUG
        Log.debug( "DirectScreen: {}x{}", width, height );
        //#endif
        myEngine = new Engine( this, new Size( width, height ) );
        }



    protected Engine myEngine;

    protected Configuration myConfiguration;

    protected final ResourceLoader myLoader;
    }
