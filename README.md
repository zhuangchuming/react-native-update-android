
```
申明：
本模块所有权来自https://github.com/cxjpxl/react-native-update-android.git
本模块在其基础上做了：
    1、android 8.0安装应用包的权限配置；
    2、生成的sd卡目录名字由new DownPackage("xxx")时传入；
    3、android 7.0以上应用间共享文件文件；
```

```
注意：
使用本模块在android6.0以上时，由于模块下载的apk放在了外部储存目录，故在调用前，先使用：react-native-permissions，做权限检查及申请，如果没有申请直接调用的话，是不会成功下载的。
```

> 应用场景：android apk下载安装


```
如何使用：
1、安装模块：
npm install git+https://github.com/zhuangchuming/react-native-update-android.git --save  
自动连接：
react-native link  react-native-update-android

2、在MainApplication中添加：
import com.cxj.react_native_update_android.DownPackage;
...
new DownPackage("xxx"),xxx为sd新建的目录名字

3、js文件引入
import DownUpdataAndroid from 'react-native-update-android';
//使用下面的方法下载更新
DownUpdataAndroid.updataApp(version,url);//version为下载的版本号,url为下载的地址
```

