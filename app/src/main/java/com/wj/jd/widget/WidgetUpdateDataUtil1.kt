package com.wj.jd.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.wj.jd.MainActivity
import com.wj.jd.MyApplication
import com.wj.jd.R
import com.wj.jd.bean.JingDouBean
import com.wj.jd.bean.RedPacket
import com.wj.jd.bean.UserBean1
import com.wj.jd.bean.VersionBean
import com.wj.jd.util.*
import com.wj.jd.util.CacheUtil.getString
import com.wj.jd.util.TimeUtil.getCurrentData
import com.wj.jd.util.TimeUtil.getCurrentHH
import com.wj.jd.util.TimeUtil.parseTime
import org.json.JSONObject
import java.lang.Exception

/**
 * author wangjing
 * Date 2021/10/13
 * Description
 */
object WidgetUpdateDataUtil1 {
    private lateinit var remoteViews: RemoteViews
    private var gson = Gson()
    private var page = 1
    private var todayTime: Long = 0
    private var yesterdayTime: Long = 0
    lateinit var thisKey: String

    @Synchronized
    fun updateWidget(key: String) {
        thisKey = key
        val str = HttpUtil.getCK(thisKey)
        if (TextUtils.isEmpty(str)) return

        remoteViews = RemoteViews(MyApplication.mInstance.packageName, R.layout.widges_layout)
        pullWidget()

        checkUpdate()

        getUserInfo()
        getUserInfo1()

        page = 1
        UserBean1.todayBean = 0
        UserBean1.ago1Bean = 0
        todayTime = TimeUtil.getTodayMillis(0)
        yesterdayTime = TimeUtil.getTodayMillis(-1)

        getJingBeanData()

        getRedPackge()
    }

