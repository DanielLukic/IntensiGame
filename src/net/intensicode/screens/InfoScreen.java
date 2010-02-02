package net.intensicode.screens;

import net.intensicode.ReleaseProperties;
import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

import javax.microedition.media.Manager;

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
        myTextLines.add( "real width " + screen().width() );
        myTextLines.add( "real height " + screen().height() );
        myTextLines.add( "width " + screen().width() );
        myTextLines.add( "height " + screen().height() );

        myTextLines.add( "" );
        myTextLines.add( "SOUND FORMAT" );
        myTextLines.add( ReleaseProperties.SOUND_FORMAT_SUFFIX );
        myTextLines.add( ReleaseProperties.SOUND_FORMAT_MIME_TYPE );
        final String[] soundProtocols = Manager.getSupportedProtocols( ReleaseProperties.SOUND_FORMAT_MIME_TYPE );
        for ( int idx = 0; idx < soundProtocols.length; idx++ )
            {
            myTextLines.add( soundProtocols[ idx ] );
            }

        myTextLines.add( "" );
        myTextLines.add( "MUSIC FORMAT" );
        myTextLines.add( ReleaseProperties.MUSIC_FORMAT_SUFFIX );
        myTextLines.add( ReleaseProperties.MUSIC_FORMAT_MIME_TYPE );
        final String[] musicProtocols = Manager.getSupportedProtocols( ReleaseProperties.MUSIC_FORMAT_MIME_TYPE );
        for ( int idx = 0; idx < musicProtocols.length; idx++ )
            {
            myTextLines.add( musicProtocols[ idx ] );
            }

        myTextLines.add( "" );
        myTextLines.add( "HISCORE ID" );
        myTextLines.add( ReleaseProperties.HISCORE_ID );

        final KeysConfiguration config = keys.platformKeysConfiguration;
        myTextLines.add( "" );
        myTextLines.add( "PLATFORM/KEYCODES" );
        myTextLines.add( config.platformName );
        myTextLines.add( Integer.toString( config.softKeyLeft ) );
        myTextLines.add( Integer.toString( config.softKeyRight ) );
        myTextLines.add( Integer.toString( config.softKeyDelete ) );
        myTextLines.add( Integer.toString( config.softKeyBack ) );

        myTextLines.add( "" );
        myTextLines.add( "FORMATS" );
        final String[] protocols = Manager.getSupportedProtocols( null );
        for ( int idx = 0; idx < protocols.length; idx++ )
            {
            myTextLines.add( protocols[ idx ] );
            }

        myTextLines.add( "" );
        myTextLines.add( "TYPES" );
        final String[] types = Manager.getSupportedContentTypes( null );
        for ( int idx = 0; idx < types.length; idx++ )
            {
            myTextLines.add( types[ idx ] );
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
