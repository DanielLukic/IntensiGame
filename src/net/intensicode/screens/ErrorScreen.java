package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class ErrorScreen extends ScreenBase
        //#if TOUCH
        implements net.intensicode.touch.TouchableHandler
        //#endif
    {
    public boolean critical;

    public String message;


    public ErrorScreen( final FontGenerator aFont )
        {
        changeFont( aFont );
        }

    public final void changeFont( final FontGenerator aFontGenerator )
        {
        myFont = aFontGenerator;
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
        myCauseOrNull = aThrowable;
        myCauseAsStringOrNull = String.valueOf( aThrowable );
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

        //#if DEBUG
        system().debug.visible = false;
        //#endif
        }

    //#if TOUCH

    //#if FEEDBACK

    public final void onInitEverytime() throws Exception
        {
        myTouchableFeedbackButton.text = "SEND BUG REPORT";
        myTouchableFeedbackButton.associatedHandler = this;
        myTouchableFeedbackButton.font = myFont;
        myTouchableFeedbackButton.alignment = FontGenerator.CENTER;
        myTouchableFeedbackButton.position.setTo( screen().width() / 2, screen().height() - myMessageOffset * 3 );
        myTouchableFeedbackButton.updateTouchableRect();
        }

    private final net.intensicode.touch.TouchableText myTouchableFeedbackButton = new net.intensicode.touch.TouchableText();

    //#endif

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
        if ( critical ) mySoftkeys.setSoftkeys( null, "EXIT" );
        else mySoftkeys.setSoftkeys( "CONTINUE", "EXIT" );

        //#if TOUCH && FEEDBACK
        touch().addLocalControl( myTouchableFeedbackButton );
        //#endif

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
        gc.clearRGB24( 0 );
        gc.setColorRGB24( 0xf00000 );

        blitBlinkingBorder();
        blitMessageIfAvailable();
        blitCauseIfAvailable();

        mySoftkeys.onDrawFrame();
        }

    private void blitBlinkingBorder()
        {
        if ( myAnimCounter >= timing().ticksPerSecond ) return;

        final DirectGraphics gc = graphics();

        final int screenWidth = screen().width();
        final int screenHeight = screen().height();

        gc.fillRect( 0, 0, screenWidth, myBorderWidth );
        gc.fillRect( 0, screenHeight - myBorderWidth, screenWidth, myBorderWidth );
        gc.fillRect( 0, 0, myBorderWidth, screenHeight );
        gc.fillRect( screenWidth - myBorderWidth, 0, myBorderWidth, screenHeight );
        }

    private void blitMessageIfAvailable()
        {
        try
            {
            if ( message == null || message.length() == 0 ) return;

            final DirectGraphics gc = graphics();
            final int screenWidth = screen().width();

            myTextRect.x = myMessageOffset;
            myTextRect.y = myMessageOffset;
            myTextRect.width = screenWidth - myMessageOffset * 2;
            myTextRect.height = myMesageBoxHeight;
            myFont.blitText( gc, message, myTextRect );
            }
        catch ( final Exception e )
            {
            handleException( e );
            }
        }

    private void blitCauseIfAvailable()
        {
        try
            {
            if ( myCauseAsStringOrNull == null || myCauseAsStringOrNull.length() == 0 ) return;

            final DirectGraphics gc = graphics();
            final int screenWidth = screen().width();
            final int screenHeight = screen().height();

            myTextRect.x = myMessageOffset;
            myTextRect.y = myMessageOffset + myMesageBoxHeight + myBorderWidth;
            myTextRect.width = screenWidth - myMessageOffset * 2;
            myTextRect.height = screenHeight - myTextRect.y - myBorderWidth;
            myFont.blitText( gc, myCauseAsStringOrNull, myTextRect );
            }
        catch ( final Exception e )
            {
            handleException( e );
            }
        }

    private void handleException( final Exception e )
        {
        //#if DEBUG
        //# Log.error( "exception in ErrorScreen", e );
        //#else
        // Avoid messing up the log output:
        Log.error( "exception in ErrorScreen: {}", e, null );
        //#endif
        }

    //#if TOUCH

    // From TouchableHandler

    public void onPressed( final Object aTouchable )
        {
        //#if FEEDBACK
        final net.intensicode.configuration.SendFeedback feedback = new net.intensicode.configuration.SendFeedback( system() );
        feedback.optionalMessageAddon = message + "\n" + myCauseOrNull;
        if ( myCauseOrNull != null )
            {
            final String exceptionData = system().platform.getExtendedExceptionData( myCauseOrNull );
            feedback.optionalMessageAddon += "\n" + exceptionData;
            }
        feedback.trigger();
        //#endif
        }

    public void onReleased( final Object aTouchable )
        {
        }

    //#endif

    private int myAnimCounter;

    private int myBorderWidth;

    private int myMessageOffset;

    private int myMesageBoxHeight;

    private Throwable myCauseOrNull;

    private String myCauseAsStringOrNull;

    private FontGenerator myFont;

    private SoftkeysScreen mySoftkeys;

    private final Rectangle myTextRect = new Rectangle();
    }
