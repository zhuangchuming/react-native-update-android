package com.cxj.react_native_update_android;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gaomu.module.DownModule;

/**
 * Created by cxj on 17-3-23.
 */

public class DownPackage implements ReactPackage {

   boolean isZw = false;
    public DownPackage(boolean isZw){
        this.isZw = isZw;
    }


    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new DownModule(reactContext,this.isZw));
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
