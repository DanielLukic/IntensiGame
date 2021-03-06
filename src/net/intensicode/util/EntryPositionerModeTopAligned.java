package net.intensicode.util;

import net.intensicode.graphics.FontGenerator;

public class EntryPositionerModeTopAligned extends EntryPositionerMode
    {
    public static final EntryPositionerModeTopAligned INSTANCE = new EntryPositionerModeTopAligned();

    public final void update( final DynamicArray aEntries, final Rectangle aBounds, final FontGenerator aFont )
        {
        final int availableHeight = aBounds.height - aFont.charHeight();
        final float spacing = calcSuitableSpacing( availableHeight, aEntries.size - 1, aFont.charHeight() );

        final int offsetX = aBounds.x;
        final int offsetY = aBounds.y + aFont.charHeight() / 2;

        final int numberOfEntries = aEntries.size;
        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final PositionableEntry entry = (PositionableEntry) aEntries.get( idx );
            entry.setAvailableWidth( aBounds.width );

            final Position position = entry.getPositionByReference();
            position.x = offsetX;
            position.y = (int) ( offsetY + idx * spacing );
            }
        }

    private EntryPositionerModeTopAligned()
        {
        }
    }
