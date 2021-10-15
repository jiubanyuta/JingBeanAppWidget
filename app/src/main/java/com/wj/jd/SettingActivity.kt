package com.wj.jd

import android.widget.Toast
import com.wj.jd.util.CacheUtil
import com.wj.jd.widget.WidgetUpdateDataUtil
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

        hideNichen.isChecked = "1" == CacheUtil.getString("hideTips")

        startUpdateService.isChecked = "1" != CacheUtil.getString("startUpdateService")
    }

    override fun setEvent() {
        hideTips.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                CacheUtil.putString("hideTips", "1")
            } else {
                CacheUtil.putString("hideTips", "0")
            }
        }

        hideNichen.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                CacheUtil.putString("hideNichen", "1")
            } else {
                CacheUtil.putString("hideNichen", "0")
            }
        }

        startUpdateService.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                CacheUtil.putString("startUpdateService", "0")
            } else {
                CacheUtil.putString("startUpdateService", "1")
            }
        }

        settingFinish.setOnClickListener {
            WidgetUpdateDataUtil.updateWidget("ck")
            Toast.makeText(this, "小组件状态更新完毕", Toast.LENGTH_SHORT).show()
        }

        padding0.setOnClickListener {
            CacheUtil.putString("paddingType", "padding0")
            paddingTip.text = "无边距"
        }

        padding5.setOnClickListener {
            CacheUtil.putString("paddingType", "padding5")
            paddingTip.text = "5dp"
        }

        padding10.setOnClickListener {
            CacheUtil.putString("paddingType", "padding10")
            paddingTip.text = "10dp"
        }

        padding15.setOnClickListener {
            CacheUtil.putString("paddingType", "padding15")
            paddingTip.text = "15dp"
        }

        padding20.setOnClickListener {
            CacheUtil.putString("paddingType", "padding20")
            paddingTip.text = "20dp"
        }
    }
}