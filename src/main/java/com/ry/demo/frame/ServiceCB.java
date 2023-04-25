package com.ry.demo.frame;

import com.ry.conversion.CaseMenu;
import com.ry.controller.PreInfo;
import com.ry.conversion.RealPlayByDataTypeDemo;
import com.ry.demo.module.DeviceInfo;
import com.netsdk.lib.NetSDKLib;
import com.ry.service.IInstructService;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * 注册服务监听回调
 */
@Component
public class ServiceCB implements NetSDKLib.fServiceCallBack,IInstructService {
    //设备信息类对象用于获取注册后设备信息
    DeviceInfo info = new DeviceInfo();

    /**
     * 前端发送信息
     *
     * @param preInfo  前端发送信息
     */
    private static final Set<PreInfo> preInfoSet = new HashSet<PreInfo>();

    @Override
    public int invoke(NetSDKLib.LLong lHandle, String pIp, int wPort,
                      int lCommand, Pointer pParam, int dwParamLen,
                      Pointer dwUserData) {
        // 将 pParam 转化为序列号
        byte[] buffer = new byte[dwParamLen];
        pParam.read(0, buffer, 0, dwParamLen);
        String deviceId = "";
        try {
            deviceId = new String(buffer, "GBK").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.printf("Register Device Info [Device address %s][port %s][DeviceID %s] \n", pIp, wPort, deviceId);

        switch (lCommand) {
            case NetSDKLib.EM_LISTEN_TYPE.NET_DVR_DISCONNECT: {  // 验证期间设备断线回调
                info.setM_strIp("");
                info.setM_nPort(0);
                break;
            }
            case NetSDKLib.EM_LISTEN_TYPE.NET_DVR_SERIAL_RETURN: { // 设备注册携带序列号
                info.setM_strIp(pIp);
                info.setM_nPort(wPort);
                info.setM_srtId(deviceId);

                /**
                 * 判断前端是否传输数据
                 * */
                if (preInfoSet.size() != 0) {
                    //这判断设备id是否相同
                    preInfoSet.forEach(preInfo -> {
                        if (info.getM_srtId().equals(preInfo.getPreDeviceId())) {
                            preInfoSet.remove(preInfo);
                            System.out.println("前端触发ID：" + preInfo.getPreDeviceId());
                            System.out.println("设备当前ID：" + info.getM_srtId());
                            if (preInfo.getPreDeviceId().equals(info.getM_srtId())) {

                                new Thread(() -> {
                                    System.out.println("开始录制");
                                    info.setM_strUser("自己的大华相机用户名");
                                    info.setM_strPassword("自己的大华相机密码");

                                    RealPlayByDataTypeDemo demo = new RealPlayByDataTypeDemo();

                                    demo.addItem(new CaseMenu.Item(demo, "mp4", "attachFlv"));
                                    demo.addItem(new CaseMenu.Item(demo, "stop", "detach"));

                                    /**
                                     * SDK初始化
                                     * @param disConnect 断线回调函数
                                     * @param haveReConnect 重连回调函数
                                     * */
                                    DisConnectCallBack disConnect = new DisConnectCallBack();
                                    HaveReConnectCallBack haveReConnect = new HaveReConnectCallBack();
//                                    demo.init(disConnect, haveReConnect);
                                    demo.init();

                                    System.out.println(info.getM_strIp());
                                    System.out.println(info.getM_nPort());

//                                    if (demo.login(info.getM_strIp(), info.getM_nPort(), info.getM_strUser(), info.getM_strPassword(),2,null,info.getM_srtId())) {
                                    if (demo.login(info.getM_strIp(), info.getM_nPort(), info.getM_strUser(), info.getM_strPassword())) {
                                        System.out.println("登录成功");
                                        // 登录成功后
//                                        demo.run(0,info.getM_srtId());
                                        demo.run();
                                        try {
                                            /**
                                             * 视频录制60s
                                             * */
                                            Thread.sleep(60 * 1000);
//                                            demo.run(1,null);
                                            demo.run();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    // 登出设备
                                    demo.logout();
                                    // 测试结束,释放sdk资源
//                          demo.clean();
                                    System.out.println("录制结束");
                                }).start();
                            } else {
                                System.out.println("设备id不一致登录失败！");
                            }
                        }
                    });
                }
                break;
            }
            default:
                break;
        }
        return lCommand;
    }

    @Override
    //向前端进行摄像机触发成功的消息提醒
    public void SendMessage(PreInfo info) {
        preInfoSet.add(info);
        System.out.println("成功触发！");
    }

}

