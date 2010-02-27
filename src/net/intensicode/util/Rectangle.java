package net.intensicode.util;

public class Rectangle
    {
    public int x;

    public int y;

    public int width;

    public int height;


    public Rectangle()
        {
        }

    public Rectangle( int aX, int aY, int aWidth, int aHeight )
        {
        //#if DEBUG
        if ( aWidth < 0 || aHeight < 0 ) throw new IllegalArgumentException();
        //#endif
        x = aX;
        y = aY;
        width = aWidth;
        height = aHeight;
        }

    public void setTo( final Rectangle aRectangle )
        {
        x = aRectangle.x;
        y = aRectangle.y;
        width = aRectangle.width;
        height = aRectangle.height;
        }

    public void applyOutsets( final int aOutsetSizeInPixels )
        {
        x -= aOutsetSizeInPixels;
        y -= aOutsetSizeInPixels;
        width += aOutsetSizeInPixels * 2;
        height += aOutsetSizeInPixels * 2;
        }

    public final void setCenterAndSize( final Position aPosition, final Size aSize )
        {
        setCenterAndSize( aPosition, aSize.width, aSize.height );
        }

    public final void setCenterAndSize( final Position aPosition, final int aWidth, final int aHeight )
        {
        //#if DEBUG
        if ( aWidth < 0 || aHeight < 0 ) throw new IllegalArgumentException();
        //#endif
        width = aWidth;
        height = aHeight;
        x = aPosition.x - width / 2;
        y = aPosition.y - height / 2;
        }

    public final boolean contains( final Position aPosition )
        {
        if ( aPosition.x < x || aPosition.x > x + width ) return false;
        if ( aPosition.y < y || aPosition.y > y + height ) return false;
        return true;
        }

    public final boolean contains( final int aX, final int aY )
        {
        if ( aX < x || aX > x + width ) return false;
        if ( aY < y || aY > y + height ) return false;
        return true;
        }

    public final boolean intersectsWith( final Rectangle aRectangleFixed )
        {
        final int x1 = aRectangleFixed.x;
        final int x2 = aRectangleFixed.x + aRectangleFixed.width;
        if ( x > x2 || x + width < x1 ) return false;

        final int y1 = aRectangleFixed.y;
        final int y2 = aRectangleFixed.y + aRectangleFixed.height;
        if ( y > y2 || y + height < y1 ) return false;

        return true;
        }

    // From Object

    public final boolean equals( final Object aThat )
        {
        if ( !( aThat instanceof Rectangle ) ) return false;

        final Rectangle that = (Rectangle) aThat;
        return this.x == that.x && this.y == that.y && this.width == that.width && this.height == that.height;
        }

    //#if DEBUG

    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append( "x=" );
        buffer.append( x );
        buffer.append( ",y=" );
        buffer.append( y );
        buffer.append( ",width=" );
        buffer.append( width );
        buffer.append( ",height=" );
        buffer.append( height );
        return buffer.toString();
        }

    //#endif
    }
