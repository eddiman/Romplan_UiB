package com.pensive.android.romplanuib.io.util;

/**
 * Class for encoding url's.
 *
 * @author Fredrik Heimseter
 * @version 1.0
 */

public class URLEncoding {

    /**
     * Encodes urls so that they ca be used with UiB's services.
     * @param url The url to be encoded.
     * @return the encoded url
     */
    public static String encode(String url){
        url = url.replace("Æ","%C6");
        url = url.replace("Ø","%D8");
        url = url.replace("Å","%C5");
        return url;
    }

}
