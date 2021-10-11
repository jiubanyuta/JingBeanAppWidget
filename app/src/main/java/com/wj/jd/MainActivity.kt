package com.wj.jd

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.wj.jd.bean.VersionBean
import com.wj.jd.dialog.NewStyleDialog
import com.wj.jd.util.CacheUtil
import com.wj.jd.util.DeviceUtil
import com.wj.jd.util.HttpUtil
import com.wj.jd.util.StringCallBack
import com.wj.jd.widget.UpdateDataService

class MainActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setTitle("京豆")
    }

    override fun initData() {
        checkAppUpdate()
        startUpdateService()
    }

    private fun checkAppUpdate() {
        HttpUtil.getAppVer(object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    var gson = Gson()
                    val versionBean = gson.fromJson(result, VersionBean::class.java)
                    if (DeviceUtil.getAppVersionName().equals(versionBean.release)) {
                        Toast.makeText(this@MainActivity, "当前已是最新版本", Toast.LENGTH_SHORT).show()
                    } else {
                        if ("1" == versionBean.isUpdate) {
                            createDialog("版本更新", versionBean.content, "更新", object : NewStyleDialog.OnRightClickListener {
                                override fun rightClick() {

                                }
                            })
                        } else {
                            createDialog("版本更新", versionBean.content, "取消", "更新", object :NewStyleDialog.OnLeftClickListener{
                                override fun leftClick() {
                                    disMissDialog()
                                }
                            }, object : NewStyleDialog.OnRightClickListener {
                                override fun rightClick() {

                                }
                            })
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }

        })
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

        setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
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