package net.intensicode;

public final class ReleaseProperties
    {
    //#if "${version}"
    //# public static final String VERSION = "${version}";
    //#else

    public static final String VERSION = "1.1.1";

    //#endif

    //#if "${date}"
    //# public static final String DATE = "${date}";
    //#else

    public static final String DATE = "2008-02-07 20:56:10";

    //#endif

    public static final String SOUND_FOLDER = "sound";

    //#if "${target.sound_suffix}"
    //# public static final String SOUND_FORMAT_SUFFIX = "${target.sound_suffix}";
    //#else

    public static final String SOUND_FORMAT_SUFFIX = ".wav";

    //#endif

    //#if "${target.sound_type}"
    //# public static final String SOUND_FORMAT_MIME_TYPE = "${target.sound_type}";
    //#else

    public static final String SOUND_FORMAT_MIME_TYPE = "audio/x-wav";

    //#endif

    public static final String MUSIC_FOLDER = "music";

    //#if "${target.music_suffix}"
    //# public static final String MUSIC_FORMAT_SUFFIX = "${target.music_suffix}";
    //#else

    public static final String MUSIC_FORMAT_SUFFIX = ".mid";

    //#endif

    //#if "${target.music_type}"
    //# public static final String MUSIC_FORMAT_MIME_TYPE = "${target.music_type}";
    //#else

    public static final String MUSIC_FORMAT_MIME_TYPE = "audio/midi";

    //#endif

    //#if ${target.leftsoft}
    //# public static final int UNUSED_KEYCODES_LEFTSOFT = ${target.leftsoft};
    //#else

    public static final int UNUSED_KEYCODES_LEFTSOFT = -6;

    //#endif

    //#if ${target.rightsoft}
    //# public static final int UNUSED_KEYCODES_RIGHTSOFT = ${target.rightsoft};
    //#else

    public static final int UNUSED_KEYCODES_RIGHTSOFT = -7;

    //#endif

    //#if ${target.pausekey}
    //# public static final int KEYCODES_PAUSEKEY = ${target.pausekey};
    //#else

    public static final int KEYCODES_PAUSEKEY = 80;

    //#endif

    //#if "${target.hiscore_id}"
    //# public static final String HISCORE_ID = "${target.hiscore_id}";
    //#else

    public static final String HISCORE_ID = "[Dev:Dev:Dev]";

    //#endif
    }