    private fun checkUpdate() {
        HttpUtil.getAppVer(object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    var gson = Gson()
                    val versionBean = gson.fromJson(result, VersionBean::class.java)
                    if (DeviceUtil.getAppVersionName().equals(versionBean.release)) {
                        UserBean1.updateTips = ""
                    } else {
                        UserBean1.updateTips = versionBean.widgetTip
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }

        })
    }

    private fun getRedPackge() {
        HttpUtil.getRedPack(thisKey, "https://m.jingxi.com/user/info/QueryUserRedEnvelopesV2?type=1&orgFlag=JD_PinGou_New&page=1&cashRedType=1&redBalanceFlag=1&channel=1&_=" + System.currentTimeMillis() + "&sceneval=2&g_login_type=1&g_ty=ls", object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    val redPacket = gson.fromJson(result, RedPacket::class.java)
                    UserBean1.hb = redPacket.data.balance
                    UserBean1.gqhb = redPacket.data.expiredBalance
                    UserBean1.countdownTime = redPacket.data.countdownTime / 60 / 60
                    setData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }
        })
    }

    private fun getUserInfo1() {
        HttpUtil.getUserInfo1(thisKey, object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    val job = JSONObject(result)
                    UserBean1.jxiang = job.optJSONObject("user").optString("uclass").replace("京享值", "")
                    setData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {

            }
        })
    }

    private fun getUserInfo() {
        HttpUtil.getUserInfo(thisKey, object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    val job = JSONObject(result)
                    try {
                        UserBean1.nickName = job.optJSONObject("data").optJSONObject("userInfo").optJSONObject("baseInfo").optString("nickname")
                        UserBean1.userLevel = job.optJSONObject("data").optJSONObject("userInfo").optJSONObject("baseInfo").optString("userLevel")
                        UserBean1.levelName = job.optJSONObject("data").optJSONObject("userInfo").optJSONObject("baseInfo").optString("levelName")
                        UserBean1.headImageUrl = job.optJSONObject("data").optJSONObject("userInfo").optJSONObject("baseInfo").optString("headImageUrl")
                        UserBean1.isPlusVip = job.optJSONObject("data").optJSONObject("userInfo").optString("isPlusVip")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        UserBean1.beanNum = job.optJSONObject("data").optJSONObject("assetInfo").optString("beanNum")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    setData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }

        })
    }

    @Synchronized
    private fun getJingBeanData() {
        HttpUtil.getJD(thisKey, page, object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    Log.i("====", result)
                    val jingDouBean = gson.fromJson(result, JingDouBean::class.java)
                    val dataList = jingDouBean.detailList
                    var isFinish = true
                    for (i in dataList.indices) {
                        val detail = dataList[i]
                        val beanDay = parseTime(detail.date)!!
                        if (beanDay > todayTime) {
                            if (detail.amount > 0) {
                                UserBean1.todayBean = UserBean1.todayBean + detail.amount
                            }
                        } else {
                            isFinish = false
                            break
                        }
                    }
                    if (isFinish) {
                        page++
                        getJingBeanData()
                    } else {
                        get1AgoBeanData()
                        setData()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }
        })
    }

    private fun get1AgoBeanData() {
        HttpUtil.getJD(thisKey, page, object : StringCallBack {
            override fun onSuccess(result: String) {
                try {
                    val jingDouBean = gson.fromJson(result, JingDouBean::class.java)
                    val dataList = jingDouBean.detailList
                    var isFinish = true
                    for (i in dataList.indices) {
                        val detail = dataList[i]
                        val beanDay = parseTime(detail.date)!!
                        if (beanDay < todayTime && beanDay > yesterdayTime) {
                            if (detail.amount > 0) {
                                UserBean1.ago1Bean = UserBean1.ago1Bean + detail.amount
                            }
                        } else if (beanDay < yesterdayTime) {
                            isFinish = false
                            break
                        }
                    }
                    if (isFinish) {
                        page++
                        get1AgoBeanData()
                    } else {
                        setData()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail() {
            }
        })
    }

    private fun setData() {
        if ("1" == getString("hideTips")) {
            remoteViews.setViewVisibility(R.id.updateTime, View.GONE)
            remoteViews.setViewVisibility(R.id.tips, View.GONE)
        } else {
            remoteViews.setViewVisibility(R.id.updateTime, View.VISIBLE)
            remoteViews.setViewVisibility(R.id.tips, View.VISIBLE)
        }

        if ("1" == getString("hideNichen")) {
            remoteViews.setTextViewText(R.id.nickName, "***")
        } else {
            remoteViews.setTextViewText(R.id.nickName, UserBean1.nickName)
        }

        if ("1" == UserBean1.isPlusVip) {
            remoteViews.setViewVisibility(R.id.plusIcon, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.plusIcon, View.GONE)
        }

        if (TextUtils.isEmpty(UserBean1.updateTips)) {
            remoteViews.setViewVisibility(R.id.haveNewVersion, View.GONE)
        } else {
            remoteViews.setViewVisibility(R.id.haveNewVersion, View.VISIBLE)
            remoteViews.setTextViewText(R.id.haveNewVersion, UserBean1.updateTips)
        }

        var paddingType = CacheUtil.getString("paddingType")
        if (TextUtils.isEmpty(paddingType) || "padding15" == paddingType) {
            remoteViews.setViewPadding(R.id.rootParent, R.dimen.dp_15.dmToPx(), 0, R.dimen.dp_15.dmToPx(), 0)
        } else if ("padding0" == paddingType) {
            remoteViews.setViewPadding(R.id.rootParent, 0, 0, 0, 0)
        } else if ("padding5" == paddingType) {
            remoteViews.setViewPadding(R.id.rootParent, R.dimen.dp_5.dmToPx(), 0, R.dimen.dp_5.dmToPx(), 0)
        } else if ("padding10" == paddingType) {
            remoteViews.setViewPadding(R.id.rootParent, R.dimen.dp_10.dmToPx(), 0, R.dimen.dp_10.dmToPx(), 0)
        }else if ("padding20" == paddingType) {
            remoteViews.setViewPadding(R.id.rootParent, R.dimen.dp_20.dmToPx(), 0, R.dimen.dp_20.dmToPx(), 0)
        }

        val cleatInt2 = Intent(MyApplication.mInstance, MainActivity::class.java)
        cleatInt2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val clearIntent2 = PendingIntent.getActivity(MyApplication.mInstance, 3, cleatInt2, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.rightContent, clearIntent2)

        remoteViews.setTextViewText(R.id.beanNum, UserBean1.beanNum)
        remoteViews.setTextViewText(R.id.todayBean, "+" + UserBean1.todayBean)
        remoteViews.setTextViewText(R.id.todayBeanNum, UserBean1.todayBean.toString())
        remoteViews.setTextViewText(R.id.oneAgoBeanNum, UserBean1.ago1Bean.toString())
        remoteViews.setTextViewText(R.id.updateTime, "数据更新于:" + getCurrentData())
        remoteViews.setTextViewText(R.id.hongbao, UserBean1.hb)
        try {
            if (getCurrentHH() + UserBean1.countdownTime > 24) {
                remoteViews.setTextViewText(R.id.guoquHb, "明日过期:" + UserBean1.gqhb)
            } else {
                remoteViews.setTextViewText(R.id.guoquHb, "今日过期:" + UserBean1.gqhb)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            remoteViews.setTextViewText(R.id.guoquHb, "今日过期:" + UserBean1.gqhb)
        }
        remoteViews.setTextViewText(R.id.jingXiang, UserBean1.jxiang)

        val cleatIntent = Intent()
        cleatIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        cleatIntent.action = "com.scott.sayhi1"
        val clearIntent3 = PendingIntent.getBroadcast(MyApplication.mInstance, 1, cleatIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.headImg, clearIntent3)

        if (TextUtils.isEmpty(UserBean1.headImageUrl)) {
            Glide.with(MyApplication.mInstance)
                .load(R.mipmap.icon_head_def)
                .into(object : SimpleTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        val head = BitmapUtil.drawableToBitmap(resource)
                        remoteViews.setImageViewBitmap(R.id.headImg, BitmapUtil.createCircleBitmap(head))
                        pullWidget()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        pullWidget()
                    }
                })
        } else {
            Glide.with(MyApplication.mInstance)
                .load(UserBean1.headImageUrl)
                .into(object : SimpleTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        val head = BitmapUtil.drawableToBitmap(resource)
                        remoteViews.setImageViewBitmap(R.id.headImg, BitmapUtil.createCircleBitmap(head))
                        pullWidget()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        pullWidget()
                    }
                })
        }
    }

    private fun pullWidget() {
        val manager = AppWidgetManager.getInstance(MyApplication.mInstance)
        val componentName = ComponentName(MyApplication.mInstance, MyAppWidgetProvider1::class.java)
        manager.updateAppWidget(componentName, remoteViews)
    }
}