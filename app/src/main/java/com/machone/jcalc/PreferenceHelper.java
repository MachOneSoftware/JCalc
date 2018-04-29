package com.machone.jcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.util.Log;

/**
 * Provides parameters
 */
public class PreferenceHelper {
    private static final String SHARED_PREFERENCES_FILE_NAME = "jcalc";

    //== Parameter Names ===================

    private static final String VERSION_NUMBER = "version_number";
    private static final String VERSION_CODE = "verison_code";
    private static final String THEME_ID = "theme_id";

    //== Default Values ====================

    private static final int VERSION_CODE_DEFAULT = 0;
    private static final int THEME_ID_DEFAULT = 0;

    //== Constructor and Dependencies ======
    private final SharedPreferences sharedPreferences;

    private PreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    //== Singleton =========================

    private static PreferenceHelper instance;

    public static synchronized PreferenceHelper getInstance(Context context){
        if (instance == null)
            instance = new PreferenceHelper(context);
        return instance;
    }

    //== Getters ===========================

    public int getSavedVersionCode(){
        // Remove "version_number" key in favor of using a better name.
        // "version_number" was only used with one version (8/1.2.0).
        sharedPreferences.edit().remove(VERSION_NUMBER).apply();
        return sharedPreferences.getInt(VERSION_CODE, VERSION_CODE_DEFAULT);
    }

    public Theme getTheme(){
        return Theme.getTheme(sharedPreferences.getInt(THEME_ID, THEME_ID_DEFAULT));
    }

    //== Setters ===========================

    public int saveCurrentVersionCode(Context context){
        int version = -1;
        try{
            PackageInfo p = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = p.versionCode;
        }catch (Exception ex){
            Log.w("setVersionCode", "Error retrieving version code.", ex);
        }

        sharedPreferences.edit().putInt(VERSION_CODE, version).apply();
        return version;
    }

    public void setTheme(Theme theme){
        sharedPreferences.edit().putInt(THEME_ID, theme.getId()).apply();
    }
}
