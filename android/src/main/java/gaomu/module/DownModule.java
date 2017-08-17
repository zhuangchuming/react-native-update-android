package gaomu.module;

import android.content.Context;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import gaomu.entry.DeviceInfo;
import gaomu.utlis.StartDownApkUtil;

/**
 * Created by cxj on 17-3-23.
 */

public class DownModule extends ReactContextBaseJavaModule {


     boolean isZw =false;
     Context context = null;
    public DownModule(ReactApplicationContext reactContext,boolean isZw){
        super(reactContext);
        context = reactContext;
        this.isZw = isZw;
    }


    @Override
    public String getName() {
        return "DownUpdataAndroid";
    }

    //更新app
    @ReactMethod
    public void updataApp(String version, String downUrl) {
        StartDownApkUtil.startDownApk(context,version,downUrl);
    }


    //获取版本信息
    @ReactMethod
    public void getAppDevice(Callback callback) {
        DeviceInfo deviceInfo = StartDownApkUtil.getAppDevice(context);
        callback.invoke(deviceInfo.deviceUuid,deviceInfo.osVersion,deviceInfo.phoneVersion,deviceInfo.versionName);
    }


    @ReactMethod
    public void getZwStatu(Callback callback) {
        callback.invoke(isZw);
    }
}

