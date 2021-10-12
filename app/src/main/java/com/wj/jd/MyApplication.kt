package com.wj.jd

import android.app.Application
import com.gyf.cactus.ext.cactus
import com.tencent.bugly.crashreport.CrashReport

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

        CrashReport.initCrashReport(this, "a8f5ee2093", Constants.isDebug)

        cactus {
            isDebug(false)
            setBackgroundMusicEnabled(true)
            addBackgroundCallback {

            }
        }
    }

}