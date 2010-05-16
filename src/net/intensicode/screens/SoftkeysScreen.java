package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.Position;

public class SoftkeysScreen extends ScreenBase
    {
    public float touchableOutsetsFactor = 0.25f;


    public SoftkeysScreen( final FontGenerator aFont )
        {
        myFontGen = aFont;
        //#if TOUCH
        myLeftTouchRect.associatedKeyID = KeysHandler.LEFT_SOFT;
        myRightTouchRect.associatedKeyID = KeysHandler.RIGHT_SOFT;
        //#endif
        setPositionInPercent( 100 );
        }

    public final void setButtonImage( final ImageResource aButtonImage )
        {
        myButtonImage = aButtonImage;
        mySomethingChanged = true;
        }

    public final int getButtonHeight()
        {
        if ( myButtonImage != null ) return myButtonImage.getHeight();
        if ( myFontGen != null ) return myFontGen.charHeight();
        return 0;
        }

    public final void setSoftkeys( final String aLeftText, final String aRightText )
        {
        myLeftText = aLeftText;
        myRightText = aRightText;
        mySomethingChanged = true;
        }

    public final void setInsets( final int aHorizontalPixels, final int aVerticalPixels )
        {
        myInsetX = aHorizontalPixels;
        myInsetY = aVerticalPixels;
        mySomethingChanged = true;
        }

    public final void setPositionInPercent( final int aVerticalPositionInPercent )
        {
        myVerticalPositionMode = MODE_VERTICAL_POSITION_PERCENT;
        myVerticalPositionValue = aVerticalPositionInPercent;
        mySomethingChanged = true;
        }

    public final void setVerticalPosition( final int aVerticalPositionInPixels )
        {
        myVerticalPositionMode = MODE_VERTICAL_POSITION_ABSOLUTE;
        myVerticalPositionValue = aVerticalPositionInPixels;
        mySomethingChanged = true;
        }

    // From ScreenBase

    public void onControlTick()
        {
        //#if TOUCH
        if ( mySomethingChanged ) updateTouchableAreas();
        else tickTouchableAreas();
        //#endif
        mySomethingChanged = false;
        }

    public void onDrawFrame()
        {
        final DirectGraphics graphics = graphics();

        if ( hasLeftText() )
            {
            setPosition( 0, myLeftText );
            if ( myButtonImage != null )
                {
                graphics.drawImage( myButtonImage, myPosition.x, myPosition.y, ALIGN_TOP_LEFT );
                }
            blitTextString( myLeftText, ALIGN_TOP_LEFT );
            }

        if ( hasRightText() )
            {
            setPosition( screen().width(), myRightText );
            if ( myButtonImage != null )
                {
                graphics.drawImage( myButtonImage, myPosition.x, myPosition.y, ALIGN_TOP_RIGHT );
                }
            blitTextString( myRightText, ALIGN_TOP_RIGHT );
            }
        }

    // Protected API

    protected void setPosition( final int aHorizontalPosition, final String aText )
        {
        final int height = getAlignHeight( aText );
        myPosition.x = aHorizontalPosition;
        myPosition.y = calcVerticalPosition( height );
        }

    protected int getAlignWidth( final String aText )
        {
        if ( myButtonImage != null ) return myButtonImage.getWidth();
        if ( aText == null ) return myInsetX * 2;
        return myFontGen.stringWidth( aText ) + myInsetX * 2;
        }

    protected int getAlignHeight( final String aText )
        {
        if ( myButtonImage != null ) return myButtonImage.getHeight();
        return myFontGen.charHeight() + myInsetY * 2;
        }

    protected int getTextHeight( final String aText )
        {
        return myFontGen.charHeight();
        }

    protected void blitTextString( final String aText, final int aAlignment )
        {
        final int alignWidth = getAlignWidth( aText );
        final int alignHeight = getAlignHeight( aText );
        final int x = myPosition.x;
        final int y = myPosition.y;
        final Position aligned = DirectGraphics.getAlignedPosition( x, y, alignWidth, alignHeight, aAlignment );
        final int xOffset = getOffsetX( aText );
        final int yOffset = getOffsetY( aText );
        final DirectGraphics graphics = graphics();
        myFontGen.blitString( graphics, aText, 0, aText.length(), aligned.x + xOffset, aligned.y + yOffset );
        }

    protected int getOffsetX( final String aText )
        {
        if ( myButtonImage != null ) return ( myButtonImage.getWidth() - myFontGen.stringWidth( aText ) ) / 2;
        else return myInsetX;
        }

    protected int getOffsetY( final String aText )
        {
        if ( myButtonImage != null ) return ( myButtonImage.getHeight() - myFontGen.charHeight() ) / 2;
        else return myInsetY;
        }

    // Implementation

    private boolean hasLeftText()
        {
        return myLeftText != null && myLeftText.length() > 0;
        }

    private boolean hasRightText()
        {
        return myRightText != null && myRightText.length() > 0;
        }

    private int calcVerticalPosition( final int aObjectHeight )
        {
        if ( myVerticalPositionMode == MODE_VERTICAL_POSITION_ABSOLUTE )
            {
            return myVerticalPositionValue;
            }
        final int availableHeight = screen().height() - aObjectHeight;
        return availableHeight * myVerticalPositionValue / ONE_HUNDRED_PERCENT;
        }

    //#if TOUCH

    private void updateTouchableAreas()
        {
        updateTouchableArea( myLeftTouchRect, myLeftText, 0 );
        final int alignWidth = getAlignWidth( myRightText );
        updateTouchableArea( myRightTouchRect, myRightText, screen().width() - alignWidth );
        }

    private void tickTouchableAreas()
        {
        if ( hasLeftText() ) touch().addLocalControl( myLeftTouchRect );
        if ( hasRightText() ) touch().addLocalControl( myRightTouchRect );
        }

    private void updateTouchableArea( final net.intensicode.touch.TouchableArea aTouchableArea, final String aText, final int aHorizontalPosition )
        {
        if ( aText == null || aText.length() == 0 ) return;

        setPosition( aHorizontalPosition, aText );
        aTouchableArea.rectangle.x = myPosition.x;
        aTouchableArea.rectangle.y = myPosition.y;
        aTouchableArea.rectangle.width = getAlignWidth( aText );
        aTouchableArea.rectangle.height = getAlignHeight( aText );
        aTouchableArea.activateMode = net.intensicode.touch.Touchable.ACTIVATE_ONLY_ON_DOWN;
        aTouchableArea.triggerMode = net.intensicode.touch.Touchable.TRIGGER_ON_UP;
        aTouchableArea.releaseMode = net.intensicode.touch.Touchable.RELEASE_IMMEDIATELY;
        touch().addLocalControl( aTouchableArea );

        final int size = Math.min( aTouchableArea.rectangle.width, aTouchableArea.rectangle.height );
        final float outsets = size * touchableOutsetsFactor;
        aTouchableArea.rectangle.applyOutsets( (int) outsets );
        }

    //#endif


    protected int myInsetX = 4;

    protected int myInsetY = 2;

    private String myLeftText;

    private String myRightText;

    protected ImageResource myButtonImage;

    private int myVerticalPositionMode;

    private int myVerticalPositionValue;

    private boolean mySomethingChanged;


    protected final FontGenerator myFontGen;

    protected final Position myPosition = new Position();

    //#if TOUCH

    private final net.intensicode.touch.TouchableArea myLeftTouchRect = new net.intensicode.touch.TouchableArea();

    private final net.intensicode.touch.TouchableArea myRightTouchRect = new net.intensicode.touch.TouchableArea();

    //#endif


    private static final int ONE_HUNDRED_PERCENT = 100;

    private static final int MODE_VERTICAL_POSITION_PERCENT = 0;

    private static final int MODE_VERTICAL_POSITION_ABSOLUTE = 1;

    private static final int ALIGN_TOP_LEFT = DirectGraphics.ALIGN_LEFT;

    private static final int ALIGN_TOP_RIGHT = DirectGraphics.ALIGN_RIGHT;
    }
