package com.example.beatrice.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.beatrice.bakingapp.R;
import com.example.beatrice.bakingapp.RecipeDetailsActivity;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.beatrice.bakingapp.widget.UpdateBakingService.INGREDIENTS_LIST;


/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    static ArrayList<String> ingredientsList = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //  views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.new_app_widget, appPendingIntent);

        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.new_app_widget, intent);

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

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));

        final String a = intent.getAction();

        if (a != null && a.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            ingredientsList = Objects.requireNonNull(intent.getExtras()).getStringArrayList(INGREDIENTS_LIST);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsIds, R.id.new_app_widget);
            BakingAppWidget.updateWidgets(context, appWidgetManager, appWidgetsIds);
            super.onReceive(context, intent);
        }

    }
}

