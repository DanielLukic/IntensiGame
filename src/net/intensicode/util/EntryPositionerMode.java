package net.intensicode.util;

import net.intensicode.graphics.FontGenerator;

abstract class EntryPositionerMode
    {
    public void update( final DynamicArray aEntries, final Rectangle aBounds, final FontGenerator aFont )
        {
        }

    // Protected API

    protected final float calcSuitableSpacing( final int aAvailableHeight, final int aNumberOfEntries, final int aFontHeight )
        {
        final float largeSpacing = aFontHeight * 2f;
        if ( largeSpacing * aNumberOfEntries <= aAvailableHeight ) return largeSpacing;

        final float mediumSpacing = aFontHeight * 3f / 2;
        if ( mediumSpacing * aNumberOfEntries <= aAvailableHeight ) return mediumSpacing;

        final float adjustedSpacing = aAvailableHeight / (float) aNumberOfEntries;
        if ( adjustedSpacing >= aFontHeight ) return adjustedSpacing;

        return aFontHeight;
        }
    }
