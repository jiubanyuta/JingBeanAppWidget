package com.wj.jd

class MyWebActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        setTitle("网页浏览器")
    }

    override fun initData() {
    }

    override fun setEvent() {
    }
}