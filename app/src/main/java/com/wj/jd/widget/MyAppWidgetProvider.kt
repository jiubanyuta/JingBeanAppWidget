package com.wj.jd.widget

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.widget.RemoteViews
import com.wj.jd.R
import android.content.ComponentName
import com.wj.jd.widget.MyAppWidgetProvider
import android.content.Intent
import android.app.PendingIntent
import android.content.Context
import com.wj.jd.widget.UpdateDataService
import android.os.Bundle
import android.util.Log

class MyAppWidgetProvider : AppWidgetProvider() {
    private fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widges_layout)
        val name = ComponentName(context, MyAppWidgetProvider::class.java)
        remoteViews.setTextViewText(R.id.title, "updateAppWidget")
        appWidgetManager.updateAppWidget(name, remoteViews)
    }

    /*
     * 每次窗口小部件被更新都调用一次该方法
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.i("====","onUpdate")
        for (appwidgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widges_layout)
            val last_intent = Intent()
            last_intent.action = CLICK_ACTION
            val last_pendingIntent = PendingIntent.getBroadcast(context, 0, last_intent, 0)
            remoteViews.setOnClickPendingIntent(R.id.title, last_pendingIntent)
            val name = ComponentName(context, MyAppWidgetProvider::class.java)
            appWidgetManager.updateAppWidget(name, remoteViews)
        }
    }

    /*
     * 接收窗口小部件点击时发送的广播
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        //这里判断是自己的action，做自己的事情
        if (intent.action == CLICK_ACTION) {
            Log.i("====","onReceive")

        }
    }

    /*
     * 当小部件从备份恢复时调用该方法
     */
    override fun onRestored(context: Context, oldWidgetIds: IntArray, newWidgetIds: IntArray) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    /*
     * 每删除一次窗口小部件就调用一次
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    /*
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    override fun onEnabled(context: Context) {
        context.startService(Intent(context, UpdateDataService::class.java))
    }

    /*
     * 当最后一个该窗口小部件删除时调用该方法
     */
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        context.stopService(Intent(context, UpdateDataService::class.java))
    }

    /*
     * 当小部件大小改变时
     */
    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    companion object {
        const val CLICK_ACTION = "com.example.action.CLICK.JD" //自己定义的action
    }
}