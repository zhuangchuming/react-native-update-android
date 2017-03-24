'use strict';

import {NativeModules} from 'react-native';
const RCTDownUpdataAndroid = NativeModules.DownUpdataAndroid;

var DownUpdataAndroid = {


    updataApp: function (
    version: string,
    downUrl: string
  ): void {
    RCTDownUpdataAndroid.updataApp(version, downUrl);
  },

  getAppDevice:function(
    callback:Callback
  ):void {
    RCTDownUpdataAndroid.getAppDevice(callback);
  },
};

module.exports = DownUpdataAndroid;