package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockWidgetService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by RamY on 23/05/16.
 */
public class StockWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId:appWidgetIds)
        {

            CharSequence widgetText = context.getString(R.string.stock_widget);
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget_layout);
            views.setTextViewText(R.id.widget_heading, widgetText);
            views.setRemoteAdapter(R.id.widget_content, new Intent(context, StockWidgetService.class));

            Intent appIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

            views.setOnClickPendingIntent(R.id.widget_heading, appPendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }



}
