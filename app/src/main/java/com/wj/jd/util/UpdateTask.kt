package com.wj.jd.util

import android.os.Handler
import android.os.Looper

/**
 * author wangjing
 * Date 2021/10/18
 * Description
 */
object UpdateTask {
    var handler = Handler(Looper.getMainLooper())

    fun updateAll() {
        WidgetUpdateDataUtil1.updateWidget("ck")
        WidgetUpdateDataUtil1.updateWidget("ck1")
        WidgetUpdateDataUtil1.updateWidget("ck2")
    }
}