package com.wj.jd.notification

import android.text.TextUtils
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

        }
    }
}