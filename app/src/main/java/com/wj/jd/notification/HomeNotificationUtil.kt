package com.wj.jd.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.wj.jd.MyApplication
import com.wj.jd.R
import com.wj.jd.util.CacheUtil
import com.wj.jd.util.HttpUtil
import com.wj.jd.widget.WidgetUpdateDataUtil

/**
 * author wangjing
 * Date 2021/10/19
 * Description 固定通知栏显示
 */
object HomeNotificationUtil {
    fun notification() {
        var notifyCationShow = CacheUtil.getString("CacheUtil")
        val ck = HttpUtil.getCK("ck")
        if ("1" == notifyCationShow && !TextUtils.isEmpty(ck)) {
            sendNotification()
        }
    }

    private fun sendNotification() {
        val manager = MyApplication.mInstance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "常驻信息"
            val channel = NotificationChannel(channelId, "常驻信息", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(false)
            channel.vibrationPattern = longArrayOf(0)
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.setSound(null, null)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(MyApplication.mInstance, channelId)
            builder.setDefaults(NotificationCompat.FLAG_NO_CLEAR)
        } else {
            builder = NotificationCompat.Builder(MyApplication.mInstance)
            builder.setDefaults(NotificationCompat.FLAG_NO_CLEAR)
        }

        var remoteViews = RemoteViews(MyApplication.mInstance.packageName, R.layout.widges_layout)

        builder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomBigContentView(remoteViews)
            .setCustomContentView(remoteViews)
            .setContent(remoteViews)
            .setAutoCancel(false) //设置通知被点击一次是否自动取消
            .setOnlyAlertOnce(true)
        val notification = builder.build()
        notification.flags = Notification.FLAG_NO_CLEAR
        manager.notify(10086, notification)
    }
}