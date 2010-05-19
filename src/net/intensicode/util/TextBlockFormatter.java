package net.intensicode.util;

import net.intensicode.graphics.FontGenerator;

public final class TextBlockFormatter
    {
    public FontGenerator font;

    public int blockWidth;

    public String text;


    public final DynamicArray getFormattedLines()
        {
        final DynamicArray lines = new DynamicArray();
        formatLinesInto( lines );
        return lines;
        }

    public final void formatLinesInto( final DynamicArray aLines )
        {
        extractLineStartinAt( 0, aLines );
        }

    private void extractLineStartinAt( final int aStartIndex, final DynamicArray aLines )
        {
        if ( aStartIndex >= text.length() ) return;

        final int foundStop = findNextStop( text, aStartIndex );
        aLines.add( text.substring( aStartIndex, foundStop ) );
        extractLineStartinAt( foundStop + 1, aLines );
        }

    private int findNextStop( final String aText, final int aStartIndex )
        {
        int length = 0;
        int lastStop = 0;
        while ( true )
            {
            final int index = aStartIndex + length;
            if ( index >= aText.length() ) return index;

            final char code = aText.charAt( index );
            if ( code == '\n' || code == '|' ) return index;
            if ( code == ' ' ) lastStop = index;

            final int width = font.substringWidth( aText, aStartIndex, length );
            if ( width >= blockWidth ) return lastStop;

            length++;
            }
        }
    }
