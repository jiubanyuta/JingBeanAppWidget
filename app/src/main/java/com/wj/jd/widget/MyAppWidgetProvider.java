package com.wj.jd.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.wj.jd.R;

public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "com.example.action.CLICK.JD";//自己定义的action

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {
        Log.i("====","updateAppWidget");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widges_layout);
        ComponentName name = new ComponentName(context, MyAppWidgetProvider.class);
        remoteViews.setTextViewText(R.id.title, "updateAppWidget");
        appWidgetManager.updateAppWidget(name, remoteViews);
    }

    /*
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("====","onUpdate");
        for (int appwidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widges_layout);

            Intent last_intent = new Intent();
            last_intent.setAction(CLICK_ACTION);
            PendingIntent last_pendingIntent = PendingIntent.getBroadcast(context, 0, last_intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.title, last_pendingIntent);

            ComponentName name = new ComponentName(context, MyAppWidgetProvider.class);
            appWidgetManager.updateAppWidget(name, remoteViews);
            Log.i("====","onUpdate");
        }
    }

    /*
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //这里判断是自己的action，做自己的事情
        Log.i("====","onReceive");
        if (intent.getAction().equals(CLICK_ACTION)) {
            Log.i("====","onReceive");
        }
    }

    /*
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Log.i("====","onRestored");
    }

    /*
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i("====","onDeleted");
    }

    /*
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        context.startService(new Intent(context, UpdateDataService.class));
        Log.i("====","onEnabled");
    }

    /*
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        context.stopService(new Intent(context, UpdateDataService.class));
        Log.i("====","onDisabled");
    }

    /*
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.i("====","onAppWidgetOptionsChanged");
    }
}
