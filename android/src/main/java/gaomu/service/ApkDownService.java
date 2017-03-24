package gaomu.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

import gaomu.common.Config;
import gaomu.utlis.FileUtli;
import gaomu.utlis.PreferencesUtils;
import gaomu.utlis.StartDownApkUtil;

/**
 * Created by cxj on 17-3-22.
 */

public class ApkDownService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String version = "";

    private String downUrl = "";

    private DownloadManager manager = null;

    private DownloadCompleteReceiver receiver = null;

    @Override
    public void onCreate() {
        super.onCreate();
        version = PreferencesUtils.getString(this.getApplicationContext(), Config.VERSION_KEY,"");
        downUrl = PreferencesUtils.getString(this.getApplicationContext(),Config.DOWN_KEY,"");
        if(version == null || version.trim().equals("")){
            stopThisService();
            return;
        }
        if(downUrl == null || downUrl.trim().equals("")){
            stopThisService();
            return;
        }
        //能进服务100%有sd的权限
        String sd = FileUtli.getSDPath();
        if(TextUtils.isEmpty(sd)){
            stopThisService();
            return ;
        }

        File destDir = new File(sd+"/gaomu");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        receiver = new DownloadCompleteReceiver();
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

	try {
            Thread.sleep(1000);
            startDown();
        } catch (InterruptedException e) {
        }

    }


    //开始下载 监听下载完成  启动安装  停止当前服务
    private void startDown(){
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downUrl));
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            down.setVisibleInDownloadsUi(true);
            down.setDestinationInExternalPublicDir("gaomu", version+".apk");
            long downId = manager.enqueue(down);
            PreferencesUtils.putLong(this.getApplicationContext(),Config.DOWN_ID_KEY,downId);
        } catch (Exception e) {
            e.toString();
	    stopThisService();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }


    private void stopThisService(){
        stopSelf();
    }


    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                StartDownApkUtil.compliteDown(context,downId);
            }
        }
    }



}
