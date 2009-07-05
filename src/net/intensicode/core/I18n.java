package net.intensicode.core;

import net.intensicode.util.Log;

public final class I18n
    {
    public static final String _( final String aString )
        {
        final I18n strings = getInstance();
        return strings.lookup( aString );
        }

    public static final void setTo( final Configuration aLanguage )
        {
        //#if DEBUG
        Log.debug( "I18n configuration setTo {}", aLanguage );
        //#endif
        theInstance = new I18n( aLanguage );
        }

    public static final I18n getInstance()
        {
        if ( theInstance == null ) theInstance = new I18n();
        return theInstance;
        }

    public final String lookup( final String aString )
        {
        final String result = myConfiguration.readString( aString, null );
        if ( result != null ) return result;

        //#if DEBUG
        if ( myConfiguration.isEmpty() == false )
            {
            Engine.showException( new RuntimeException( "Missing translation for " + aString ) );
            Log.debug( "Missing translation for {}", aString );
            myConfiguration.store( aString, aString );
            }
        //#endif
        return aString;
        }

    public final boolean isEmptyConfiguration()
        {
        return myConfiguration == EMPTY_CONFIGURATION;
        }

    public I18n()
        {
        this( EMPTY_CONFIGURATION );
        }

    public I18n( final Configuration aConfiguration )
        {
        myConfiguration = aConfiguration;
        }



    private final Configuration myConfiguration;

    private static I18n theInstance;

    private static final Configuration EMPTY_CONFIGURATION = new Configuration();
    }
