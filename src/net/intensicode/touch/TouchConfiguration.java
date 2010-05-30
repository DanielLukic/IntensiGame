//#condition TOUCH

package net.intensicode.touch;

import net.intensicode.util.Assert;

public abstract class TouchConfiguration
    {
    public static final String[] NO_LABELS = new String[0];

    public TouchConfiguration[] presets;

    public int defaultPresetIndex;

    public String label;


    public final String[] getLabels()
        {
        Assert.notNull( "presets initialized", presets );
        if ( presets == null ) return NO_LABELS;

        final String[] labels = new String[presets.length];
        for ( int idx = 0; idx < presets.length; idx++ )
            {
            labels[ idx ] = presets[ idx ].label;
            }
        return labels;
        }

    public final void setDefaultPreset()
        {
        setPresetByIndex( defaultPresetIndex );
        }

    public final void setPresetByIndex( final int aPresetIndex )
        {
        Assert.notNull( "presets initialized", presets );
        if ( presets == null || presets.length == 0 ) return;

        final int maximumIndex = presets.length - 1;
        final int index = Math.max( 0, Math.min( maximumIndex, aPresetIndex ) );
        setTo( presets[ index ] );
        }

    public abstract void setTo( final TouchConfiguration aNewConfiguration );
    }
