package com.wj.jd.util

import android.os.Handler
import android.os.Looper
import com.wj.jd.widget.WidgetUpdateDataUtil1

/**
 * author wangjing
 * Date 2021/10/18
 * Description
 */
object UpdateTask {
    var handler = Handler(Looper.getMainLooper())

    fun updateAll() {
        handler.post {
            WidgetUpdateDataUtil1.updateWidget("ck")

        }

        handler.postDelayed({
            WidgetUpdateDataUtil1.updateWidget("ck1")
        }, 1500)


        handler.postDelayed({
            WidgetUpdateDataUtil1.updateWidget("ck2")
        }, 3000)
    }
}