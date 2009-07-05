/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.screens;

import net.intensicode.core.AbstractScreen;
import net.intensicode.core.DirectScreen;
import net.intensicode.core.Engine;
import net.intensicode.util.FontGen;
import net.intensicode.util.Position;

import javax.microedition.lcdui.Graphics;



public final class MenuEntry extends AbstractScreen
    {
    public static int selectorColor = 0xFF7F0000;

    public final Position position;

    public final FontGen fontGen;

    public final String text;

    public boolean selectedState;

    public int id;



    MenuEntry( final FontGen aCharGen, final String aText, final Position aPosition )
        {
        fontGen = aCharGen;
        text = aText;
        position = aPosition;
        }

    final void setSelected( boolean aSelectedFlag )
        {
        selectedState = aSelectedFlag;
        }

    // From AbstractScreen

    public final void onControlTick( final Engine aEngine ) throws Exception
        {
        }

    public final void onDrawFrame( final DirectScreen aScreen )
        {
        final Graphics gc = aScreen.graphics();

        if ( selectedState )
            {
            final int x = 0;
            final int y = position.y - fontGen.charHeight() / 2;
            final int width = aScreen.width();
            final int height = fontGen.charHeight();
            gc.setColor( selectorColor );
            gc.fillRect( x, y, width, height );
            }

        fontGen.blitString( gc, text, position, FontGen.CENTER );
        }
    }
