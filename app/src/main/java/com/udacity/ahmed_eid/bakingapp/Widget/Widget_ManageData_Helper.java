package com.udacity.ahmed_eid.bakingapp.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.ahmed_eid.bakingapp.Model.Recipe;

public class Widget_ManageData_Helper {

    public static final String SHARED_PREFERENCES_NAME = "widget_saveRecipeObject";
    public static final String SHARED_PREFERENCES_Object_KEY = "recipeObjectGson";
    private static final String TAG = "Widget_ManageData_Helper";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public Widget_ManageData_Helper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        editor = sharedPreferences.edit();
        gson = new GsonBuilder().setLenient().create();
    }

    @SuppressLint("LongLogTag")
    public void setWidgetData(Recipe recipeObject) {
        String recipeObjectGson = gson.toJson(recipeObject);
        editor.putString(SHARED_PREFERENCES_Object_KEY, recipeObjectGson);
        editor.commit();
        Log.i(TAG, "Added Recipe Object In SharedPref Successfully..!");
    }

    public Recipe getWidgetData() {
        String recipeObjectGson = sharedPreferences.getString(SHARED_PREFERENCES_Object_KEY, "NO Data In SharedPer");
        Recipe recipeObject = gson.fromJson(recipeObjectGson, Recipe.class);
        return recipeObject;
    }

}




