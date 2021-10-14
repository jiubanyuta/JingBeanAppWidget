package com.wj.jd.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

class UpdateDataService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("====","onStartCommand")
        WidgetUpdateDataUtil.updateWidget("ck")
        return super.onStartCommand(intent, flags, startId)
    }
}