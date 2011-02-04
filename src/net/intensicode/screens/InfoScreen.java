package net.intensicode.screens;

import net.intensicode.ReleaseProperties;
import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class InfoScreen extends MultiScreen
    {
    public InfoScreen( final FontGenerator aTitleFont, final FontGenerator aTextFont )
        {
        myTitleFont = aTitleFont;
        myTextFont = aTextFont;
        }

    public final InfoScreen shareSoftkeys( final SoftkeysScreen aSoftkeys )
        {
        mySoftkeys = aSoftkeys;
        return this;
        }

    // From AbstractScreen

    public final void onInitOnce() throws Exception
        {
        if ( mySoftkeys == null ) throw new IllegalStateException( "shareSoftkeys not called" );

        addScreen( new ClearScreen() );
        addScreen( mySoftkeys );

        myHelpPos.x = screen().width() / 2;
        myHelpPos.y = myTitleFont.charHeight();
        }

    public final void onInitEverytime() throws Exception
        {
        final KeysHandler keys = keys();
        keys.keyRepeatDelayInTicks = timing().ticksPerSecond * 25 / 100;
        keys.keyRepeatIntervalInTicks = timing().ticksPerSecond * 10 / 100;

        mySoftkeys.setSoftkeys( "", "BACK" );

        myTextLines.clear();

        myTextLines.add( "RELEASE" );
        myTextLines.add( "version " + ReleaseProperties.VERSION );
        myTextLines.add( "date " + ReleaseProperties.DATE );

        myTextLines.add( "" );
        myTextLines.add( "SCREEN" );
        myTextLines.add( "width " + screen().width() );
        myTextLines.add( "height " + screen().height() );
        myTextLines.add( "target width " + screen().getTargetWidth() );
        myTextLines.add( "target height " + screen().getTargetHeight() );
        myTextLines.add( "native width " + screen().getNativeWidth() );
        myTextLines.add( "native height " + screen().getNativeHeight() );

        myTextLines.add( "" );
        myTextLines.add( "SOUND FORMAT" );
        myTextLines.add( ReleaseProperties.SOUND_FORMAT_SUFFIX );
        myTextLines.add( ReleaseProperties.SOUND_FORMAT_MIME_TYPE );

        myTextLines.add( "" );
        myTextLines.add( "MUSIC FORMAT" );
        myTextLines.add( ReleaseProperties.MUSIC_FORMAT_SUFFIX );
        myTextLines.add( ReleaseProperties.MUSIC_FORMAT_MIME_TYPE );

        final KeysConfiguration config = keys.platformKeysConfiguration;
        myTextLines.add( "" );
        myTextLines.add( "PLATFORM/KEYCODES" );
        myTextLines.add( config.platformName );
        myTextLines.add( "left soft " + Integer.toString( config.softKeyLeft ) );
        myTextLines.add( "right soft " + Integer.toString( config.softKeyRight ) );
        myTextLines.add( "delete " + Integer.toString( config.softKeyDelete ) );
        myTextLines.add( "back " + Integer.toString( config.softKeyBack ) );

        myTextLines.add( "" );
        myTextLines.add( "SYSTEM INFORMATION" );

        final DynamicArray informationStrings = system().getInformationStrings();
        for ( int idx = 0; idx < informationStrings.size; idx++ )
            {
            myTextLines.add( informationStrings.get( idx ) );
            }
        }

    public final void onControlTick() throws Exception
        {
        final KeysHandler keys = keys();
        if ( keys.checkRightSoftAndConsume() ) stack().popScreen( this );

        super.onControlTick();

        if ( keys.checkUpAndConsume() ) myLineOffset--;
        if ( keys.checkDownAndConsume() ) myLineOffset++;

        final int linesOnScreen = linesOnScreen();
        if ( myLineOffset + linesOnScreen >= myTextLines.size ) myLineOffset = myTextLines.size - linesOnScreen;
        if ( myLineOffset < 0 ) myLineOffset = 0;
        }

    public final void onDrawFrame()
        {
        super.onDrawFrame();

        final DirectGraphics gc = graphics();
        myTitleFont.blitString( gc, "INFO", myHelpPos, FontGenerator.CENTER );

        if ( myTextLines.size < 1 ) return;

        final int linesOnScreen = linesOnScreen();

        final Object[] lines = myTextLines.objects;
        for ( int idx = 0; idx < linesOnScreen; idx++ )
            {
            if ( myLineOffset + idx >= myTextLines.size ) break;

            final String line = (String) lines[ myLineOffset + idx ];
            myBlitPos.x = screen().width() / 2;
            myBlitPos.y = titleHeight() + idx * lineHeight();
            myTextFont.blitString( gc, line, myBlitPos, FontGenerator.CENTER );
            }

        myBlitPos.x = 0;
        myBlitPos.y = 0;
        myTextFont.blitNumber( gc, myBlitPos, keys().lastCode, FontGenerator.TOP_LEFT );
        myBlitPos.y = myTextFont.charHeight();
        myTextFont.blitNumber( gc, myBlitPos, keys().lastAction, FontGenerator.TOP_LEFT );
        }

    private int linesOnScreen()
        {
        final int textHeight = ( screen().height() - titleHeight() );
        return ( textHeight / lineHeight() - 1 );
        }

    private int lineHeight()
        {
        return myTextFont.charHeight() * 3 / 2;
        }

    private int titleHeight()
        {
        return myTitleFont.charHeight() * 3;
        }


    private int myLineOffset = 0;

    private SoftkeysScreen mySoftkeys;

    private final FontGenerator myTextFont;

    private final FontGenerator myTitleFont;

    private final Position myBlitPos = new Position();

    private final Position myHelpPos = new Position();

    private final DynamicArray myTextLines = new DynamicArray( 25, 25 );
    }
