package com.ry.demo.frame;

import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class DownloadRecordCallBack implements NetSDKLib.fTimeDownLoadPosCallBack{



    private static class CallBackHolder {
        private static DownloadRecordCallBack instance = new DownloadRecordCallBack();
    }

    public static DownloadRecordCallBack getInstance() {
        return DownloadRecordCallBack.CallBackHolder.instance;
    }

    private BufferedImage gateBufferedImage = null;

    @Override
    public void invoke(NetSDKLib.LLong IPlayHandle, int dwTotalSize, int dwDownLoadSize, int index
            , NetSDKLib.NET_RECORDFILE_INFO.ByValue recordFileInfo, Pointer dwUser) {
//        if (IPlayHandle.longValue() == 0) return;

    }
}
