package net.intensicode.hiscore;

import java.util.Calendar;
import java.util.TimeZone;

public final class GammetaDate
    {
    public static String now()
        {
        return from( Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) ) );
        }

    public static String from( final Calendar aCalendar )
        {
        final StringBuffer date = new StringBuffer();

        final int dow = aCalendar.get( Calendar.DAY_OF_WEEK );
        if ( dow == Calendar.MONDAY ) date.append( "Mon" );
        if ( dow == Calendar.TUESDAY ) date.append( "Tue" );
        if ( dow == Calendar.WEDNESDAY ) date.append( "Wed" );
        if ( dow == Calendar.THURSDAY ) date.append( "Thu" );
        if ( dow == Calendar.FRIDAY ) date.append( "Fri" );
        if ( dow == Calendar.SATURDAY ) date.append( "Sat" );
        if ( dow == Calendar.SUNDAY ) date.append( "Sun" );
        date.append( ", " );
        if ( aCalendar.get( Calendar.DAY_OF_MONTH ) < 10 ) date.append( '0' );
        date.append( aCalendar.get( Calendar.DAY_OF_MONTH ) );
        date.append( " " );

        final int month = aCalendar.get( Calendar.MONTH );
        if ( month == Calendar.JANUARY ) date.append( "Jan" );
        if ( month == Calendar.FEBRUARY ) date.append( "Feb" );
        if ( month == Calendar.MARCH ) date.append( "Mar" );
        if ( month == Calendar.APRIL ) date.append( "Apr" );
        if ( month == Calendar.MAY ) date.append( "May" );
        if ( month == Calendar.JUNE ) date.append( "Jun" );
        if ( month == Calendar.JULY ) date.append( "Jul" );
        if ( month == Calendar.AUGUST ) date.append( "Aug" );
        if ( month == Calendar.SEPTEMBER ) date.append( "Sep" );
        if ( month == Calendar.OCTOBER ) date.append( "Oct" );
        if ( month == Calendar.NOVEMBER ) date.append( "Nov" );
        if ( month == Calendar.DECEMBER ) date.append( "Dec" );
        date.append( " " );
        date.append( aCalendar.get( Calendar.YEAR ) );
        date.append( " " );
        if ( aCalendar.get( Calendar.HOUR_OF_DAY ) < 10 ) date.append( '0' );
        date.append( aCalendar.get( Calendar.HOUR_OF_DAY ) );
        date.append( ":" );
        if ( aCalendar.get( Calendar.MINUTE ) < 10 ) date.append( '0' );
        date.append( aCalendar.get( Calendar.MINUTE ) );
        date.append( ":" );
        if ( aCalendar.get( Calendar.SECOND ) < 10 ) date.append( '0' );
        date.append( aCalendar.get( Calendar.SECOND ) );
        date.append( " GMT" );

        return date.toString();
        }
    }
