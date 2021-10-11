package com.wj.jd

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.wj.jd.util.CacheUtil

class SettingActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        setTitle("小组件设置")
    }

    override fun initData() {
        checkAppUpdate()
        startUpdateService()
    }

    private fun checkAppUpdate() {
    }

    private fun startUpdateService() {
        /*
        * app进入重新启动更新数据后台服务
        * */
        startService(Intent(this, UpdateDataService::class.java))
    }

    override fun setEvent() {
        noCK.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://a.wangjing520.cn:5701/")
            startActivity(intent)
        }

        updateCK.setOnClickListener {
            if (TextUtils.isEmpty(inputCK.text.toString())) {
                Toast.makeText(this, "CK为空，添加失败", Toast.LENGTH_SHORT).show()
            } else {
                CacheUtil.putString("ck", inputCK.text.toString())
                Toast.makeText(this, "CK添加成功", Toast.LENGTH_SHORT).show()
                inputCK.setText("")
                startService(Intent(this, UpdateDataService::class.java))
            }
        }
    }
}