package com.codeimagination.properties.splash_screen;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context context;

    public IntroManager(Context context){

        this.context = context;
        preferences = context.getSharedPreferences("First", 0);
        editor = preferences.edit();

    }

    public void setFirst(boolean isFirst){

        editor.putBoolean("check", isFirst);
        editor.commit();

    }

    public boolean check(){

        return preferences.getBoolean("check", true);

    }



}
