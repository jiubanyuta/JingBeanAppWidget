package com.wj.jd.util

import android.text.TextUtils
import android.util.Log
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.wj.jd.StringCallBack

/**
 * author wangjing
 * Date 2021/9/17
 * Description 网络请求通用核心基类
 */
object HttpUtil {
    @JvmOverloads
    fun getAppVer(path: String, callback: StringCallBack?) {
        var str = CacheUtil.getString("ck")
        if(TextUtils.isEmpty(str)) return
        str =
            str + "User-Agent" + "=" + "jdapp;iPhone;10.0.2;14.3;network/wifi;Mozilla/5.0 (iPhone; CPU iPhone OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1;"
        str = str + "Accept-Language" + "=" + "zh-cn;"
        str = str + "Referer" + "=" + "https://home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&"
        str = str + "Accept-Encoding" + "=" + "gzip, deflate, br"
        OkGo.get<String>(path)
            .tag("context")
            .headers("Host", "me-api.jd.com")
            .headers("Accept", "*/*")
            .headers("Connection", "keep-alive")
            .headers("Cookie", str)

            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Log.i("====出参解密", response.body())
                    callback?.onSuccess(response.body())
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                }
            })
    }

    @JvmOverloads
    fun getUserInfo(path: String, callback: StringCallBack?) {
        var str = CacheUtil.getString("ck")
        if(TextUtils.isEmpty(str)) return
        str =
            str + "User-Agent" + "=" + "jdapp;iPhone;10.0.2;14.3;network/wifi;Mozilla/5.0 (iPhone; CPU iPhone OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1;"
        str = str + "Accept-Language" + "=" + "zh-cn;"
        str = str + "Referer" + "=" + "https://home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&"
        str = str + "Accept-Encoding" + "=" + "gzip, deflate, br"
        OkGo.get<String>(path)
            .tag("context")
            .headers("Host", "me-api.jd.com")
            .headers("Accept", "*/*")
            .headers("Connection", "keep-alive")
            .headers("Cookie", str)

            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Log.i("====出参解密", response.body())
                    callback?.onSuccess(response.body())
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                }
            })
    }

    @JvmOverloads
    fun getJD(path: String, page: Int, callback: StringCallBack?) {
        var str = CacheUtil.getString("ck")
        if(TextUtils.isEmpty(str)) return
        OkGo.post<String>(path)
            .tag("context")
            .params("body", "{\"pageSize\":\"20\",\"page\":\"$page\"}")
            .params("appid", "ld")
            .headers(
                "User-Agent",
                "jdapp;android;10.1.0;9;network/wifi;Mozilla/5.0 (Linux; Android 9; MI 6 Build/PKQ1.190118.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044942 Mobile Safari/537.36"
            )
            .headers("Host", "api.m.jd.com")
            .headers("Content-Type", "application/x-www-form-urlencoded")
            .headers("Cookie", str)

            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Log.i("====出参解密", response.body())
                    callback?.onSuccess(response.body())
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                }
            })
    }

    @JvmOverloads
    fun getRedPack(path: String, callback: StringCallBack?) {
        var str = CacheUtil.getString("ck")
        if(TextUtils.isEmpty(str)) return
        OkGo.get<String>(path)
            .tag("context")
            .headers("Host", "m.jingxi.com")
            .headers("Accept", "*/*")
            .headers("Connection", "keep-alive")
            .headers("Accept-Language", "zh-cn")
            .headers("Referer", "https://st.jingxi.com/my/redpacket.shtml?newPg=App&jxsid=16156262265849285961")
            .headers("User-Agent", "jdapp;android;10.1.6;8.1.0;network/wifi;Mozilla/5.0 (Linux; Android 8.1.0; 16 X Build/OPM1.171019.026; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044942 Mobile Safari/537.36")
            .headers("Cookie", str)
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    callback?.onSuccess(response.body())
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                }
            })
    }

    private fun cancel(tag: Any) {
        OkGo.getInstance().cancelTag(tag)
    }

    private fun cancelAll() {
        OkGo.getInstance().cancelAll()
    }

}