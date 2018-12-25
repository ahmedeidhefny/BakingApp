package com.udacity.ahmed_eid.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.ahmed_eid.bakingapp.Activities.MainActivity;
import com.udacity.ahmed_eid.bakingapp.Model.Recipe;
import com.udacity.ahmed_eid.bakingapp.Model.ingredient;
import com.udacity.ahmed_eid.bakingapp.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setOnClickPendingIntent(R.id.widget_root_layout, pendingIntent);

        Widget_ManageData_Helper widgetHelper = new Widget_ManageData_Helper(context);
        Recipe recipe = widgetHelper.getWidgetData();
        ArrayList<ingredient> ingredients = recipe.getIngredients();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            stringBuilder.append("- ")
                    .append(ingredients.get(i).getIngredient())
                    .append("\n");
        }
        //Log.e("widget", stringBuilder.toString());
        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
        views.setTextViewText(R.id.widget_recipe_ingredients, stringBuilder.toString());
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

