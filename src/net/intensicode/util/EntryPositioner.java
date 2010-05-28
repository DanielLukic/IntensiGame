package net.intensicode.util;

import net.intensicode.core.DirectScreen;
import net.intensicode.graphics.FontGenerator;

public final class EntryPositioner
    {
    public EntryPositioner()
        {
        myMode = EntryPositionerModeCentered.INSTANCE;
        }

    public final void shareDirectScreen( final DirectScreen aScreen )
        {
        Assert.notNull( "screen must be valid", aScreen );
        myScreen = aScreen;
        }

    public final void setFont( final FontGenerator aFont )
        {
        Assert.notNull( "font must be valid", aFont );
        myFont = aFont;
        }

    public final void setBounds( final Rectangle aRectangelByReference )
        {
        myBoundsOrNull = aRectangelByReference;
        }

    public final void setBounds( final Position aPosition, final Size aSize )
        {
        if ( myBoundsOrNull == null ) myBoundsOrNull = new Rectangle();
        myBoundsOrNull.set( aPosition, aSize );
        }

    public final void clearBoundsAndUseFullScreen()
        {
        myBoundsOrNull = null;
        }

    public final void setCentered()
        {
        myMode = EntryPositionerModeCentered.INSTANCE;
        }

    public final void setTopAligned()
        {
        myMode = EntryPositionerModeTopAligned.INSTANCE;
        }

    public final void setManual( final EntryPositionerMode aManualMode )
        {
        Assert.notNull( "mode must be valid", aManualMode );
        myMode = aManualMode;
        }

    public final void updatePositions( final DynamicArray aListOfPositionableEntries )
        {
        Assert.notNull( "entries must be valid", aListOfPositionableEntries );
        Assert.notNull( "font must be valid", myFont );

        final Rectangle bounds = determineBounds();
        myMode.update( aListOfPositionableEntries, bounds, myFont );

        //#if TOUCH
        updateTouchables( aListOfPositionableEntries );
        //#endif
        }

    //#if TOUCH

    private void updateTouchables( final DynamicArray aListOfPositionableEntries )
        {
        for ( int idx = 0; idx < aListOfPositionableEntries.size; idx++ )
            {
            final PositionableEntry entry = (PositionableEntry) aListOfPositionableEntries.get( idx );
            entry.updateTouchable();
            }
        }

    //#endif

    private Rectangle determineBounds()
        {
        if ( myBoundsOrNull != null ) return myBoundsOrNull;
        updateFullScreenBounds();
        return myFullScreenBounds;
        }

    private void updateFullScreenBounds()
        {
        Assert.notNull( "screen must be valid", myScreen );
        if ( myFullScreenBounds == null ) myFullScreenBounds = new Rectangle();
        myFullScreenBounds.width = myScreen.width();
        myFullScreenBounds.height = myScreen.height();
        }


    private FontGenerator myFont;

    private DirectScreen myScreen;

    private Rectangle myBoundsOrNull;

    private EntryPositionerMode myMode;

    private Rectangle myFullScreenBounds;
    }
