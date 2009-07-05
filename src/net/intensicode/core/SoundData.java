/************************************************************************/
/* {{PROJECT_NAME}}             {{COMPANY}}             {{DATE_CREATE}} */
/************************************************************************/

package net.intensicode.core;

/**
 * TODO: Describe this!
 */
final class SoundData
    {
    public byte[] bytes;

    public String mimeType;



    public SoundData( final byte[] aData, final String aMimeType )
        {
        bytes = aData;
        mimeType = aMimeType;
        }
    }
