package com.wj.jd.util;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.wj.jd.MyApplication;

public class DeviceUtil {

    /**
     * 获取当前app version name
     */
    public static String getAppVersionName(Context context) {
        String appVersionName = "-1";
        try {
            PackageInfo packageInfo = MyApplication.getMInstance().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (Exception e) {
        }
        return appVersionName;
    }
}
