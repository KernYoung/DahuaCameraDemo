package com.ry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.netsdk.module.entity.DeviceInfo;
import com.ry.demo.module.CapturePictureModule;
import com.ry.demo.module.LoginModule;
import com.ry.service.IDaHuaCameraService;
import com.ry.service.impl.DaHuaCameraServiceImpl;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.ry.common.Cache;
import com.ry.common.CaptureReceiveCB;
import com.ry.common.DisConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@RestController
public class DHController {

    Logger logger = LoggerFactory.getLogger(DHController.class);

    @Autowired
    private IDaHuaCameraService daHuaCameraService;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/method/snapCapture1", method = RequestMethod.POST)
    @ResponseBody
    public String snapCapture(@RequestBody Map<String, String> params) throws JsonProcessingException {
//        JsonResponse jsonResponse;
        HashMap<String,Object> hs = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String key = null;
//        NetSDKLib.LLong loginHandle = new NetSDKLib.LLong(0);
        try {
            String ip = params.get("ip");
            String port = params.get("port");
            String username = params.get("username");
            String password = params.get("password");
            String channel = params.get("channel");
            String fileName = params.get("fileName");
            boolean init = this.initClient();
            if (!init) {
                throw new Exception("客户端初始化失败");
            }
            CaptureReceiveCB captureReceiveCB = new CaptureReceiveCB(ip, fileName);
            boolean loginWithHighSecurity = daHuaCameraService.loginWithHighSecurity("192.168.10.243", 37777, "admin", "admin123");
//            loginHandle = this.login(ip, Integer.parseInt(port), username, password, captureReceiveCB);
            if(LoginModule.m_hLoginHandle.longValue() == 0) {
                throw new Exception("登录失败，用户名或密码不正确");
            }

            //            String filepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/static/" + fileName;

            CapturePictureModule.setSnapRevCallBack(captureReceiveCB);
            CapturePictureModule.localCapturePicture(LoginModule.m_hLoginHandle, fileName);

//            boolean resultCapture = this.capture1(loginHandle, Integer.parseInt(channel), captureReceiveCB);
//            if(!resultCapture) {
//                throw new Exception("抓拍图片失败");
//            }
//            key = ip + loginHandle.longValue();
//            Semaphore semaphore = Cache.getSemaphore(key);
//            semaphore.tryAcquire(1, 5, TimeUnit.SECONDS);


//            jsonResponse = JsonResponse.success("抓拍图片成功", filepath);
            hs.put("msg","抓图成功！");
            hs.put("filepath", fileName);
        } catch (Exception e) {
//            jsonResponse = JsonResponse.error("抓拍图片失败:" + e.getMessage());
            hs.put("msg","抓图失败！");
            hs.put("errorMessage", e.getMessage());
            logger.error(e.getMessage(), e);
        }finally {
            if(key != null) {
                Cache.removeSemaphore(key);
            }
            if(LoginModule.m_hLoginHandle.longValue() != 0) {
                NetSDKLib.NETSDK_INSTANCE.CLIENT_Logout(LoginModule.m_hLoginHandle);
            }
            NetSDKLib.NETSDK_INSTANCE.CLIENT_Cleanup();
        }
        return objectMapper.writeValueAsString(hs);
    }

//    private static boolean localCapturePicture(NetSDKLib.LLong hPlayHandle, String picFileName) {
//
//        if (!LoginModule.netsdk.CLIENT_CapturePictureEx(hPlayHandle, picFileName, NetSDKLib.NET_CAPTURE_FORMATS.NET_CAPTURE_JPEG)) {
//            System.err.printf("CLIENT_CapturePicture Failed!" + ToolKits.getErrorCode());
//            return false;
//        } else {
//            System.out.println("CLIENT_CapturePicture success");
//        }
//        return true;
//    }

    private boolean initClient() {
        DisConnect disConnect = new DisConnect();
        return NetSDKLib.NETSDK_INSTANCE.CLIENT_Init(disConnect, null);
    }

//    private NetSDKLib.LLong login(String ip, int port, String user, String password, CaptureReceiveCB captureReceiveCB) throws Exception {
//        Native.setCallbackThreadInitializer(captureReceiveCB,
//                new CallbackThreadInitializer(false, false, "snapPicture callback thread"));
//        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
//        pstInParam.nPort = port;
//        pstInParam.szIP = ip.getBytes();
//        pstInParam.szPassword = password.getBytes();
//        pstInParam.szUserName = user.getBytes();
//        //出参
//        NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
//        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
//        pstOutParam.stuDeviceInfo = m_stDeviceInfo;
//        return NetSDKLib.NETSDK_INSTANCE.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
//    }

    private boolean capture(NetSDKLib.LLong loginHandle, int channel, CaptureReceiveCB captureReceiveCB) throws Exception {
        NetSDKLib.NETSDK_INSTANCE.CLIENT_SetSnapRevCallBack(captureReceiveCB, null);
        // send caputre picture command to device
        NetSDKLib.SNAP_PARAMS stuSnapParams = new NetSDKLib.SNAP_PARAMS();
        stuSnapParams.Channel = channel;  			// channel
        stuSnapParams.mode = 0;    			// capture picture mode
        stuSnapParams.Quality = 3;				// picture quality
        stuSnapParams.InterSnap = 0; 	// timer capture picture time interval
        stuSnapParams.CmdSerial = 0;  			// request serial
        IntByReference reserved = new IntByReference(0);
        return NetSDKLib.NETSDK_INSTANCE.CLIENT_SnapPictureEx(loginHandle, stuSnapParams, reserved);
    }

    private boolean capture1(NetSDKLib.LLong loginHandle, int channel, CaptureReceiveCB captureReceiveCB) throws Exception {
        NetSDKLib.NETSDK_INSTANCE.CLIENT_SetSnapRevCallBack(captureReceiveCB, null);
        // send caputre picture command to device
        NetSDKLib.SNAP_PARAMS stuSnapParams = new NetSDKLib.SNAP_PARAMS();
        stuSnapParams.Channel = channel;  			// channel
        stuSnapParams.mode = 0;    			// capture picture mode
        stuSnapParams.Quality = 3;				// picture quality
        stuSnapParams.InterSnap = 0; 	// timer capture picture time interval
        stuSnapParams.CmdSerial = 0;  			// request serial
//        IntByReference reserved = new IntByReference(0);
//        return NetSDKLib.NETSDK_INSTANCE.CLIENT_SnapPictureEx(loginHandle, stuSnapParams, reserved);

        IntByReference reserved = new IntByReference(0);
        if (!NetSDKLib.NETSDK_INSTANCE.CLIENT_SnapPictureEx(loginHandle, stuSnapParams, reserved)) {
            System.err.printf("CLIENT_SnapPictureEx Failed!" + ToolKits.getErrorCode());
            return false;
        } else {
            System.out.println("CLIENT_SnapPictureEx success");
        }
        return true;
    }

    private boolean capture2(NetSDKLib.LLong loginHandle, String filePath, CaptureReceiveCB captureReceiveCB) throws Exception {
        CapturePictureModule.setSnapRevCallBack(captureReceiveCB);
        CapturePictureModule.localCapturePicture(loginHandle, filePath);
        return true;
    }
}
