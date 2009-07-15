package net.intensicode.util;

import javax.microedition.io.Connector;
import java.io.IOException;

public class Messaging
    {
    public static void sendSmsByUrl( final String aRequestUrl ) throws IOException
        {
        final int protocolDelimiter = aRequestUrl.indexOf( "://" );
        final int contentDelimiter = aRequestUrl.indexOf( "/", protocolDelimiter + 3 );
        final String number = aRequestUrl.substring( protocolDelimiter + 3, contentDelimiter );
        final String content = aRequestUrl.substring( contentDelimiter + 1 );
        sendSms( number, content );
        }

    public static void sendSms( final String aNumber, final String aContent ) throws IOException
        {
        Log.debug( "sending SMS to {}: {}", aNumber, aContent );
        final String request = "sms://" + aNumber;
        final javax.wireless.messaging.MessageConnection connection = (javax.wireless.messaging.MessageConnection) Connector.open( request );
        final javax.wireless.messaging.TextMessage message = (javax.wireless.messaging.TextMessage) connection.newMessage( javax.wireless.messaging.MessageConnection.TEXT_MESSAGE );
        message.setPayloadText( aContent );
        connection.send( message );
        }
    }
