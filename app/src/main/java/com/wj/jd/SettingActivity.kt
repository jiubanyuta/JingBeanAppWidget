package com.wj.jd

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.CompoundButton
import android.widget.Toast
import com.wj.jd.util.CacheUtil
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        setTitle("小组件设置")
    }

    override fun initData() {
        hideTips.isChecked = "1" == CacheUtil.getString("hideTips")
    }

    override fun setEvent() {
        hideTips.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                CacheUtil.putString("hideTips", "1")
            } else {
                CacheUtil.putString("hideTips", "0")
            }
        }
    }
}