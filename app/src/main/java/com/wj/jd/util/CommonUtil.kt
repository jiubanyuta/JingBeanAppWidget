package com.wj.jd.util

import com.wj.jd.MyApplication

/**
 * author wangjing
 * Date 2021/4/19
 * Description 顶层方法
 */


fun Int.dmToPx(): Int {
    return MyApplication.mInstance.resources.getDimensionPixelSize(this)
}

fun Int.dmToSp(): Int {
    return MyApplication.mInstance.resources.getDimensionPixelSize(this)
}
