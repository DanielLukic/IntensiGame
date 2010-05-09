package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public final class ScrollTextScreen extends ScreenBase
    {
    public Rectangle optionalBoundingBox;

    public boolean showIndicators;

    //#if TOUCH

    public boolean useTouchSlider;

    //#endif

    public boolean handleKeys;

    public FontGenerator font;

    public String text;


    public void onInitEverytime() throws Exception
        {
        if ( myFullScreenRect == null ) return;
        myFullScreenRect.width = screen().width();
        myFullScreenRect.height = screen().height();
        }

    public final void onControlTick() throws Exception
        {
        if ( someDataMissing() ) return;

        if ( handleKeys ) scrollWithKeys();
        //#if TOUCH
        if ( useTouchSlider ) scrollWithSlider();
        if ( showIndicators ) tickTouchableIndicators();
        //#endif
        }

    private boolean someDataMissing()
        {
        return font == null || text == null || text.length() == 0;
        }

    private void scrollWithKeys()
        {
        if ( keys().checkUpAndConsume() ) scrollUp();
        if ( keys().checkDownAndConsume() ) scrollDown();
        }

    private void scrollUp()
        {
        if ( myScrollIndex > 0 ) myScrollIndex--;
        }

    private void scrollDown()
        {
        final int previousIndex = myScrollIndex;

        myScrollIndex++;

        final Rectangle textRect = determineTextRect();
        updateTextLinesIfNecessary( textRect.width );

        final int textHeightFromNewIndex = determineTextHeight();
        final int maxLastLineHeight = font.charHeight() * 3 / 2;
        if ( textHeightFromNewIndex + maxLastLineHeight >= textRect.height ) return;

        myScrollIndex = previousIndex;
        }

    private int determineTextHeight()
        {
        int textHeight = 0;

        final int fontHeight = font.charHeight();
        for ( int idx = myScrollIndex; idx < myTextLines.size; idx++ )
            {
            final String line = (String) myTextLines.get( idx );
            if ( line.endsWith( "\n" ) ) textHeight += fontHeight * 3 / 2;
            else textHeight += fontHeight;
            }

        return textHeight;
        }

    //#if TOUCH

    private void scrollWithSlider()
        {
        if ( myTouchSlider == null ) initTouchSliderIfNecessary();

        final int fontHeight = font.charHeight();
        myTouchSlider.stepSizeInPixels.setTo( fontHeight, fontHeight );

        final Rectangle textRect = determineTextRect();
        myTouchSlider.touchableArea.setTo( textRect );

        final int steps = myTouchSlider.slideSteps.y;
        if ( steps == 0 ) return;

        myScrollIndex -= steps;
        if ( myScrollIndex < 0 )
            {
            myScrollIndex = 0;
            }

        updateTextLinesIfNecessary( textRect.width );

        final int textHeightFromNewIndex = determineTextHeight();
        final int maxLastLineHeight = font.charHeight() * 3 / 2;
        if ( textHeightFromNewIndex + maxLastLineHeight < textRect.height )
            {
            myScrollIndex += steps;
            }

        myTouchSlider.clearSlideSteps();
        }

    private void initTouchSliderIfNecessary()
        {
        if ( myTouchSlider != null ) return;

        myTouchSlider = new net.intensicode.touch.TouchSlider( new net.intensicode.touch.TouchSliderConfiguration() );
        touch().addListener( myTouchSlider );
        }

    private void tickTouchableIndicators()
        {
        final Rectangle textRect = determineTextRect();

        if ( myScrollIndex > 0 )
            {
            if ( myUpIndicator == null )
                {
                myBlitPos.x = textRect.x + textRect.width / 2;
                myBlitPos.y = textRect.y - font.charHeight();
                myUpIndicator = new net.intensicode.touch.TouchableArea();
                myUpIndicator.associatedKeyID = KeysHandler.UP;
                myUpIndicator.rectangle.setCenterAndSize( myBlitPos, font.stringWidth( UP_INDICATOR_LABEL ), font.charHeight() );
                }
            touch().addLocalControl( myUpIndicator );
            }

        final int textHeightFromNewIndex = determineTextHeight();
        if ( textHeightFromNewIndex >= textRect.height )
            {
            if ( myDownIndicator == null )
                {
                myBlitPos.x = textRect.x + textRect.width / 2;
                myBlitPos.y = textRect.y + textRect.height + font.charHeight();
                myDownIndicator = new net.intensicode.touch.TouchableArea();
                myDownIndicator.associatedKeyID = KeysHandler.DOWN;
                myDownIndicator.rectangle.setCenterAndSize( myBlitPos, font.stringWidth( DOWN_INDICATOR_LABEL ), font.charHeight() );
                }
            touch().addLocalControl( myDownIndicator );
            }
        }

    private net.intensicode.touch.TouchSlider myTouchSlider;

    private net.intensicode.touch.TouchableArea myUpIndicator;

    private net.intensicode.touch.TouchableArea myDownIndicator;

    //#endif

    public final void onDrawFrame()
        {
        if ( someDataMissing() ) return;

        final Rectangle textRect = determineTextRect();
        updateTextLinesIfNecessary( textRect.width );

        blitLines( textRect );

        if ( showIndicators ) blitIndicators( textRect );
        }

    private void blitLines( final Rectangle aTextRect )
        {
        final int maxLowerBoundary = aTextRect.y + aTextRect.height;

        final int fontHeight = font.charHeight();

        final DirectGraphics graphics = graphics();

        myBlitPos.x = aTextRect.x;
        myBlitPos.y = aTextRect.y;

        for ( int idx = myScrollIndex; idx < myTextLines.size; idx++ )
            {
            final String line = (String) myTextLines.get( idx );
            font.blitString( graphics, line, myBlitPos, FontGenerator.TOP_LEFT );

            if ( line.endsWith( "\n" ) ) myBlitPos.y += fontHeight * 3 / 2;
            else myBlitPos.y += fontHeight;

            final int nextLowerBoundary = myBlitPos.y + fontHeight;
            if ( nextLowerBoundary >= maxLowerBoundary ) break;
            }
        }

    private void blitIndicators( final Rectangle aTextRect )
        {
        if ( myScrollIndex > 0 )
            {
            myBlitPos.x = aTextRect.x + aTextRect.width / 2;
            myBlitPos.y = aTextRect.y - font.charHeight();
            font.blitString( graphics(), UP_INDICATOR_LABEL, myBlitPos, FontGenerator.CENTER );
            }

        final int textHeightFromNewIndex = determineTextHeight();
        if ( textHeightFromNewIndex >= aTextRect.height )
            {
            myBlitPos.x = aTextRect.x + aTextRect.width / 2;
            myBlitPos.y = aTextRect.y + aTextRect.height + font.charHeight();
            font.blitString( graphics(), DOWN_INDICATOR_LABEL, myBlitPos, FontGenerator.CENTER );
            }
        }

    private void updateTextLinesIfNecessary( final int aRenderWidth )
        {
        if ( myKnownText == text ) return;
        if ( text.equals( myKnownText ) ) return;
        Log.debug( "breaking text into lines - GC heavy operation - avoid if possible" );
        myTextLines = StringUtils.breakIntoLines( text, font, aRenderWidth );
        myKnownText = text;
        }

    private Rectangle determineTextRect()
        {
        if ( optionalBoundingBox != null ) return optionalBoundingBox;
        if ( myFullScreenRect != null ) return myFullScreenRect;
        myFullScreenRect = new Rectangle( 0, 0, screen().width(), screen().height() );
        return myFullScreenRect;
        }


    private int myScrollIndex;

    private String myKnownText;

    private DynamicArray myTextLines;

    private Rectangle myFullScreenRect;

    private final Position myBlitPos = new Position();

    private static final String UP_INDICATOR_LABEL = "[UP]";

    private static final String DOWN_INDICATOR_LABEL = "[DOWN]";
    }
