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
        WidgetUpdateDataUtil.updateWidget("ck")
        WidgetUpdateDataUtil.updateWidget("ck1")
        WidgetUpdateDataUtil.updateWidget("ck2")
    }
}