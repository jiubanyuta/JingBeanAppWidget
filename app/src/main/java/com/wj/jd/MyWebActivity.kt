package com.wj.jd

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.widget.Toast
import com.wj.jd.dialog.NewStyleDialog
import com.wj.jd.webView.CommonWebView
import kotlinx.android.synthetic.main.activity_web.*

class MyWebActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        setTitle("网页浏览器")
    }

    override fun initData() {
//        removeCookie(this)
        mCommonWebView.loadUrl("https://plogin.m.jd.com/login/login")
    }

    override fun setEvent() {
        mCommonWebView.setOnGetCookieListener(object : CommonWebView.OnGetCookie {
            override fun get(ck: String) {
                dealCk(ck)
            }
        })
    }

    private fun dealCk(ck: String) {
        createDialog("已获取到CK", ck, "取消", "复制", object : NewStyleDialog.OnLeftClickListener {
            override fun leftClick() {
                disMissDialog()
            }
        }, object : NewStyleDialog.OnRightClickListener {
            override fun rightClick() {
                copyClipboard(ck)
                disMissDialog()
                finish()
            }
        })
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

    fun copyClipboard(content: String?) {
        try {
            val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val primaryClip = ClipData.newPlainText("text", content)
            myClipboard.setPrimaryClip(primaryClip)
            Toast.makeText(MyApplication.mInstance, "已复制CK到粘贴板", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}