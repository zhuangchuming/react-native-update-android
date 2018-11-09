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


     String directoryName ="";
    StartDownApkUtil downutil;
     Context context = null;
    public DownModule(ReactApplicationContext reactContext,String directoryName){
        super(reactContext);
        context = reactContext;
        this.directoryName = directoryName;
        downutil=new StartDownApkUtil(directoryName);
    }

    @Override
    public String getName() {
        return "DownUpdataAndroid";
    }

    //更新app
    @ReactMethod
    public void updataApp(String version, String downUrl) {
        downutil.startDownApk(context,version,downUrl);
    }
}

