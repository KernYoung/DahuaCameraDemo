package com.ry.demo.frame;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class AnalyzerDataCallBack implements NetSDKLib.fAnalyzerDataCallBack{

    private static class CallBackHolder {
        private static AnalyzerDataCallBack instance = new AnalyzerDataCallBack();
    }

    public static AnalyzerDataCallBack getInstance() {
        return AnalyzerDataCallBack.CallBackHolder.instance;
    }

    @Override
    public int invoke(NetSDKLib.LLong lAnalyzerHandle, int dwAlarmType, Pointer pAlarmInfo,
                      Pointer pBuffer, int dwBufSize, Pointer dwUser, int nSequence,
                      Pointer reserved) {
        if (lAnalyzerHandle.longValue() == 0 || pAlarmInfo == null) return -1;

        switch (dwAlarmType) {
            case NetSDKLib.EVENT_IVS_TRAFFICJUNCTION: // 交通路口事件----老规则(对应 DEV_EVENT_TRAFFICJUNCTION_INFO)
            {
                // 保存图片
                SavePlatePic(pBuffer, dwBufSize);
                break;
            }
            default:
                System.out.println("其他事件：" + dwAlarmType);
                break;
        }
        return 0;
    }

    private void SavePlatePic(Pointer pBuffer, int dwBufSize) {
        BufferedImage gateBufferedImage;
        File path = new File("/Users/yangwenqiang/Doc/SunKoDa/snapPictureTemp");
        if (!path.exists()){
            path.mkdir();
        }
        //保存图片，获取图片缓存
        String snapPicPath = path + "/" + System.currentTimeMillis() + "TrafficSnapPicture.jpg";

        byte[] buffer = pBuffer.getByteArray(0, dwBufSize);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);

        try {
            gateBufferedImage = ImageIO.read(byteArrayInputStream);
            if (gateBufferedImage != null){
                ImageIO.write(gateBufferedImage, "jpg", new File(snapPicPath));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
