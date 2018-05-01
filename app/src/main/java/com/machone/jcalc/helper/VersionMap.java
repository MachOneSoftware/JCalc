package com.machone.jcalc.helper;

/**
 * Maps a versionCode to a versionName.
 */
public class VersionMap {
    public static final String verCode_6 = "1.0.0";
    public static final String verCode_8 = "1.2.0";
    public static final String verCode_9 = "1.2.1";
    public static final String verCode_10 = "1.2.2";

    public static String getVersionName(int versionCode) {
        switch (versionCode) {
            case 6:
                return verCode_6;
            case 8:
                return verCode_8;
            case 9:
                return verCode_9;
            case 10:
                return verCode_10;
            default:
                throw new IllegalArgumentException("Unexpected version code: " + versionCode);
        }
    }
}
