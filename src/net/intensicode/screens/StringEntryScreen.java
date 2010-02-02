package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.core.KeysHandler;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public final class StringEntryScreen extends MultiScreen
    {
    public StringEntryScreen( final FontGenerator aTitleFont, final FontGenerator aTextFont )
        {
        myTitleFont = aTitleFont;
        myTextFont = aTextFont;
        }

    public final void setSoftkeys( final SoftkeysScreen aSoftkeys )
        {
        mySoftkeys = aSoftkeys;
        }

    public final void setInfo( final String aTitle, final String aInfo )
        {
        myTitle = aTitle;
        myInfo = aInfo;
        }

    public final void setString( final String aString, final int aMinLength, final int aMaxLength )
        {
        //#if DEBUG
        final int length = aString.length();
        if ( length < aMinLength || length > aMaxLength ) throw new IllegalArgumentException( aString );
        //#endif

        myMinLength = aMinLength;
        myMaxLength = aMaxLength;
        myCurrentValue = myInitialValue = aString;
        }

    public final String getResult()
        {
        return myCurrentValue;
        }

    // From ScreenBase

    public final void onInitOnce() throws Exception
        {
        if ( mySoftkeys == null ) mySoftkeys = new SoftkeysScreen( myTextFont );

        addScreen( mySoftkeys );

        myTitlePos.x = screen().width() / 2;
        myTitlePos.y = myTitleFont.charHeight();
        }

    public final void onInitEverytime() throws Exception
        {
        myCurrentCharIndex = 'A';

        final int tps = system().timing.ticksPerSecond;

        final KeysHandler keys = system().keys;
        keys.reset( tps );

        keys.keyRepeatDelayInTicks = tps * 50 / 100;
        keys.keyRepeatIntervalInTicks = tps * 10 / 100;
        keys.dontRepeatFlags[ KeysHandler.UP ] = true;
        keys.dontRepeatFlags[ KeysHandler.DOWN ] = true;
        keys.dontRepeatFlags[ KeysHandler.FIRE1 ] = true;
        keys.dontRepeatFlags[ KeysHandler.FIRE2 ] = true;
        keys.dontRepeatFlags[ KeysHandler.STICK_DOWN ] = true;
        keys.dontRepeatFlags[ KeysHandler.LEFT_SOFT ] = true;
        keys.dontRepeatFlags[ KeysHandler.RIGHT_SOFT ] = true;

        mySoftkeys.setSoftkeys( "OK", "CANCEL" );
        }

    public final void onControlTick() throws Exception
        {
        final KeysHandler keys = keys();

        mySoftkeys.setSoftkeys( nameValid() ? "OK" : "", "CANCEL" );

        if ( nameValid() && keys.checkLeftSoftAndConsume() )
            {
            stack().popScreen( this );
            }
        if ( keys.checkRightSoftAndConsume() )
            {
            myCurrentValue = myInitialValue;
            stack().popScreen( this );
            }

        if ( myCurrentCharIndex == MAX_CHAR_INDEX && keys.checkFireAndConsume() ) removeLastChar();
        else if ( !nameMaxLength() && keys.checkFireAndConsume() ) addCurrentChar();

        if ( keys.checkLeftAndConsume() ) myCurrentCharIndex--;
        if ( keys.checkRightAndConsume() ) myCurrentCharIndex++;

        if ( myCurrentCharIndex < MIN_CHAR_INDEX ) myCurrentCharIndex = MAX_CHAR_INDEX;
        else if ( myCurrentCharIndex > MAX_CHAR_INDEX ) myCurrentCharIndex = MIN_CHAR_INDEX;

        super.onControlTick();
        }

    public final void onDrawFrame()
        {
        super.onDrawFrame();

        final DirectGraphics graphics = graphics();

        myBlitPos.setTo( myTitlePos );
        myTitleFont.blitString( graphics, myTitle, myBlitPos, FontGenerator.CENTER );

        myBlitPos.y += myTitleFont.charHeight();
        myTextFont.blitString( graphics, myInfo, myBlitPos, FontGenerator.CENTER );

        final int refWidth = myTitleFont.stringWidth( "X" );

        final int x = screen().width() / 2 - refWidth * 2 / 3;
        final int y = screen().height() / 2 - myTitleFont.charHeight() / 2;
        final int width = refWidth * 3 / 2 - refWidth / 4;
        final int height = myTitleFont.charHeight();

        graphics.setColorRGB24( 0x800000 );
        graphics.fillRect( x, y, width, height );
        graphics.setColorRGB24( 0xC04040 );
        graphics.drawRect( x, y, width, height );

        myBlitPos.y = screen().height() / 2 - myTitleFont.charHeight();

        for ( int idx = -10; idx < 10; idx++ )
            {
            int charCode = myCurrentCharIndex + idx;
            if ( charCode < MIN_CHAR_INDEX ) charCode += NUM_CHARS;
            if ( charCode > MAX_CHAR_INDEX ) charCode -= NUM_CHARS;
            if ( charCode == MAX_CHAR_INDEX ) charCode += 32;

            final int yOffset = Math.abs( idx ) * myTitleFont.charHeight() / 25;

            myBlitPos.x = screen().width() / 2 + idx * refWidth * 3 / 2;
            myBlitPos.y = screen().height() / 2 + yOffset * yOffset;

            final FontGenerator font = ( idx != 1000 ) ? myTitleFont : myTextFont;
            font.blitChar( graphics, myBlitPos.x - refWidth / 2, myBlitPos.y - font.charHeight() / 2, charCode );
            }

        myBlitPos.x = screen().width() / 2;
        myBlitPos.y = screen().height() * 3 / 4;

        myTitleFont.blitString( graphics, myCurrentValue, myBlitPos, FontGenerator.CENTER );
        }

    // Implementation

    private void addCurrentChar()
        {
        final StringBuffer buffer = new StringBuffer( myCurrentValue );
        buffer.append( (char) myCurrentCharIndex );
        myCurrentValue = buffer.toString();
        }

    private void removeLastChar()
        {
        if ( myCurrentValue.length() == 0 ) return;
        myCurrentValue = myCurrentValue.substring( 0, myCurrentValue.length() - 1 );
        }

    private boolean nameValid()
        {
        final int length = myCurrentValue.length();
        return length >= myMinLength && length <= myMaxLength;
        }

    private boolean nameMaxLength()
        {
        return myCurrentValue.length() == myMaxLength;
        }



    private String myInfo;

    private String myTitle;

    private int myMinLength;

    private int myMaxLength;

    private String myCurrentValue;

    private String myInitialValue;

    private int myCurrentCharIndex;

    private SoftkeysScreen mySoftkeys;


    private final FontGenerator myTextFont;

    private final FontGenerator myTitleFont;

    private final Position myBlitPos = new Position();

    private final Position myTitlePos = new Position();


    private static final char MIN_CHAR_INDEX = ' ';

    private static final char MAX_CHAR_INDEX = '_';

    private static final int NUM_CHARS = MAX_CHAR_INDEX - MIN_CHAR_INDEX + 1;
    }
