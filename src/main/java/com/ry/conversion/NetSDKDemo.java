package com.ry.conversion;

import com.netsdk.lib.callback.impl.DefaultDisconnectCallback;
import com.netsdk.lib.callback.impl.DefaultHaveReconnectCallBack;
import com.netsdk.lib.enumeration.ENUMERROR;
import com.netsdk.module.BaseModule;
import com.netsdk.module.entity.DeviceInfo;

public class NetSDKDemo {

  public static void main(String[] args) {
    BaseModule baseModule = new BaseModule();
    baseModule.init(
        DefaultDisconnectCallback.getINSTANCE(), DefaultHaveReconnectCallBack.getINSTANCE(), true);
    DeviceInfo info = baseModule.login("192.168.10.243", 37777, "admin", "admin123");
    if (info.getLoginHandler() != 0L) {
      System.out.println("login success.");
      // 登出
      if (baseModule.logout(info.getLoginHandler())) {
        System.out.println("logout success.");
      } else {
        System.out.println("logout failed.error is " + ENUMERROR.getErrorMessage());
      }
    }
    baseModule.clean();
  }
}
