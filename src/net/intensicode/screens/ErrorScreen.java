package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Rectangle;

public final class ErrorScreen extends ScreenBase
    {
    public boolean critical;

    public String message;



    public ErrorScreen( final FontGenerator aFont )
        {
        myFont = aFont;
        }

    public final void reset()
        {
        critical = false;
        message = null;
        myCauseOrNull = null;
        myAnimCounter = 0;
        }

    public final void setCause( final Throwable aThrowable )
        {
        if ( aThrowable != null ) myCauseOrNull = aThrowable.toString();
        }

    // From ScreenBase

    public final void onInit( final GameSystem aGameSystem ) throws Exception
        {
        final DirectScreen screen = aGameSystem.screen;
        myBorderWidth = screen.width() / 32;
        myMessageOffset = myBorderWidth * 2;
        myMesageBoxHeight = screen.height() / 5;

        mySoftkeys = new SoftkeysScreen( myFont );
        mySoftkeys.onInit( aGameSystem );
        mySoftkeys.setInsets( myMessageOffset, myMessageOffset );

        // Call this last because we need mySoftkeys in onInitEverytime..
        super.onInit( aGameSystem );
        }

    public final void onInitEverytime() throws Exception
        {
        if ( critical ) mySoftkeys.setSoftkeys( null, "EXIT" );
        else mySoftkeys.setSoftkeys( "CONTINUE", "EXIT" );
        }

    //#if TOUCH
    private boolean myPreviousGlobalControlsState;
    //#endif

    public final void onTop()
        {
        //#if TOUCH
        myPreviousGlobalControlsState = touch().globalControlsActive;
        touch().globalControlsActive = false;
        //#endif
        mySoftkeys.onTop();
        }

    public final void onPop()
        {
        mySoftkeys.onPop();
        //#if TOUCH
        touch().globalControlsActive = myPreviousGlobalControlsState;
        //#endif
        }

    public final void onControlTick() throws Exception
        {
        myAnimCounter++;
        if ( myAnimCounter >= timing().ticksPerSecond * 2 ) myAnimCounter = 0;

        final KeysHandler keys = keys();
        if ( keys.checkLeftSoftAndConsume() || keys.checkFire1AndConsume() )
            {
            if ( !critical ) stack().popScreen( this );
            }
        else if ( keys.checkRightSoftAndConsume() || keys.checkFire2AndConsume() )
            {
            system().shutdownAndExit();
            }

        mySoftkeys.onControlTick();
        }

    public final void onDrawFrame()
        {
        final DirectGraphics gc = graphics();

        final int screenWidth = screen().width();
        final int screenHeight = screen().height();

        gc.clearRGB24( 0 );
        gc.setColorRGB24( 0xf00000 );

        if ( myAnimCounter < timing().ticksPerSecond )
            {
            gc.fillRect( 0, 0, screenWidth, myBorderWidth );
            gc.fillRect( 0, screenHeight - myBorderWidth, screenWidth, myBorderWidth );
            gc.fillRect( 0, 0, myBorderWidth, screenHeight );
            gc.fillRect( screenWidth - myBorderWidth, 0, myBorderWidth, screenHeight );
            }

        if ( message != null && message.length() > 0 )
            {
            myTextRect.x = myMessageOffset;
            myTextRect.y = myMessageOffset;
            myTextRect.width = screenWidth - myMessageOffset * 2;
            myTextRect.height = myMesageBoxHeight;
            myFont.blitText( gc, message, myTextRect );
            }

        if ( myCauseOrNull != null && myCauseOrNull.length() > 0 )
            {
            myTextRect.x = myMessageOffset;
            myTextRect.y = myMessageOffset + myMesageBoxHeight + myBorderWidth;
            myTextRect.width = screenWidth - myMessageOffset * 2;
            myTextRect.height = screenHeight - myTextRect.y - myBorderWidth;
            myFont.blitText( gc, myCauseOrNull, myTextRect );
            }

        mySoftkeys.onDrawFrame();
        }



    private int myAnimCounter;

    private int myBorderWidth;

    private int myMessageOffset;

    private int myMesageBoxHeight;

    private String myCauseOrNull;

    private SoftkeysScreen mySoftkeys;


    private final FontGenerator myFont;

    private final Rectangle myTextRect = new Rectangle();
    }
