//#condition FEEDBACK

package net.intensicode.configuration;

import net.intensicode.*;
import net.intensicode.core.GameSystem;

public final class SendFeedback implements ConfigurableActionValue
    {
    public String optionalMessageAddon;


    public SendFeedback( final GameSystem aGameSystem )
        {
        myGameSystem = aGameSystem;
        }

    public final String getTitle()
        {
        return "Send feedback";
        }

    public final String getInfoText()
        {
        return "Send feedback or bug report via EMail.\n\n" +
               "This will open your EMail application. " +
               "From there you can send bug reports or feedback about this game. " +
               "Thanks for contributing!";
        }

    public final void trigger()
        {
        final EmailData data = new EmailData();
        myGameSystem.context.fillEmailData( data );

        if ( data.to == null ) data.to = "intensigame@intensicode.net";
        if ( data.subject == null ) data.subject = "IntensiGame Feedback";

        final StringBuffer buffer = new StringBuffer();

        buffer.append( "Add your comments here or after the configuration data block following below:" );
        buffer.append( NEWLINE );

        buffer.append( NEWLINE );
        buffer.append( NEWLINE );
        buffer.append( NEWLINE );

        buffer.append( "-------------------------------" );
        buffer.append( NEWLINE );

        buffer.append( "RELEASE" );
        buffer.append( " version " + ReleaseProperties.VERSION );
        buffer.append( " build " + ReleaseProperties.BUILD );
        buffer.append( " date " + ReleaseProperties.DATE );
        buffer.append( NEWLINE );

        buffer.append( "SCREEN" );
        buffer.append( " size " + myGameSystem.screen.width() );
        buffer.append( "x" + myGameSystem.screen.height() );
        buffer.append( " target  " + myGameSystem.screen.getTargetWidth() );
        buffer.append( "x" + myGameSystem.screen.getTargetHeight() );
        buffer.append( " native " + myGameSystem.screen.getNativeWidth() );
        buffer.append( "x" + myGameSystem.screen.getNativeHeight() );
        buffer.append( NEWLINE );

        buffer.append( "FORMATS " );
        buffer.append( ReleaseProperties.SOUND_FORMAT_SUFFIX );
        buffer.append( ' ' );
        buffer.append( ReleaseProperties.SOUND_FORMAT_MIME_TYPE );
        buffer.append( ' ' );
        buffer.append( ReleaseProperties.MUSIC_FORMAT_SUFFIX );
        buffer.append( ' ' );
        buffer.append( ReleaseProperties.MUSIC_FORMAT_MIME_TYPE );
        buffer.append( NEWLINE );

        buffer.append( "HISCORE ID " );
        buffer.append( ReleaseProperties.HISCORE_ID );
        buffer.append( NEWLINE );

        buffer.append( "PLATFORM " );
        buffer.append( myGameSystem.platform.getPlatformSpecString() );
        buffer.append( NEWLINE );

        buffer.append( "GRAPHICS " );
        buffer.append( myGameSystem.platform.getGraphicsSpecString() );
        buffer.append( NEWLINE );

        if ( optionalMessageAddon != null )
            {
            buffer.append( "MESSAGE " );
            buffer.append( optionalMessageAddon );
            buffer.append( NEWLINE );
            }

        buffer.append( "-------------------------------" );
        buffer.append( NEWLINE );

        buffer.append( NEWLINE );
        buffer.append( NEWLINE );
        buffer.append( NEWLINE );
        buffer.append( "Add your comments here or above the configuration data block:" );
        buffer.append( NEWLINE );

        buffer.append( NEWLINE );
        buffer.append( NEWLINE );
        buffer.append( NEWLINE );

        data.text = buffer.toString();

        myGameSystem.platform.sendEmail( data );
        }

    private final GameSystem myGameSystem;

    private static final char NEWLINE = '\n';
    }
