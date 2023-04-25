package com.ry.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.callback.impl.DefaultDisconnectCallback;
import com.netsdk.lib.callback.impl.DefaultHaveReconnectCallBack;
import com.netsdk.lib.structure.NET_IN_QUERY_COURSEMEDIA_FILECLOSE;
import com.netsdk.lib.structure.NET_OUT_QUERY_COURSEMEDIA_FILECLOSE;
import com.netsdk.module.BaseModule;
import com.netsdk.module.entity.DeviceInfo;
import com.ry.common.CaptureReceiveCB;
import com.ry.demo.frame.AnalyzerDataCallBack;
import com.ry.demo.frame.DownloadRecordCallBack;
import com.ry.demo.module.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/camera")
public class DaHuaCameraController {

    Logger logger = LoggerFactory.getLogger(DaHuaController.class);

    //录像回放
    @GetMapping("/playBackByTime")
    public String playBackByTime() throws JsonProcessingException {
        HashMap<String,Object> hs = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginModule.init(null, null);
            LoginModule.login("sunkoda.us.to", 37771, "admin", "admin123");

            NetSDKLib.NET_TIME stTimeStart = new NetSDKLib.NET_TIME();
            NetSDKLib.NET_TIME stTimeEnd = new NetSDKLib.NET_TIME();
            stTimeStart.setTime(2023,04,12,14,30,00);
            stTimeEnd.setTime(2023,04,12,15,00,00);

            PlayBackModule pm = new PlayBackModule();
            pm.playBack(1, 1, stTimeStart, stTimeEnd
                    , "/Users/yangwenqiang/Doc/SunKoDa/snapPictureTemp/test.mp4", 3);

        }catch (Exception e){
            hs.put("msg","下载录像失败！");
            hs.put("errorMessage", e.getMessage());
            logger.error(e.getMessage(), e);
        }finally {
//            LoginModule.logout();
//            LoginModule.cleanup();
        }
        return objectMapper.writeValueAsString(hs);
    }

    //录像下载
    @GetMapping("/downloadRecord")
    public String downloadRecord() throws JsonProcessingException {
        HashMap<String,Object> hs = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginModule.init(null, null);
            LoginModule.login("sunkoda.us.to", 37771, "admin", "admin123");

            NetSDKLib.NET_TIME stTimeStart = new NetSDKLib.NET_TIME();
            NetSDKLib.NET_TIME stTimeEnd = new NetSDKLib.NET_TIME();
            stTimeStart.setTime(2023,04,12,14,30,0);
            stTimeEnd.setTime(2023,04,12,15,0,0);

            DownLoadRecordModule dr = new DownLoadRecordModule();
            dr.downloadRecordFileConverted(1, 1, stTimeStart, stTimeEnd
                    , "/Users/yangwenqiang/Doc/SunKoDa/snapPictureTemp/test.mp4", 3);
        }catch (Exception e){
            hs.put("msg","下载录像失败！");
            hs.put("errorMessage", e.getMessage());
            logger.error(e.getMessage(), e);
        }finally {
//            LoginModule.logout();
//            LoginModule.cleanup();
        }
        return objectMapper.writeValueAsString(hs);
    }

    //事件照片回传
    @GetMapping("/getTrafficEventPic")
    public String getTrafficEventPic() throws JsonProcessingException {
        HashMap<String,Object> hs = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginModule.init(null, null);
            LoginModule.login("sunkoda.us.to", 37772, "admin", "admin123");

            NetSDKLib.MANUAL_SNAP_PARAMETER snapParam = new NetSDKLib.MANUAL_SNAP_PARAMETER();
            snapParam.nChannel = 0;
            boolean bRet = LoginModule.netsdk.CLIENT_ControlDevice(LoginModule.m_hLoginHandle,
                    NetSDKLib.CtrlType.CTRLTYPE_MANUAL_SNAP, snapParam.getPointer(), 1000);

            boolean listenFlag = TrafficEventModule.attachIVSEvent(0, AnalyzerDataCallBack.getInstance());
            //此处为持久化订阅，结束订阅需要调用 TrafficEventModule.detachIVSEvent()

            if (listenFlag){
                hs.put("msg","订阅成功！");
            }else {
                hs.put("msg","订阅失败！");
            }
        }catch (Exception e){
            hs.put("errorMessage", e.getMessage());
            logger.error(e.getMessage(), e);
        }finally {
            //最后记得释放资源
//            LoginModule.logout();
//            LoginModule.cleanup();
        }
        return objectMapper.writeValueAsString(hs);
    }

//    @GetMapping("/snapPicture")
//    public String snapPicture() throws JsonProcessingException {
//        HashMap<String,Object> hs = new HashMap<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String fileName = "test";
//            LoginModule.init(null,null);
////            LoginModule.login("192.168.10.243", 37777, "admin", "admin123");
////            CaptureReceiveCB captureReceiveCB = new CaptureReceiveCB("192.168.10.243", fileName);
//            LoginModule.login("sunkoda.us.to", 37772, "admin", "admin123");
//            CaptureReceiveCB captureReceiveCB = new CaptureReceiveCB("sunkoda.us.to", fileName);
//            CapturePictureModule.setSnapRevCallBack(captureReceiveCB);
//            CapturePictureModule.localCapturePicture(LoginModule.m_hLoginHandle, fileName);
//            hs.put("msg","抓图成功！");
//            hs.put("filepath", fileName);
//        }catch (Exception e){
//            hs.put("msg","抓图失败！");
//            hs.put("errorMessage", e.getMessage());
//            logger.error(e.getMessage(), e);
//        }finally {
////            if(key != null) {
////                Cache.removeSemaphore(key);
////            }
//            if(LoginModule.m_hLoginHandle.longValue() != 0) {
//                NetSDKLib.NETSDK_INSTANCE.CLIENT_Logout(LoginModule.m_hLoginHandle);
//            }
//            NetSDKLib.NETSDK_INSTANCE.CLIENT_Cleanup();
//        }
//        return objectMapper.writeValueAsString(hs);
//    }

//    @GetMapping("/loginTest")
//    public String loginTest() throws JsonProcessingException {
//        HashMap<String,Object> hs = new HashMap<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);
//        try {
//            BaseModule baseModule = new BaseModule();
//            baseModule.init(DefaultDisconnectCallback.getINSTANCE(), DefaultHaveReconnectCallBack.getINSTANCE(), true);
////            DeviceInfo info = baseModule.login("192.168.10.243", 37777, "admin", "admin123");
//            DeviceInfo info = baseModule.login("sunkoda.us.to", 37772, "admin", "admin123");
//
//            m_hLoginHandle.setValue(info.getLoginHandler());
////            String fileName = "test";
////            CaptureReceiveCB captureReceiveCB = new CaptureReceiveCB("sunkoda.us.to", fileName);
////            CapturePictureModule.setSnapRevCallBack(captureReceiveCB);
////            CapturePictureModule.localCapturePicture(m_hLoginHandle, fileName);
//            if (info.getLoginHandler() != 0L) {
//                hs.put("msg","登录成功！");
//            }else {
//                hs.put("msg","登录失败！");
//            }
//        }catch (Exception e){
//            hs.put("msg","登录失败！");
//            hs.put("errorMessage", e.getMessage());
//            logger.error(e.getMessage(), e);
//        }finally {
//            if(m_hLoginHandle.longValue() != 0) {
//                NetSDKLib.NETSDK_INSTANCE.CLIENT_Logout(m_hLoginHandle);
//            }
//            NetSDKLib.NETSDK_INSTANCE.CLIENT_Cleanup();
//        }
//        return objectMapper.writeValueAsString(hs);
//    }


}

