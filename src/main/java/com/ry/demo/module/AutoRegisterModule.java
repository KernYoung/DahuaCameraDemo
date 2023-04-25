package com.ry.demo.module;

import com.netsdk.lib.NetSDKLib;
import com.ry.demo.module.LoginModule;
import com.netsdk.lib.NetSDKLib.LLong;
import com.netsdk.lib.NetSDKLib.fServiceCallBack;
import com.netsdk.lib.NetSDKLib;
import com.ry.lib.ToolKits;

public class AutoRegisterModule {

    // 监听服务句柄
    public static NetSDKLib.LLong mServerHandler = new LLong(0);

    // 设备信息
    public static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();

    /**
     * 开启服务
     *
     * @param address  本地IP地址
     * @param port     本地端口, 可以任意
     * @param callback 回调函数
     */
    public static boolean startServer(String address, int port, fServiceCallBack callback) {
        //调用监听接口实现服务的开启
        mServerHandler = LoginModule.netsdk.CLIENT_ListenServer(address, port, 1000, callback, null);
        if (0 == mServerHandler.longValue()) {
            System.err.println("Failed to start server." + ToolKits.getErrorCodePrint());
//            System.err.println("Failed to start server." );
        } else {
            System.out.printf("Start server, [Server address %s][Server port %d]\n", address, port);
        }
        return mServerHandler.longValue() != 0;
    }

    /**
     * 结束服务
     */
    public static boolean stopServer() {
        boolean bRet = false;

        if (mServerHandler.longValue() != 0) {
            bRet = LoginModule.netsdk.CLIENT_StopListenServer(mServerHandler);
            mServerHandler.setValue(0);
            System.out.println("Stop server!");
        }

        return bRet;
    }

}
