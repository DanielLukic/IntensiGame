package net.intensicode.screens;

import net.intensicode.core.DirectGraphics;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;
import net.intensicode.touch.*;

public final class ScrollTextScreen extends MultiScreen
    {
    public static final int ALIGN_LEFT = 0;

    public static final int ALIGN_RIGHT = 1;

    public static final int ALIGN_CENTER = 2;

    public int alignment = ALIGN_LEFT;

    public Rectangle optionalBoundingBox;

    public boolean showIndicators;

    //#if TOUCH

    public boolean useTouchSlider;

    //#endif

    public boolean handleKeys;

    public String text;


    public ScrollTextScreen( final FontGenerator aTextFont )
        {
        Assert.notNull( "valid font", aTextFont );
        myFont = aTextFont;
        myIndicators = new ScrollTextIndicators( myFont );
        }

    public final void changeIndicatorFont( final FontGenerator aFontGenerator )
        {
        myIndicators.changeFont( aFontGenerator );
        }

    // From MultiScreen

    public void onInitOnce() throws Exception
        {
        addScreen( myIndicators );
        }

    public void onInitEverytime() throws Exception
        {
        if ( myFullScreenRect == null ) return;
        myFullScreenRect.width = screen().width();
        myFullScreenRect.height = screen().height();
        }

    public final void onControlTick() throws Exception
        {
        if ( someDataMissing() ) return;

        super.onControlTick();

        setVisibility( myIndicators, showIndicators );

        updateIndicators();

        if ( handleKeys ) scrollWithKeys();
        //#if TOUCH
        if ( useTouchSlider ) scrollWithSlider();
        //#endif
        }

    private boolean someDataMissing()
        {
        return myFont == null || text == null || text.length() == 0;
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
        final int linesOnScreen = determineTextRect().height / myFont.charHeight();
        final int maxIndex = myTextLines.size - linesOnScreen + ( showIndicators ? 2 : 0 );

        if ( myScrollIndex < maxIndex ) myScrollIndex++;
        else myScrollIndex = maxIndex;
        }

    private void updateTextLinesIfNecessary()
        {
        final Rectangle textRect = determineTextRect();
        if ( myKnownWidth == textRect.width && text.equals( myKnownText ) ) return;
        Log.debug( "breaking text into lines - GC heavy operation - avoid if possible" );
        myTextLines = StringUtils.breakIntoLines( text, myFont, textRect.width );
        myKnownText = text;
        myKnownWidth = textRect.width;
        }

    private Rectangle determineTextRect()
        {
        if ( optionalBoundingBox != null ) return optionalBoundingBox;
        if ( myFullScreenRect != null ) return myFullScreenRect;
        myFullScreenRect = new Rectangle( 0, 0, screen().width(), screen().height() );
        return myFullScreenRect;
        }

    //#if TOUCH

    private void scrollWithSlider()
        {
        if ( myTouchSlider == null ) initTouchSliderIfNecessary();

        final int fontHeight = myFont.charHeight();
        myTouchSlider.stepSizeInPixels.setTo( fontHeight, fontHeight );

        final Rectangle textRect = determineTextRect();
        myTouchSlider.touchableArea.setTo( textRect );

        final int steps = myTouchSlider.slideSteps.y;
        if ( steps == 0 ) return;

        updateTextLinesIfNecessary();

        if ( steps > 0 )
            {
            for ( int idx = 0; idx < steps; idx++ )
                {
                scrollUp();
                }
            }
        else if ( steps < 0 )
            {
            for ( int idx = 0; idx < Math.abs( steps ); idx++ )
                {
                scrollDown();
                }
            }

        myTouchSlider.clearSlideSteps();
        }

    private void initTouchSliderIfNecessary()
        {
        if ( myTouchSlider != null ) return;

        final TouchSliderConfiguration configuration = new TouchSliderConfiguration();
        configuration.setTo( touch().sharedSliderConfiguration );
        configuration.additionalStepThresholdInPixels = myFont.charHeight();
        configuration.initialStepThresholdInPixels = 0;
        configuration.newSlideStartThresholdInMillis = 2500;
        configuration.slideMoveThresholdInPixels = 0;
        configuration.slideStartThresholdInMillis = 25;
        configuration.slideStartThresholdInPixels = 0;
        myTouchSlider = new TouchSlider( configuration );

        touch().addListener( myTouchSlider );
        }

    private net.intensicode.touch.TouchSlider myTouchSlider;

    //#endif

    private void updateIndicators()
        {
        final Rectangle rect = determineTextRect();
        myIndicators.upPosition.x = rect.x + rect.width / 2;
        myIndicators.upPosition.y = rect.y + myFont.charHeight() / 2;

        myIndicators.downPosition.x = rect.x + rect.width / 2;
        myIndicators.downPosition.y = rect.y + rect.height - myFont.charHeight() / 2;

        updateTextLinesIfNecessary();

        final int linesOnScreen = determineTextRect().height / myFont.charHeight();
        final int maxIndex = myTextLines.size - linesOnScreen + ( showIndicators ? 2 : 0 );

        myIndicators.showUpIndicator = myScrollIndex > 0;
        myIndicators.showDownIndicator = myScrollIndex < maxIndex;
        }

    public final void onDrawFrame()
        {
        if ( someDataMissing() ) return;

        super.onDrawFrame();

        updateTextLinesIfNecessary();

        final Rectangle textRect = determineTextRect();
        blitLines( textRect );
        }

    private void blitLines( final Rectangle aTextRect )
        {
        final int fontHeight = myFont.charHeight();

        final DirectGraphics graphics = graphics();

        myBlitPos.x = aTextRect.x;
        myBlitPos.y = aTextRect.y;

        if ( showIndicators ) myBlitPos.y += myFont.charHeight();

        final int maxLowerBoundary = aTextRect.y + aTextRect.height - ( showIndicators ? fontHeight : 0 );

        for ( int idx = myScrollIndex; idx < myTextLines.size; idx++ )
            {
            final String line = (String) myTextLines.get( idx );

            myBlitPos.x = aTextRect.x;
            if ( alignment != ALIGN_LEFT ) myBlitPos.x += ( aTextRect.width - myFont.stringWidth( line ) ) / alignment;

            myFont.blitString( graphics, line, myBlitPos, FontGenerator.TOP_LEFT );
            myBlitPos.y += fontHeight;

            final int nextLowerBoundary = myBlitPos.y + fontHeight;
            if ( nextLowerBoundary >= maxLowerBoundary ) break;
            }
        }


    private int myScrollIndex;

    private int myKnownWidth;

    private String myKnownText;

    private DynamicArray myTextLines;

    private Rectangle myFullScreenRect;

    private ScrollTextIndicators myIndicators;

    private final FontGenerator myFont;

    private final Position myBlitPos = new Position();
    }
