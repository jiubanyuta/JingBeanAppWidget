package com.wj.jd

import android.Manifest
import android.app.ProgressDialog
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
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
        PermissionX.init(this@MainActivity)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    downLoad(contentUrl)
                } else {
                    Toast.makeText(this, "版本更新需要同意存储权限", Toast.LENGTH_LONG).show()
                }
            }
    }

    private lateinit var pd: ProgressDialog

    private fun downLoad(contentUrl: String?) {
        if (TextUtils.isEmpty(contentUrl)) return

        pd = ProgressDialog(this)
        pd.setTitle("提示")
        pd.setMessage("软件版本更新中，请稍后...")
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) //设置带进度条的

        pd.setMax(100)
        pd.setCancelable(false)
        pd.show()

        FileDownloader.setup(this)
        FileDownloader.getImpl().create(contentUrl)
            .setPath(Environment.getExternalStorageDirectory().path + File.separator + "downApk" + File.separator, true)
            .setListener(object : SimpleFileDownloadListener() {
                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    val per = soFarBytes / (totalBytes / 100)
                    pd.setProgress(per)
                }

                override fun completed(task: BaseDownloadTask) {
                    pd.dismiss()
                    val file = File(task.path + task.filename)
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
}