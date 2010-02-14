package net.intensicode.util;

import javax.microedition.lcdui.Image;

public final class ImageBlender
    {
    public int width;

    public int height;

    public int[] buffer;


    public final void blend(final Image aSourceImage, final int aAlpha8)
        {
        width = aSourceImage.getWidth();
        height = aSourceImage.getHeight();
        updateBufferIfNecessary();
        aSourceImage.getRGB(buffer, 0, width, 0, 0, width, height);
        applyAlpha(aAlpha8);
        }

    public final void blend(final Image aSourceImage, final Rectangle aSourceRect, final int aAlpha8)
        {
        width = aSourceRect.width;
        height = aSourceRect.height;
        updateBufferIfNecessary();
        aSourceImage.getRGB(buffer, 0, width, aSourceRect.x, aSourceRect.y, width, height);
        applyAlpha(aAlpha8);
        }

    // Implementation

    private void updateBufferIfNecessary()
        {
        if (buffer != null && buffer.length < width * height) buffer = null;
        if (buffer == null) buffer = new int[width * height];
        }

    private void applyAlpha(final int aAlpha8)
        {
        final int numberOfPixels = width * height;
        for (int idx = 0; idx < numberOfPixels; idx++)
            {
            final int argb = buffer[idx];
            final int alpha = (argb >> 24) & 0xFF;
            final int blendedAlpha = alpha * aAlpha8 / 255;
            buffer[idx] = blendedAlpha << 24 | (argb & 0x00FFFFFF);
            }
        }
    }
