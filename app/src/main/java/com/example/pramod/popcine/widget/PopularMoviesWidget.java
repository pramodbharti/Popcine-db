package com.example.pramod.popcine.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.pramod.popcine.R;
import com.example.pramod.popcine.ui.PopcineMainActivity;
import com.example.pramod.popcine.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class PopularMoviesWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.popular_movies_widget);
        views.setTextViewText(R.id.tv_widget_popular, widgetText);

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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.popular_movies_widget);

        Intent widgetPressIntent = new Intent(context, PopcineMainActivity.class);
        PendingIntent buttonPressPendingIntent = PendingIntent.getActivity(context, 0, widgetPressIntent, 0);
        views.setOnClickPendingIntent(R.id.rl_widget_main_layout, buttonPressPendingIntent);

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_MOVIE, Context.MODE_PRIVATE);

        String movieString = sharedPref.getString(Constants.SHARED_PREF_KEY, "");

        views.setTextViewText(R.id.tv_widget_popular, movieString);

        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, PopularMoviesWidget.class), views);
    }
}

