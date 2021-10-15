package com.wj.jd

import kotlinx.android.synthetic.main.activity_web.*

class MyWebActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        setTitle("网页浏览器")
    }

    override fun initData() {
        mCommonWebView.loadUrl("https://www.baidu.com")
    }

    override fun setEvent() {
    }
}