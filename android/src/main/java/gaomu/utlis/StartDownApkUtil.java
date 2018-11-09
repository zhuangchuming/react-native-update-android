package gaomu.utlis;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import java.io.File;

import gaomu.common.Config;
import gaomu.entry.DeviceInfo;
import gaomu.service.ApkDownService;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by cxj on 17-3-22.
 */

public class StartDownApkUtil {

    public static String directoryName ="";
    public StartDownApkUtil(String directoryName){
        this.directoryName = directoryName;
    }
    //开始下载apk
    public static  void startDownApk(Context context, String version, String downUrl){
        if(context == null)  return ;
        PreferencesUtils.putString(context.getApplicationContext(),Config.VERSION_KEY,version);
        PreferencesUtils.putString(context.getApplicationContext(),Config.DOWN_KEY,downUrl);
        PreferencesUtils.putString(context.getApplicationContext(),Config.DIRECTORYNAME,directoryName);
        checkSdPremission(context);
    }

    private static void checkSdPremission(Context context){
       startService(context);
    }

    public static void startService(Context context){
        long downId = PreferencesUtils.getLong(context.getApplicationContext(),Config.DOWN_ID_KEY,-1);
        String version = PreferencesUtils.getString(context.getApplicationContext(), Config.VERSION_KEY,"");
        if(TextUtils.isEmpty(version)) return;
        if(downId != -1){
            DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            Cursor c = manager.query(new DownloadManager.Query().setFilterById(downId));
            if(c!=null&&c.getCount()>0){
                c.moveToFirst();
                int state = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if(state == DownloadManager.STATUS_SUCCESSFUL){
                    String sd = FileUtli.getSDPath();
                    if(TextUtils.isEmpty(sd)){
                        return ;
                    }
                    //这里已经带版本号检测
                    File file = new File(sd+"/"+directoryName+"/"+version+".apk");
                    if(file.exists()){
                        installAPK(context, file);
                        return;
                    }else{
                        File myDir = new File(sd+"/"+directoryName+"/");
                        FileUtli.deleteAllFiles(myDir);
                    }
                    stopDownApk(context);
                    context.getApplicationContext().startService(  //启动服务重新下载
                            new Intent(context.getApplicationContext(),
                                    ApkDownService.class));

                }else if(state == DownloadManager.STATUS_FAILED ||state == DownloadManager.STATUS_PAUSED ){  //失败的话
                    stopDownApk(context);
                    manager.remove(downId);
                    context.getApplicationContext().startService(  //启动服务重新下载
                            new Intent(context.getApplicationContext(),
                                    ApkDownService.class));
                }
                c.close();
            }else{
                stopDownApk(context);
                String sd = FileUtli.getSDPath();
                if(TextUtils.isEmpty(sd)){
                    return ;
                }
                File myDir = new File(sd+"/"+directoryName+"/");
                FileUtli.deleteAllFiles(myDir);
                context.getApplicationContext().startService(  //启动服务重新下载
                        new Intent(context.getApplicationContext(),
                                ApkDownService.class));
            }
        }else{
            stopDownApk(context);
            context.getApplicationContext().startService(  //启动服务重新下载
                    new Intent(context.getApplicationContext(),
                            ApkDownService.class));
        }
    }


    //停止下载
    public static void stopDownApk(Context context){
         context.getApplicationContext().stopService(new Intent(context.getApplicationContext(), ApkDownService.class));
    }

    //开始安装
    public static void installAPK(Context context, File file) {
        Intent intents = new Intent();
        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intents.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24 ) {//判断版本大于等于7.0 Build.VERSION_CODES.N
            // "sven.com.fileprovider.fileprovider"即是在清单文件中配置的authorities
            // 通过FileProvider创建一个content类型的Uri
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
            intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
        } else {
            uri = Uri.fromFile(file);
        }
        intents.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intents);
    }


    public static DeviceInfo getAppDevice(Context context) {
        String versionName = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;    // 应用版本号
        } catch (PackageManager.NameNotFoundException e) {

        }

        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        String deviceUuid = szImei;    // 设备唯一编号
        String osVersion = "android" + android.os.Build.VERSION.RELEASE;  // 系统版本
        String phoneVersion = android.os.Build.MANUFACTURER + android.os.Build.MODEL;// 终端型号

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.deviceUuid = deviceUuid;
        deviceInfo.osVersion = osVersion;
        deviceInfo.phoneVersion = phoneVersion;
        deviceInfo.versionName = versionName;
        return deviceInfo;
    }

    public static void compliteDown(Context context, long downId ){
        long id = PreferencesUtils.getLong(context.getApplicationContext(), Config.DOWN_ID_KEY,-1);
        if(id!=downId){
            return ;
        }
        String version = PreferencesUtils.getString(context.getApplicationContext(), Config.VERSION_KEY,"");
        DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        Cursor c = manager.query(new DownloadManager.Query().setFilterById(downId));
        if(c!=null) {
            c.moveToFirst();
            int state = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (state == DownloadManager.STATUS_SUCCESSFUL) {
                String sd = FileUtli.getSDPath();
                if (TextUtils.isEmpty(sd)) {
                    return;
                }
                File file = new File(sd + "/"+directoryName+"/" + version + ".apk");
                if (file.exists()) {
                    StartDownApkUtil.installAPK(context, file);
                    StartDownApkUtil.stopDownApk(context);
                    return;
                }

            }
        }
    }
}

