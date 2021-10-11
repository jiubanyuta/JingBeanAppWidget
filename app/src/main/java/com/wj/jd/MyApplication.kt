package com.wj.jd

import android.app.Application

/**
 * author wangjing
 * Date 2021/6/25
 * Description
 */
class MyApplication : Application() {
    companion object {
        @JvmStatic
        lateinit var mInstance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}