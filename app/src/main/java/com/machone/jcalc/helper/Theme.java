package com.machone.jcalc.helper;

import android.util.Log;

public enum Theme {
    Classic;

    public int getId() {
        switch (this) {
            case Classic:
                return 0;
            default:
                Log.w("Theme.getId", "Theme has no associated ID: " + this.name());
                return 0;
        }
    }

    public static Theme getTheme(int id) {
        switch (id) {
            case 0:
                return Classic;
            default:
                Log.w("Theme.getTheme", "ID has no associated Theme: " + id);
                return Classic;
        }
    }
}
