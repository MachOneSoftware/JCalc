package com.machone.jcalc.helper;

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
    private static final String CUSTOM_TIP_SHOWN = "custom_tip_tooltip_shown";
    private static final String TAP_TO_RESET_SHOWN = "tipcalc_tap_to_reset_tooltip_shown";
    private static final String BACK_TO_MAIN_SHOWN = "tipcalc_tap_to_reset_tooltip_shown";

    //== Default Values ====================

    private static final int VERSION_CODE_DEFAULT = 0;
    private static final int THEME_ID_DEFAULT = 0;
    private static final boolean CUSTOM_TIP_SHOWN_DEFAULT = false;
    private static final boolean TAP_TO_RESET_SHOWN_DEFAULT = false;
    private static final boolean BACK_TO_MAIN_SHOWN_DEFAULT = false;

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
        int number = sharedPreferences.getInt(VERSION_NUMBER, VERSION_CODE_DEFAULT);
        if (number != 0){
            // Remove "version_number" key in favor of using a better name.
            // "version_number" was only used with one version (8/1.2.0).
            sharedPreferences.edit().remove(VERSION_NUMBER).apply();
            return number;
        }
        return sharedPreferences.getInt(VERSION_CODE, VERSION_CODE_DEFAULT);
    }

    public Theme getTheme(){
        return Theme.getTheme(sharedPreferences.getInt(THEME_ID, THEME_ID_DEFAULT));
    }

    public boolean getCustomTipTooltipShown(){
        return sharedPreferences.getBoolean(CUSTOM_TIP_SHOWN, CUSTOM_TIP_SHOWN_DEFAULT);
    }

    public boolean getTapToResetTooltipShown(){
        return sharedPreferences.getBoolean(TAP_TO_RESET_SHOWN, TAP_TO_RESET_SHOWN_DEFAULT);
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

    public void setCustomTipTooltipShown(){
        sharedPreferences.edit().putBoolean(CUSTOM_TIP_SHOWN, true).apply();
    }

    public void setTapToResetTooltipShown(){
        sharedPreferences.edit().putBoolean(TAP_TO_RESET_SHOWN, true).apply();
    }
}
