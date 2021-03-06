package net.intensicode.util;

public class Rectangle
    {
    public static final Rectangle NULL = new Rectangle();

    public int x;

    public int y;

    public int width;

    public int height;


    public Rectangle()
        {
        }

    public Rectangle( final int aX, final int aY, final int aWidth, final int aHeight )
        {
        //#if DEBUG
        if ( aWidth < 0 || aHeight < 0 ) throw new IllegalArgumentException();
        //#endif
        x = aX;
        y = aY;
        width = aWidth;
        height = aHeight;
        }

    public void set( final Position aPosition, final Size aSize )
        {
        x = aPosition.x;
        y = aPosition.y;
        width = aSize.width;
        height = aSize.height;
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

    public final boolean isAdjacent( final Rectangle that )
        {
        if ( this.x == that.x && this.width == that.width )
            {
            if ( this.y == that.y + that.height ) return true;
            if ( this.y + this.height == that.y ) return true;
            }
        if ( this.y == that.y && this.height == that.height )
            {
            if ( this.x == that.x + that.width ) return true;
            if ( this.x + this.width == that.x ) return true;
            }
        return false;
        }

    public final void uniteWith( final Rectangle that )
        {
        final int left = Math.min( this.x, that.x );
        final int top = Math.min( this.y, that.y );
        final int right = Math.max( this.x + this.width, that.x + that.width );
        final int bottom = Math.max( this.y + this.height, that.y + that.height );
        x = left;
        y = top;
        width = right - left;
        height = bottom - top;
        }

    // From Object

    public final boolean equals( final Object aThat )
        {
        if ( this == aThat ) return true;
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
