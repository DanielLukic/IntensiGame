package net.intensicode.screens;

import net.intensicode.SystemConfiguration;
import net.intensicode.PlatformConfiguration;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.core.Keys;
import net.intensicode.core.MultiScreen;
import net.intensicode.util.DynamicArray;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;

import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;



/**
 * TODO: Describe this!
 */
public final class InfoScreen extends MultiScreen
    {
    public InfoScreen( final FontGen aTitleFont, final FontGen aTextFont )
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

    public final void onInitOnce( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        addScreen( new ColorScreen() );
        addScreen( mySoftkeys );

        myHelpPos.x = aScreen.width() / 2;
        myHelpPos.y = myTitleFont.charHeight();
        }

    public final void onInitEverytime( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        final Keys keys = aEngine.keys;
        keys.keyRepeatDelayInTicks = Engine.ticksPerSecond * 25 / 100;
        keys.keyRepeatIntervalInTicks = Engine.ticksPerSecond * 10 / 100;

        mySoftkeys.setSoftkeys( "", "BACK" );

        myTextLines.clear();

        myTextLines.add( "RELEASE" );
        myTextLines.add( "version " + SystemConfiguration.VERSION );
        myTextLines.add( "date " + SystemConfiguration.DATE );

        myTextLines.add( "" );
        myTextLines.add( "SCREEN" );
        myTextLines.add( "real width " + aScreen.getWidth() );
        myTextLines.add( "real height " + aScreen.getHeight() );
        myTextLines.add( "width " + aScreen.width() );
        myTextLines.add( "height " + aScreen.height() );

        myTextLines.add( "" );
        myTextLines.add( "SOUND FORMAT" );
        myTextLines.add( SystemConfiguration.SOUND_FORMAT_SUFFIX );
        myTextLines.add( SystemConfiguration.SOUND_FORMAT_MIME_TYPE );
        final String[] soundProtocols = Manager.getSupportedProtocols( SystemConfiguration.SOUND_FORMAT_MIME_TYPE );
        for ( int idx = 0; idx < soundProtocols.length; idx++ )
            {
            myTextLines.add( soundProtocols[ idx ] );
            }

        myTextLines.add( "" );
        myTextLines.add( "MUSIC FORMAT" );
        myTextLines.add( SystemConfiguration.MUSIC_FORMAT_SUFFIX );
        myTextLines.add( SystemConfiguration.MUSIC_FORMAT_MIME_TYPE );
        final String[] musicProtocols = Manager.getSupportedProtocols( SystemConfiguration.MUSIC_FORMAT_MIME_TYPE );
        for ( int idx = 0; idx < musicProtocols.length; idx++ )
            {
            myTextLines.add( musicProtocols[ idx ] );
            }

        myTextLines.add( "" );
        myTextLines.add( "HISCORE ID" );
        myTextLines.add( SystemConfiguration.HISCORE_ID );

        final PlatformConfiguration config = keys.platformConfiguration;
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

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        final Keys keys = aEngine.keys;
        if ( keys.checkRightSoftAndConsume() ) aEngine.popScreen( this );

        super.onControlTick( aEngine );

        if ( keys.checkUpAndConsume() ) myLineOffset--;
        if ( keys.checkDownAndConsume() ) myLineOffset++;

        final int linesOnScreen = linesOnScreen();
        if ( myLineOffset + linesOnScreen >= myTextLines.size ) myLineOffset = myTextLines.size - linesOnScreen;
        if ( myLineOffset < 0 ) myLineOffset = 0;
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        super.onDrawFrame( aScreen );

        final Graphics gc = aScreen.graphics();
        myTitleFont.blitString( gc, "INFO", myHelpPos, FontGen.CENTER );

        if ( myTextLines.size < 1 ) return;

        final int linesOnScreen = linesOnScreen();

        final Object[] lines = myTextLines.objects;
        for ( int idx = 0; idx < linesOnScreen; idx++ )
            {
            if ( myLineOffset + idx >= myTextLines.size ) break;

            final String line = (String) lines[ myLineOffset + idx ];
            myBlitPos.x = aScreen.width() / 2;
            myBlitPos.y = titleHeight() + idx * lineHeight();
            myTextFont.blitString( gc, line, myBlitPos, FontGen.CENTER );
            }

        myBlitPos.x = 0;
        myBlitPos.y = 0;
        myTextFont.blitNumber( gc, myBlitPos, engine().keys.lastCode, FontGen.TOP_LEFT );
        myBlitPos.y = myTextFont.charHeight();
        myTextFont.blitNumber( gc, myBlitPos, engine().keys.lastAction, FontGen.TOP_LEFT );
        }

    private final int linesOnScreen()
        {
        final int textHeight = (screen().height() - titleHeight());
        return (textHeight / lineHeight() - 1);
        }

    private final int lineHeight()
        {
        return myTextFont.charHeight() * 3 / 2;
        }

    private final int titleHeight()
        {
        return myTitleFont.charHeight() * 3;
        }



    private int myLineOffset = 0;

    private SoftkeysScreen mySoftkeys;

    private final FontGen myTextFont;

    private final FontGen myTitleFont;

    private final Position myBlitPos = new Position();

    private final Position myHelpPos = new Position();

    private final DynamicArray myTextLines = new DynamicArray( 25, 25 );
    }
