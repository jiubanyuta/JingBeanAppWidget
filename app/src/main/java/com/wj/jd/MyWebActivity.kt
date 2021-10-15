package com.wj.jd

import android.content.Context
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import kotlinx.android.synthetic.main.activity_web.*

class MyWebActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        setTitle("网页浏览器")
    }

    override fun initData() {
        removeCookie(this)
        mCommonWebView.loadUrl("https://plogin.m.jd.com/login/login")
    }

    override fun setEvent() {
    }

    private fun removeCookie(context: Context) {
        try {
            CookieSyncManager.createInstance(context)
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            CookieSyncManager.getInstance().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}