package com.ry.conversion;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * @author 47081
 * @version 1.0
 * @description 数据回调函数
 * @date 2021/3/9
 */
public class RealPlayByDataTypeCallback implements NetSDKLib.fRealDataCallBackEx2 {
  private static volatile RealPlayByDataTypeCallback instance;

  private RealPlayByDataTypeCallback() {}

  public static RealPlayByDataTypeCallback getInstance() {
    if (instance == null) {
      synchronized (RealPlayByDataTypeCallback.class) {
        if (instance == null) {
          instance = new RealPlayByDataTypeCallback();
        }
      }
    }
    return instance;
  }

  @Override
  public void invoke(
      NetSDKLib.LLong lRealHandle,
      int dwDataType,
      Pointer pBuffer,
      int dwBufSize,
      NetSDKLib.LLong param,
      Pointer dwUser) {
    // 依据dwDataType进行码流类型判断
    // 私有流或mp4,出来的码流均为私有流
    if (dwDataType == 0 || dwDataType == 1003) {
      // 私有流
    } else {
      // 转码码流
      int type = dwDataType - 1000;
      /**
       * EM_REAL_DATA_TYPE_PRIVATE(0, "私有码流"), EM_REAL_DATA_TYPE_GBPS(1, "国标PS码流"),
       * EM_REAL_DATA_TYPE_TS(2, "TS码流"), EM_REAL_DATA_TYPE_MP4(3, "MP4文件"),
       * EM_REAL_DATA_TYPE_H264(4, "裸H264码流"), EM_REAL_DATA_TYPE_FLV_STREAM(5, "流式FLV");
       */
    }
    System.out.println("dwDataType: " + dwDataType);
  }
}
