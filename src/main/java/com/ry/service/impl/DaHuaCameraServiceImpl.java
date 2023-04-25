package com.ry.service.impl;

import com.mysql.cj.util.StringUtils;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.callback.impl.DefaultDisconnectCallback;
import com.netsdk.lib.callback.impl.DefaultHaveReconnectCallBack;
import com.netsdk.lib.enumeration.ENUMERROR;
import com.netsdk.module.BaseModule;
import com.netsdk.module.entity.DeviceInfo;
import com.ry.conversion.BaseDemo;
import com.ry.conversion.CaseMenu;
import com.ry.demo.module.LoginModule;
import com.ry.lib.ToolKits;
import com.ry.service.IDaHuaCameraService;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

import static com.ry.demo.module.DeviceInfo.netSdk;


@Component
public class DaHuaCameraServiceImpl extends BaseDemo implements IDaHuaCameraService   {

    BaseModule baseModule = new BaseModule();
    public static DeviceInfo info;

    @Override
    public boolean loginWithHighSecurity(String ip, int port, String username, String password) {
        baseModule.init(
                DefaultDisconnectCallback.getINSTANCE(), DefaultHaveReconnectCallBack.getINSTANCE(), true);
        info = baseModule.login(ip, port, username, password);
        if (info.getLoginHandler() != 0L) {
            System.out.println("login success.");
            return true;
        }else {
            return false;
        }

        // sdk初始化
//        daHuaCameraService.init();
//        daHuaCameraService.addItem(new CaseMenu.Item(daHuaCameraService, "转码flv", "attachFlv"));
//        // 登录设备
//        return daHuaCameraService.login(ip, port, username, password);
    }

    @Override
    public boolean logout() {
        boolean logoutSuccess;
        // 登出
        if (baseModule.logout(info.getLoginHandler())) {
            System.out.println("logout success.");
            logoutSuccess = true;
        } else {
            System.out.println("logout failed.error is " + ENUMERROR.getErrorMessage());
            logoutSuccess = false;
        }
        baseModule.clean();

        return logoutSuccess;
    }

    @Override
    public boolean push() {
        boolean pushSuccess;
        if (DaHuaCameraServiceImpl.info == null) pushSuccess = false;

        if (info.getLoginHandler() != 0){
            pushSuccess = true;
        }else {
            pushSuccess = false;
        }





        return pushSuccess;
    }


    public static NetSDKLib.LLong startRealPlay(int channel, int stream, Panel realPlayWindow) {
        NetSDKLib.LLong m_hPlayHandle = LoginModule.netsdk.CLIENT_RealPlayEx(LoginModule.m_hLoginHandle, channel, Native.getComponentPointer(realPlayWindow), stream);
//        LoginModule.netsdk.CLIENT_SetRealDataCallBackEx(m_hPlayHandle, RealDataCallBack.getInstance(), null, 31);
        if(m_hPlayHandle.longValue() == 0) {
            System.err.println("开始实时监视失败，错误码" + ToolKits.getErrorCodePrint());
        } else {
            System.out.println("Success to start realplay");
        }
        return m_hPlayHandle;
    }



    /**
     * \if ENGLISH_LANG
     * Set Capture Picture Callback
     * \else
     * 设置抓图回调函数
     * \endif
     */
    public static void setSnapRevCallBack(NetSDKLib.fSnapRev cbSnapReceive){
        LoginModule.netsdk.CLIENT_SetSnapRevCallBack(cbSnapReceive, null);
    }



}

