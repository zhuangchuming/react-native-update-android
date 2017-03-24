使用
npm install git+https://github.com/cxjpxl/react-native-update-android.git --save  安装模块



react-native link  链接模块


使用方法
DownUpdataAndroid.updataApp(versionName,downUrl);


DownUpdataAndroid.getAppDevice(
            (deviceUuid,
             osVersion,
             phoneVersion,
             versionName) => {
                let phoneDevice = "唯一id：" + deviceUuid + "\n" +
                    "系统版本：" + osVersion + "\n" +
                    "手机型号：" + phoneVersion + "\n" +
                    "应用版本：" + versionName;
              
            });
