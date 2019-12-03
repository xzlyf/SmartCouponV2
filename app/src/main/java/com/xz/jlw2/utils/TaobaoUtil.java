package com.xz.jlw2.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

public class TaobaoUtil {
    private static final String packageName = "com.taobao.taobao";

    public static void jump2TaobaoQuan(Context context, String QuanUrl) {

        Uri uri = Uri.parse(QuanUrl);

        if (checkPackage(context, packageName)) {
            //跳转到淘宝客户端优惠券界面
            Intent intent = new Intent();
            intent.setAction("Android.intent.action.VIEW");
            intent.setData(uri);
            intent.setClassName(packageName, "com.taobao.browser.BrowserActivity");
            context.startActivity(intent);
        } else {

            //跳转到浏览器优惠券界面
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Toast.makeText(context, "未检测到手机淘宝", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param context
     * @param packeage
     * @return true 存在 false 不存在
     */
    public static boolean checkPackage(Context context, String packeage) {
        PackageManager manager = context.getPackageManager();
        //获取已安装的应用包信息
        List<PackageInfo> infos = manager.getInstalledPackages(0);
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).packageName.equalsIgnoreCase(packeage)){
                return true;
            }
        }
        return false;
    }
}
