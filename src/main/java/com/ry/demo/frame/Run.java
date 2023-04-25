package com.ry.demo.frame;

import com.ry.demo.module.AutoRegisterModule;
import com.ry.demo.module.LoginModule;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 主动注册实现函数，进行主动注册监听
 */
public class Run extends JFrame {
    // 主动注册监听回调
    private final ServiceCB servicCallback = new ServiceCB();
    // 设备断线通知回调
    private final DisConnectCallBack disConnectCallback = new DisConnectCallBack();

    /**
     * 主动注册
     * @param ServiceAddress 服务器公网IP
     * @param ServicePort  服务器注册端口默认为9500
     * */
    public boolean AutoListen(String ServiceAddress , int ServicePort){
        //window系统下监听设备返回信息函数，必须增加不增加无法返回注册设备信息
        pack();
        //初始化
        LoginModule.init(disConnectCallback,null);
        if (ServiceAddress == null){
            System.out.println("service is null");
        }
        if (ServicePort == 0){
            System.out.println("service port is zero");
        }
        //进行主动注册开启服务
        return(AutoRegisterModule.startServer(ServiceAddress, ServicePort, servicCallback));
    }
    /**
     * 获取本地地址
     * @return 获取本机ip
     */
    public String getHostAddress() {
        String address = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            address = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return address;
    }
}

