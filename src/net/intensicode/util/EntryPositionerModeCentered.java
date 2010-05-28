package net.intensicode.util;

import net.intensicode.graphics.FontGenerator;

public class EntryPositionerModeCentered extends EntryPositionerMode
    {
    public static final EntryPositionerModeCentered INSTANCE = new EntryPositionerModeCentered();

    public final void update( final DynamicArray aEntries, final Rectangle aBounds, final FontGenerator aFont )
        {
        final int numberOfEntries = aEntries.size;
        final float spacing = calcSuitableSpacing( aBounds.height, aEntries.size, aFont.charHeight() );
        final int entriesOffset = (int) ( ( aBounds.height - numberOfEntries * spacing ) / 2 );

        final int offsetX = aBounds.x;
        final int offsetY = (int) ( aBounds.y + entriesOffset + spacing / 2 );

        for ( int idx = 0; idx < numberOfEntries; idx++ )
            {
            final PositionableEntry entry = (PositionableEntry) aEntries.get( idx );
            entry.setAvailableWidth( aBounds.width );

            final Position position = entry.getPositionByReference();
            position.x = offsetX;
            position.y = (int) ( offsetY + idx * spacing );
            }
        }

    private EntryPositionerModeCentered()
        {
        }
    }
