package net.intensicode.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;



/**
 * TODO: Describe this!
 */
public final class ImageUtils
    {
    public static final Image[] splitVertically( final Image aImage, final int aNumberOfStripes )
        {
        final int sourceWidth = aImage.getWidth();
        final int sourceHeight = aImage.getHeight();
        final Image[] images = new Image[aNumberOfStripes];
        for ( int idx = 0; idx < aNumberOfStripes; idx++ )
            {
            final int yStart = idx * sourceHeight / aNumberOfStripes;
            final int yEnd = (idx + 1) * sourceHeight / aNumberOfStripes;
            final int height = yEnd - yStart;
            //#if NOKIA
            //# final Image stripe = images[ idx ] = Image.createImage( sourceWidth, height );
            //# final Graphics graphics = stripe.getGraphics();
            //# graphics.setColor( 0 );
            //# graphics.fillRect( 0, 0, sourceWidth, height );
            //# graphics.drawImage( aImage, 0, -yStart, Graphics.TOP | Graphics.LEFT );
            //#else
            final int[] rgb = new int[sourceWidth * height];
            aImage.getRGB( rgb, 0, sourceWidth, 0, yStart, sourceWidth, height );
            images[ idx ] = Image.createRGBImage( rgb, sourceWidth, height, true );
            //#endif
            }
        return images;
        }

    public static final Image resizeIfNecessary( final Image aImage, final Size aSize )
        {
        if ( aImage.getWidth() == aSize.width && aImage.getHeight() == aSize.height ) return aImage;
        return ImageUtils.scale( aImage, aSize.width, aSize.height, false );
        }

    public static final Image resizeIfNecessary( final Image aImage, final int aWidth, final int aHeight )
        {
        if ( aImage.getWidth() == aWidth && aImage.getHeight() == aHeight ) return aImage;
        return ImageUtils.scale( aImage, aWidth, aHeight, false );
        }

    public static final Image scale( final Image aImage, final int aWidth, final int aHeight, final boolean aKeepAspect )
        {
        final int targetWidth;
        final int targetHeight;

        if ( aKeepAspect == false )
            {
            targetWidth = aWidth;
            targetHeight = aHeight;
            }
        else
            {
            final int imageWidth = aImage.getWidth();
            final int imageHeight = aImage.getHeight();

            int xFactor = FixedMath.toFixed( aWidth ) / imageWidth;
            int yFactor = FixedMath.toFixed( aHeight ) / imageHeight;
            xFactor = yFactor = Math.min( xFactor, yFactor );

            targetWidth = FixedMath.toInt( xFactor * imageWidth );
            targetHeight = FixedMath.toInt( yFactor * imageHeight );
            }

        //#if DEBUG
        if ( targetWidth > aWidth || targetHeight > aHeight ) throw new IllegalStateException();
        //#endif

        return scaleMIDP2( aImage, Math.min( aWidth, targetWidth ), Math.min( aHeight, targetHeight ) );
        }

    private static final Image scaleMIDP2( final Image aSource, final int aTargetWidth, final int aTargetHeight )
        {
        final int sourceWidth = aSource.getWidth();
        final int sourceHeight = aSource.getHeight();

        final int[] source = getSourceBuffer( sourceWidth, sourceHeight );
        aSource.getRGB( source, 0, sourceWidth, 0, 0, sourceWidth, sourceHeight );

        final int[] target = getTargetBuffer( aTargetWidth, aTargetHeight );
        for ( int y = 0; y < aTargetHeight; y++ )
            {
            final int ySource = y * sourceHeight / aTargetHeight;
            for ( int x = 0; x < aTargetWidth; x++ )
                {
                final int xSource = x * sourceWidth / aTargetWidth;
                target[ x + y * aTargetWidth ] = source[ xSource + ySource * sourceWidth ];
                }
            }

        return Image.createRGBImage( target, aTargetWidth, aTargetHeight, true );
        }

    public static final void scaleInto( final Graphics aGC, final int aWidth, final int aHeight, final Image aSource )
        {
        // This works online for scansize == aWidth..

        final int sourceWidth = aSource.getWidth();
        final int sourceHeight = aSource.getHeight();

        final int[] source = getSourceBuffer( sourceWidth, sourceHeight );
        aSource.getRGB( source, 0, sourceWidth, 0, 0, sourceWidth, sourceHeight );

        final int[] target = getTargetBuffer( aWidth, aHeight );
        for ( int y = 0; y < aHeight; y++ )
            {
            final int ySource = y * sourceHeight / aHeight;
            for ( int x = 0; x < aWidth; x++ )
                {
                final int xSource = x * sourceWidth / aWidth;
                target[ x + y * aWidth ] = source[ xSource + ySource * sourceWidth ];
                }
            }

        aGC.drawRGB( target, 0, aWidth, 0, 0, aWidth, aHeight, true );
        }

    private static final int[] getSourceBuffer( final int aWidth, final int aHeight )
        {
        theSourceBuffer = updateBuffer( theSourceBuffer, aWidth, aHeight );
        return theSourceBuffer;
        }

    private static final int[] getTargetBuffer( final int aWidth, final int aHeight )
        {
        theTargetBuffer = updateBuffer( theTargetBuffer, aWidth, aHeight );
        return theTargetBuffer;
        }

    private static final int[] updateBuffer( final int[] aBuffer, final int aWidth, final int aHeight )
        {
        if ( aBuffer == null || aBuffer.length < aWidth * aHeight )
            {
            return new int[aWidth * aHeight];
            }
        return aBuffer;
        }



    private static int[] theSourceBuffer;

    private static int[] theTargetBuffer;
    }
