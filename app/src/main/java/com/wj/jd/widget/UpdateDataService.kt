package com.wj.jd.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class UpdateDataService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        WidgetUpdateDataUtil.updateWidget()
        return super.onStartCommand(intent, flags, startId)
    }
}