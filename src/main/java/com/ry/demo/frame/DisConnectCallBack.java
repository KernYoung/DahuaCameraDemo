package com.ry.demo.frame;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * 设备断线监听回调用于监听设备的在线状态，如果掉线进行消息回传
 */

public class DisConnectCallBack implements NetSDKLib.fDisConnect {

    public DisConnectCallBack() {
    }

    private static class CallBackHolder {
        private static DisConnectCallBack instance = new DisConnectCallBack();
    }

    public static DisConnectCallBack getInstance() {
        return CallBackHolder.instance;
    }

    public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
    }
}

