package net.intensicode.screens;

import net.intensicode.core.*;
import net.intensicode.graphics.FontGenerator;
import net.intensicode.util.*;

public class SoftkeysScreen extends ScreenBase
    {
    public SoftkeysScreen( final FontGenerator aFont )
        {
        myFontGen = aFont;
        }

    public final void setSoftkeys( final String aLeftButton, final String aRightButton )
        {
        //#if TOUCH_SUPPORTED
        myLeftChanged = hasButtonChanged( myLeftButton, aLeftButton );
        myRightChanged = hasButtonChanged( myRightButton, aRightButton );
        //#endif

        myLeftButton = aLeftButton;
        myRightButton = aRightButton;
        }

    public final void setInsets( final int aX, final int aY )
        {
        myOffsetX = aX;
        myOffsetY = aY;
        }

    // From ScreenBase

    public final void onTop()
        {
        //#if TOUCH_SUPPORTED
        updateLeftTouchButton();
        updateRightTouchButton();
        //#endif
        }

    public void onPop()
        {
        //#if TOUCH_SUPPORTED
        removeLeftTouchButton();
        removeRightTouchButton();
        //#endif
        }

    public void onControlTick()
        {
        //#if TOUCH_SUPPORTED
        if ( myLeftChanged ) updateLeftTouchButton();
        if ( myRightChanged ) updateRightTouchButton();
        myLeftChanged = myRightChanged = false;
        //#endif
        }

    public void onDrawFrame()
        {
        final DirectGraphics gc = graphics();

        if ( myLeftButton != null )
            {
            myBlitPosition.x = myOffsetX;
            myBlitPosition.y = screen().height() - myOffsetY;

            myFontGen.blitString( gc, myLeftButton, myBlitPosition, FontGenerator.LEFT | FontGenerator.BOTTOM );
            }

        if ( myRightButton != null )
            {
            myBlitPosition.x = screen().width() - myOffsetX;
            myBlitPosition.y = screen().height() - myOffsetY;

            myFontGen.blitString( gc, myRightButton, myBlitPosition, FontGenerator.RIGHT | FontGenerator.BOTTOM );
            }
        }

    // Implementation

    //#if TOUCH_SUPPORTED

    private boolean hasButtonChanged( final String aCurrentValue, final String aNewValue )
        {
        if ( aCurrentValue == null || aNewValue == null ) return aCurrentValue != aNewValue;
        return !aCurrentValue.equals( aNewValue );
        }

    private void updateLeftTouchButton()
        {
        if ( buttonValid( myLeftButton ) ) createLeftTouchButton();
        else removeLeftTouchButton();
        }

    private void updateRightTouchButton()
        {
        if ( buttonValid( myRightButton ) ) createRightTouchButton();
        else removeRightTouchButton();
        }

    private boolean buttonValid( final String aButtonTextOrNull )
        {
        return aButtonTextOrNull != null && aButtonTextOrNull.length() > 0;
        }

    // TODO: Clean up this touch button mess.. How!?

    private void createLeftTouchButton()
        {
        final Rectangle rectangle = myLeftTouchRect.rectangle;
        applyLabelBounds( rectangle, myLeftButton );
        rectangle.x = myOffsetX;
        rectangle.y = screen().height() - myOffsetY - rectangle.height;
        applyOutsets( rectangle );

        myLeftTouchRect.associatedKeyID = KeysHandler.LEFT_SOFT;
        system().touch.addLocalControl( myLeftTouchRect );
        }

    private void removeLeftTouchButton()
        {
        system().touch.removeLocalControl( myLeftTouchRect );
        }

    private void createRightTouchButton()
        {
        final Rectangle rectangle = myRightTouchRect.rectangle;
        applyLabelBounds( rectangle, myRightButton );
        rectangle.x = screen().width() - myOffsetX - rectangle.width;
        rectangle.y = screen().height() - myOffsetY - rectangle.height;
        applyOutsets( rectangle );

        myRightTouchRect.associatedKeyID = KeysHandler.RIGHT_SOFT;
        system().touch.addLocalControl( myRightTouchRect );
        }

    private void removeRightTouchButton()
        {
        system().touch.removeLocalControl( myRightTouchRect );
        }

    private void applyLabelBounds( final Rectangle aRectangle, final String aLabel )
        {
        aRectangle.width = myFontGen.stringWidth( aLabel );
        aRectangle.height = myFontGen.charHeight();
        }

    private void applyOutsets( final Rectangle aRectangle )
        {
        aRectangle.applyOutsets( aRectangle.height / 2 );
        }

    //#endif


    protected int myOffsetX = 4;

    protected int myOffsetY = 2;

    protected String myLeftButton;

    protected String myRightButton;

    protected final FontGenerator myFontGen;

    private final Position myBlitPosition = new Position();

    //#if TOUCH_SUPPORTED

    private boolean myLeftChanged;

    private boolean myRightChanged;

    private final TouchableArea myLeftTouchRect = new TouchableArea();

    private final TouchableArea myRightTouchRect = new TouchableArea();

    //#endif
    }
