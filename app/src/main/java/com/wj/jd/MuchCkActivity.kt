package com.wj.jd

import android.widget.Toast
import com.wj.jd.util.CacheUtil
import com.wj.jd.widget.WidgetUpdateDataUtil
import kotlinx.android.synthetic.main.activity_setting.*

class MuchCkActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_much
    }

    override fun initView() {
        setTitle("多账号设置")
    }

    override fun initData() {
    }

    override fun setEvent() {
    }
}