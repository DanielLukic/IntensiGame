package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.util.Assert;

public abstract class ScreenBase
    {
    public void onInit( final GameSystem aGameSystem ) throws Exception
        {
        if ( !isInitialized() )
            {
            myGameSystem = aGameSystem;
            onInitOnce();
            }
        onInitEverytime();
        }

    public void onInitOnce() throws Exception
        {
        }

    public void onInitEverytime() throws Exception
        {
        }

    public void onTop()
        {
        }

    public void onPop()
        {
        }

    public abstract void onControlTick() throws Exception;

    public abstract void onDrawFrame();

    // Protected Interface

    protected ScreenBase()
        {
        }

    protected final boolean isInitialized()
        {
        return myGameSystem != null;
        }

    protected final GameSystem system()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        //#endif
        return myGameSystem;
        }

    protected final StorageManager storage()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "storage not initialized", myGameSystem.storage );
        //#endif
        return myGameSystem.storage;
        }

    protected final ResourcesManager resources()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "resources not initialized", myGameSystem.resources );
        //#endif
        return myGameSystem.resources;
        }

    protected final SkinManager skin()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "skin not initialized", myGameSystem.skin );
        //#endif
        return myGameSystem.skin;
        }

    protected final GameEngine engine()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "engine not initialized", myGameSystem.engine );
        //#endif
        return myGameSystem.engine;
        }

    protected final GameTiming timing()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "timing not initialized", myGameSystem.timing );
        //#endif
        return myGameSystem.timing;
        }

    protected final DirectScreen screen()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "screen not initialized", myGameSystem.screen );
        //#endif
        return myGameSystem.screen;
        }

    protected final DirectGraphics graphics()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "graphics not initialized", myGameSystem.graphics );
        //#endif
        return myGameSystem.graphics;
        }

    protected final ScreenStack stack()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "stack not initialized", myGameSystem.stack );
        //#endif
        return myGameSystem.stack;
        }

    //#if TOUCH_SUPPORTED
    protected final TouchHandler touch()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "touch not initialized", myGameSystem.touch );
        //#endif
        return myGameSystem.touch;
        }
    //#endif

    protected final KeysHandler keys()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "keys not initialized", myGameSystem.keys );
        //#endif
        return myGameSystem.keys;
        }

    protected final AudioManager audio()
        {
        //#if DEBUG
        Assert.isNotNull( "system not initialized", myGameSystem );
        Assert.isNotNull( "audio not initialized", myGameSystem.audio );
        //#endif
        return myGameSystem.audio;
        }

    private GameSystem myGameSystem;
    }
