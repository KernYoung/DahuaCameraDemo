package com.ry.demo.frame;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * 设备重连回调，用与监听离线设备重连的状态，将重连成功后的设备信息进行回传
 */
public class HaveReConnectCallBack implements NetSDKLib.fHaveReConnect {
    public HaveReConnectCallBack() {
    }

    private static class CallBackHolder {
        private static HaveReConnectCallBack instance = new HaveReConnectCallBack();
    }

    public static HaveReConnectCallBack getInstance() {
        return CallBackHolder.instance;
    }

    public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);

    }
}

