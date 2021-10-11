package com.wj.jd

import android.Manifest
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloader
import com.permissionx.guolindev.PermissionX
import com.wj.jd.bean.SimpleFileDownloadListener
import com.wj.jd.bean.VersionBean
import com.wj.jd.dialog.NewStyleDialog
import com.wj.jd.util.CacheUtil
import com.wj.jd.util.DeviceUtil
import com.wj.jd.util.HttpUtil
import com.wj.jd.util.StringCallBack
import com.wj.jd.widget.UpdateDataService
import com.zhy.base.fileprovider.FileProvider7
import java.io.File

class MainActivity : BaseActivity() {
    private lateinit var notificationUpdateReceiver: NotificationUpdateReceiver

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setTitle("京豆")
    }

    override fun initData() {
        checkAppUpdate()
        startUpdateService()
        initNotification()
    }

    private fun initNotification() {
        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("com.scott.sayhi")
        notificationUpdateReceiver = NotificationUpdateReceiver()
        registerReceiver(notificationUpdateReceiver, intentFilter2)
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
                                    downLoadApk(versionBean.content_url)
                                }
                            })
                        } else {
                            createDialog("版本更新", versionBean.content, "取消", "更新", object : NewStyleDialog.OnLeftClickListener {
                                override fun leftClick() {
                                    disMissDialog()
                                }
                            }, object : NewStyleDialog.OnRightClickListener {
                                override fun rightClick() {
                                    disMissDialog()
                                    downLoadApk(versionBean.content_url)
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

    //context.getExternalFilesDir()
    private fun downLoadApk(contentUrl: String?) {
        downLoad(contentUrl)
    }

    private lateinit var pd: ProgressDialog

    ///storage/emulated/0/Android/data/<包名>/files
    ///storage/emulated/0/Android/data/com.wj.jd/files
    private fun downLoad(contentUrl: String?) {
        if (TextUtils.isEmpty(contentUrl)) return

        pd = ProgressDialog(this)
        pd.setTitle("提示")
        pd.setMessage("软件版本更新中，请稍后...")
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) //设置带进度条的

        pd.max = 100
        pd.setCancelable(false)
        pd.show()

        var pathParent = filesDir.path + "/downApk"
        var apkName = pathParent + System.currentTimeMillis() + ".apk"
        val file = File(pathParent)
        if (!file.exists()) {
            file.mkdirs()
        }

        FileDownloader.setup(this)
        FileDownloader.getImpl().create(contentUrl)
            .setPath(apkName)
            .setListener(object : SimpleFileDownloadListener() {
                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    val per = soFarBytes / (totalBytes / 100)
                    pd.progress = per
                }

                override fun completed(task: BaseDownloadTask) {
                    pd.dismiss()
                    val file = File(apkName)
                    installApk(file)
                }
            }).start()

    }

    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        FileProvider7.setIntentDataAndType(this, intent, "application/vnd.android.package-archive", file, true)
        startActivity(intent)
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

    inner class NotificationUpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i("====", "NotificationUpdateReceiver")
            startService(Intent(this@MainActivity, UpdateDataService::class.java))
        }
    }
}