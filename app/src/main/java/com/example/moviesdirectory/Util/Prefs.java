package com.example.moviesdirectory.Util;

import android.app.Activity;
import android.content.SharedPreferences;

//The idea is to store the searches in Shared Preference, so that, it can be shown when the app opens for the first time
public class Prefs {

    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = sharedPreferences;
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setSearch(String search) {
        sharedPreferences.edit().putString("search", search).apply();
    }

    public String getSearch() {
        return sharedPreferences.getString("search", "Batman");
        /**for the 1st time getSearch() will be called, when there will be no data stored, the default value "Batman" will be
         * returned*/
    }
}

