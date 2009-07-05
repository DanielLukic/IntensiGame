package net.intensicode.screens;

import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.core.Keys;
import net.intensicode.core.MultiScreen;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;

import javax.microedition.lcdui.Graphics;

/**
 * TODO: Describe this!
 */
public final class StringEntryScreen extends MultiScreen
    {
    public StringEntryScreen( final FontGen aTitleFont, final FontGen aTextFont )
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

    // From AbstractScreen

    public final void onInitOnce( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        if ( mySoftkeys == null ) mySoftkeys = new SoftkeysScreen( myTextFont );

        addScreen( mySoftkeys );

        myTitlePos.x = aScreen.width() / 2;
        myTitlePos.y = myTitleFont.charHeight();
        }

    public final void onInitEverytime( final Engine aEngine, final DirectScreen aScreen ) throws Exception
        {
        myCurrentCharIndex = 'A';

        final int tps = Engine.ticksPerSecond;

        final Keys keys = aEngine.keys;
        keys.reset( tps );

        keys.keyRepeatDelayInTicks = tps * 50 / 100;
        keys.keyRepeatIntervalInTicks = tps * 10 / 100;
        keys.dontRepeatFlags[ Keys.UP ] = true;
        keys.dontRepeatFlags[ Keys.DOWN ] = true;
        keys.dontRepeatFlags[ Keys.FIRE1 ] = true;
        keys.dontRepeatFlags[ Keys.FIRE2 ] = true;
        keys.dontRepeatFlags[ Keys.STICK_DOWN ] = true;
        keys.dontRepeatFlags[ Keys.LEFT_SOFT ] = true;
        keys.dontRepeatFlags[ Keys.RIGHT_SOFT ] = true;

        mySoftkeys.setSoftkeys( "OK", "CANCEL" );
        }

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        final Keys keys = aEngine.keys;

        mySoftkeys.setSoftkeys( nameValid() ? "OK" : "", "CANCEL" );

        if ( nameValid() && keys.checkLeftSoftAndConsume() )
            {
            engine().popScreen( this );
            }
        if ( keys.checkRightSoftAndConsume() )
            {
            myCurrentValue = myInitialValue;
            engine().popScreen( this );
            }

        if ( myCurrentCharIndex == MAX_CHAR_INDEX && keys.checkFireAndConsume() ) removeLastChar();
        else if ( nameMaxLength() == false && keys.checkFireAndConsume() ) addCurrentChar();

        if ( keys.checkLeftAndConsume() ) myCurrentCharIndex--;
        if ( keys.checkRightAndConsume() ) myCurrentCharIndex++;

        if ( myCurrentCharIndex < MIN_CHAR_INDEX ) myCurrentCharIndex = MAX_CHAR_INDEX;
        else if ( myCurrentCharIndex > MAX_CHAR_INDEX ) myCurrentCharIndex = MIN_CHAR_INDEX;

        super.onControlTick( aEngine );
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        super.onDrawFrame( aScreen );

        final Graphics gc = aScreen.graphics();

        myBlitPos.setTo( myTitlePos );
        myTitleFont.blitString( gc, myTitle, myBlitPos, FontGen.CENTER );

        myBlitPos.y += myTitleFont.charHeight();
        myTextFont.blitString( gc, myInfo, myBlitPos, FontGen.CENTER );

        final int refWidth = myTitleFont.stringWidth( "X" );

        final int x = aScreen.width() / 2 - refWidth * 2 / 3;
        final int y = aScreen.height() / 2 - myTitleFont.charHeight() / 2;
        final int width = refWidth * 3 / 2 - refWidth / 4;
        final int height = myTitleFont.charHeight();

        gc.setColor( 0x800000 );
        gc.fillRect( x, y, width, height );
        gc.setColor( 0xC04040 );
        gc.drawRect( x, y, width, height );

        myBlitPos.y = aScreen.height() / 2 - myTitleFont.charHeight();

        for ( int idx = -10; idx < 10; idx++ )
            {
            int charCode = myCurrentCharIndex + idx;
            if ( charCode < MIN_CHAR_INDEX ) charCode += NUM_CHARS;
            if ( charCode > MAX_CHAR_INDEX ) charCode -= NUM_CHARS;
            if ( charCode == MAX_CHAR_INDEX ) charCode += 32;

            final int yOffset = Math.abs( idx ) * myTitleFont.charHeight() / 25;

            myBlitPos.x = aScreen.width() / 2 + idx * refWidth * 3 / 2;
            myBlitPos.y = aScreen.height() / 2 + yOffset * yOffset;

            final FontGen font = (idx != 1000) ? myTitleFont : myTextFont;
            font.blitChar( gc, myBlitPos.x - refWidth / 2, myBlitPos.y - font.charHeight() / 2, charCode );
            }

        myBlitPos.x = aScreen.width() / 2;
        myBlitPos.y = aScreen.height() * 3 / 4;

        myTitleFont.blitString( gc, myCurrentValue, myBlitPos, FontGen.CENTER );
        }

    // Implementation

    private final void addCurrentChar()
        {
        final StringBuffer buffer = new StringBuffer( myCurrentValue );
        buffer.append( (char) myCurrentCharIndex );
        myCurrentValue = buffer.toString();
        }

    private final void removeLastChar()
        {
        if ( myCurrentValue.length() == 0 ) return;
        myCurrentValue = myCurrentValue.substring( 0, myCurrentValue.length() - 1 );
        }

    private final boolean nameValid()
        {
        final int length = myCurrentValue.length();
        return length >= myMinLength && length <= myMaxLength;
        }

    private final boolean nameMaxLength()
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


    private final FontGen myTextFont;

    private final FontGen myTitleFont;

    private final Position myBlitPos = new Position();

    private final Position myTitlePos = new Position();


    private static final char MIN_CHAR_INDEX = ' ';

    private static final char MAX_CHAR_INDEX = '_';

    private static final int NUM_CHARS = MAX_CHAR_INDEX - MIN_CHAR_INDEX + 1;
    }
