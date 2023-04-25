package com.ry.demo.frame;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * 在摄像机工作过程中进行工作状态的监听，将设备数据进行回传
 * 实时监视数据回调函数--扩展(pBuffer内存由SDK内部申请释放)
 */

public class CbfRealDataCallBackEx implements NetSDKLib.fRealDataCallBackEx {
    private static class CallBackHolder {
        private static CbfRealDataCallBackEx instance = new CbfRealDataCallBackEx();
    }

    public static CbfRealDataCallBackEx getInstance() {
        return CallBackHolder.instance;
    }

    @Override
    public void invoke(NetSDKLib.LLong lRealHandle, int dwDataType, Pointer pBuffer, int dwBufSize, int param, Pointer dwUser) {
        if (0 != lRealHandle.longValue()) {
            switch (dwDataType) {
                case 0:

                    break;
                case 1:
                    //标准视频数据
                    System.out.println("[1]"+"码流大小为" + dwBufSize + "\n" + "码流类型为原始音视频混合数据");
                    break;
                case 2:
                    //yuv 数据
                    System.out.println("[2]"+"码流大小为" + dwBufSize + "\n" + "码流类型为原始音视频混合数据");
                    break;
                case 3:
                    //pcm 音频数据
                    System.out.println("[3]"+"码流大小为" + dwBufSize + "\n" + "码流类型为原始音视频混合数据");
                    break;
                case 4:
                    //原始音频数据
                    System.out.println("[4]"+"码流大小为" + dwBufSize + "\n" + "码流类型为原始音视频混合数据");
                    break;
                default:
                    break;
            }
        }
    }
}

