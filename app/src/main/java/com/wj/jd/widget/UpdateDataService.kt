package com.wj.jd.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class UpdateDataService : Service() {
    var timer: Timer? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (timer == null) {
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
//                    updata();
                }
            }, 0, (30 * 60 * 1000).toLong())
        } else {
//            updata();
        }
        return super.onStartCommand(intent, flags, startId)
    }
}