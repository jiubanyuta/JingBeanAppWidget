package com.wj.jd.util

import android.os.Handler
import android.os.Looper
import com.wj.jd.widget.WidgetUpdateDataUtil

/**
 * author wangjing
 * Date 2021/10/18
 * Description
 */
object UpdateTask {
    var handler = Handler(Looper.getMainLooper())

    fun updateAll() {
        handler.post {
            WidgetUpdateDataUtil.updateWidget("ck")
        }
        handler.postDelayed({
            WidgetUpdateDataUtil.updateWidget("ck1")
        }, 2000)

        handler.postDelayed({
            WidgetUpdateDataUtil.updateWidget("ck2")
        }, 4000)
    }
}