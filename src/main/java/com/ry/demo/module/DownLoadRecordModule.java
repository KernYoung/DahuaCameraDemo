package com.ry.demo.module;

import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.NetSDKLib.LLong;
import com.netsdk.lib.NetSDKLib.NET_IN_DOWNLOAD_BY_DATA_TYPE;
import com.netsdk.lib.NetSDKLib.NET_OUT_DOWNLOAD_BY_DATA_TYPE;
import com.netsdk.lib.ToolKits;
import com.netsdk.lib.enumeration.EM_COURSE_LOCK_TYPE;
import com.netsdk.lib.enumeration.EM_COURSE_RECORD_COMPRESSION_TYPE;
import com.netsdk.lib.enumeration.EM_COURSE_RECORD_TYPE;
import com.netsdk.lib.structure.*;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import static com.netsdk.lib.Utils.getOsPrefix;

/**
 * 下载录像接口实现
 * 主要有 ： 下载录像、设置码流类型功能
 */
public class DownLoadRecordModule {
	// 下载句柄
	public static LLong m_hDownLoadHandle = new LLong(0);

//	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//	// 查找录像文件
//	public static boolean queryRecordFile(int nChannelId,
//									   NetSDKLib.NET_TIME stTimeStart,
//									   NetSDKLib.NET_TIME stTimeEnd,
//									   NetSDKLib.NET_RECORDFILE_INFO[] stFileInfo,
//									   IntByReference nFindCount) {
//		// RecordFileType 录像类型 0:所有录像  1:外部报警  2:动态监测报警  3:所有报警  4:卡号查询   5:组合条件查询
//		// 6:录像位置与偏移量长度   8:按卡号查询图片(目前仅HB-U和NVS特殊型号的设备支持)  9:查询图片(目前仅HB-U和NVS特殊型号的设备支持)
//		// 10:按字段查询    15:返回网络数据结构(金桥网吧)  16:查询所有透明串数据录像文件
//		int nRecordFileType = 0;
//		boolean bRet = LoginModule.netsdk.CLIENT_QueryRecordFile(LoginModule.m_hLoginHandle, nChannelId,
//										nRecordFileType, stTimeStart, stTimeEnd, null, stFileInfo,
//										stFileInfo.length * stFileInfo[0].size(), nFindCount, 5000, false);
//
//		if(bRet) {
//			System.out.println("QueryRecordFile  Succeed! \n" + "查询到的视频个数：" + nFindCount.getValue());
//		} else {
//			System.err.println("QueryRecordFile  Failed!" + ToolKits.getErrorCode());
//			return false;
//		}
//		return true;
//	}

//	/**
//	 *  设置回放时的码流类型
//	 * @param m_streamType 码流类型
//	 */
//	public static void setStreamType(int m_streamType) {
//
//        IntByReference steamType = new IntByReference(m_streamType);// 0-主辅码流,1-主码流,2-辅码流
//        int emType = NetSDKLib.EM_USEDEV_MODE.NET_RECORD_STREAM_TYPE;
//
//        boolean bret = LoginModule.netsdk.CLIENT_SetDeviceMode(LoginModule.m_hLoginHandle, emType, steamType.getPointer());
//        if (!bret) {
//        	System.err.println("Set Stream Type Failed, Get last error." + ToolKits.getErrorCode());
//        } else {
//        	System.out.println("Set Stream Type  Succeed!");
//        }
//	}

//	public static LLong downloadRecordFileByType(int nChannelId,
//										     int nRecordFileType,
//										     NetSDKLib.NET_TIME stTimeStart,
//										     NetSDKLib.NET_TIME stTimeEnd,
//										     String SavedFileName,
//										     NetSDKLib.fTimeDownLoadPosCallBack cbTimeDownLoadPos) {
//
//		NET_IN_DOWNLOAD_BY_DATA_TYPE pstInParam = new NET_IN_DOWNLOAD_BY_DATA_TYPE();
//		pstInParam.nChannelID = nChannelId;
//		pstInParam.emRecordType = nRecordFileType;
//		pstInParam.stStartTime = stTimeStart;
//		pstInParam.stStopTime = stTimeEnd;
//		pstInParam.szSavedFileName = SavedFileName;
//		pstInParam.emDataType = 3;
//		pstInParam.write();
//
//		NET_OUT_DOWNLOAD_BY_DATA_TYPE pstOutParam = new NET_OUT_DOWNLOAD_BY_DATA_TYPE();
//		pstOutParam.write();
//
//		// 指定码流类型 开始下载, 下载得到的文件和数据回调函数 fDownLoadDataCallBack 中得到的码流类型均为 emDataType 所指定的类型
//		m_hDownLoadHandle = LoginModule.netsdk.CLIENT_DownloadByDataType(LoginModule.m_hLoginHandle
//				, pstInParam.getPointer(), pstOutParam.getPointer(), 50000);
//
//		if(m_hDownLoadHandle.longValue() != 0) {
//			System.out.println("Downloading RecordFile!");
//		} else {
//			System.err.println("Download RecordFile Failed!" + ToolKits.getErrorCode());
//		}
//		return m_hDownLoadHandle;
//	}

//	public static LLong downloadRecordFile(int nChannelId,
//										   int nRecordFileType,
//										   NetSDKLib.NET_TIME stTimeStart,
//										   NetSDKLib.NET_TIME stTimeEnd,
//										   String SavedFileName,
//										   NetSDKLib.fTimeDownLoadPosCallBack cbTimeDownLoadPos) {
//
//		m_hDownLoadHandle = LoginModule.netsdk.CLIENT_DownloadByTimeEx(LoginModule.m_hLoginHandle, nChannelId, nRecordFileType,
//															stTimeStart, stTimeEnd, SavedFileName,
//															cbTimeDownLoadPos, null, null, null, null);
//		if(m_hDownLoadHandle.longValue() != 0) {
//			System.out.println("Downloading RecordFile!");
//		} else {
//			System.err.println("Download RecordFile Failed!" + ToolKits.getErrorCode());
//		}
//		return m_hDownLoadHandle;
//	}

	public static void stopDownLoadRecordFile(LLong m_hDownLoadHandle) {
		if (m_hDownLoadHandle.longValue() == 0) {
			return;
		}
		LoginModule.netsdk.CLIENT_StopDownload(m_hDownLoadHandle);
	}

//	/**
//	 * 下载录像 转码流 通用接口 可以从回调获取转码流数据
//	 */
//	public void downloadRecordFileWithConvertedDataType() {
//
//		int nStreamType = 0;         // 0-主辅码流,1-主码流,2-辅码流
//		int nChannel = 0;            // 通道号
//		int nType = 0;               // 文件类型
//		NetSDKLib.NET_TIME stTimeStart = new NetSDKLib.NET_TIME();    // 开始时间
//		NetSDKLib.NET_TIME stTimeEnd = new NetSDKLib.NET_TIME();      // 结束时间
//
//		Calendar calendar = Calendar.getInstance();
//
//		@SuppressWarnings("resource")
//		Scanner scanner = new Scanner(System.in);
//		try {
//			// 请选择要下载的码流 0-主辅码流,1-主码流,2-辅码流
//			nStreamType = 1;   // 默认主码流
//
//			System.out.println("请输入真实通道号(注意sdk从0开始计数): ");
//			nChannel = scanner.nextInt();
//
//			//        0 私有码流
//			//        1 国标PS码流
//			//        2 TS码流
//			//        3 MP4文件(从回调函数出来的是私有码流数据,参数dwDataType值为0)
//			//        4 裸H264码流
//			//        5 流式FLV
//			// 请输入保存的文件类型 码流转换类型 0 私有码流; 1 国标PS码流; 2 TS码流; 3 MP4文件; 4 裸H264码流; 5 流式FLV");
//			nType = 3;     // 转成 FLV
//
//			System.out.println("请输入录像开始时间(格式:yyyy-MM-dd HH:mm:ss): ");
//			String startTime = scanner.next().trim() + " " + scanner.next().trim();
//			calendar.setTime(format.parse(startTime));
//			setTime(calendar, stTimeStart);
//
//			System.out.println("请输入录像结束时间(格式:yyyy-MM-dd HH:mm:ss): ");
//			String endTime = scanner.next().trim() + " " + scanner.next().trim();
//			calendar.setTime(format.parse(endTime));
//			setTime(calendar, stTimeEnd);
//		} catch (ParseException e) {
//			System.err.println("时间输入非法");
//			return;
//		}
//
//		File dir = new File("RecordFiles");
//		if (!dir.exists()) {// 判断目录是否存在
//			dir.mkdir();
//		}
//
//		String savedFileName = "RecordFiles/RecordCovertTest" + System.currentTimeMillis() + ".mp4"; // 保存录像文件名
//		this.downloadRecordFileConverted(nStreamType, nChannel, stTimeStart, stTimeEnd, savedFileName, nType);
//	}

	/**
	 * 下载录像，通用接口(把原始码流转换成其他封装格式的码流)
	 */
	public void downloadRecordFileConverted(int nStreamType, int nChannel,
											NetSDKLib.NET_TIME stTimeStart, NetSDKLib.NET_TIME stTimeEnd,
											String savedFileName, int nType) {

		if (!setStreamType(nStreamType)) {
			System.err.println("Set Stream Type Failed!." + ToolKits.getErrorCode());
			return;
		}

		NetSDKLib.NET_IN_DOWNLOAD_BY_DATA_TYPE stIn = new NetSDKLib.NET_IN_DOWNLOAD_BY_DATA_TYPE();

		stIn.emDataType = nType;            // 封装类型
		stIn.emRecordType = NetSDKLib.EM_QUERY_RECORD_TYPE.EM_RECORD_TYPE_ALL; // 所有录像
		stIn.nChannelID = nChannel;
		stIn.stStartTime = stTimeStart;     // 开始时间
		stIn.stStopTime = stTimeEnd;        // 结束时间
		stIn.cbDownLoadPos = DownLoadRecordModule.DownloadPosCallback.getInstance();           // 下载监控回调函数
		stIn.dwPosUser = null;
		stIn.fDownLoadDataCallBack = DownLoadRecordModule.DownLoadDataCallBack.getInstance();  // 下载数据回调函数
		stIn.dwDataUser = null;
		stIn.szSavedFileName = savedFileName;

		NetSDKLib.NET_OUT_DOWNLOAD_BY_DATA_TYPE stOut = new NetSDKLib.NET_OUT_DOWNLOAD_BY_DATA_TYPE();
		stIn.write();
		stOut.write();
		m_hDownLoadHandle = LoginModule.netsdk.CLIENT_DownloadByDataType(LoginModule.m_hLoginHandle, stIn.getPointer(), stOut.getPointer(), 5000);
		if (m_hDownLoadHandle.longValue() != 0) {
			System.out.println("DownloadByDataType Succeed!");
		} else {
			System.err.println("DownloadByDataType Failed! " + ToolKits.getErrorCode());
		}
	}

//	private void setTime(Calendar calendar, NetSDKLib.NET_TIME stuTime) {
//		stuTime.setTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
//				calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//	}

	/**
	 * 设置回放时的码流类型: 主码流/辅码流
	 *
	 * @param m_streamType 码流类型
	 */
	public static boolean setStreamType(int m_streamType) {
		int emType = NetSDKLib.EM_USEDEV_MODE.NET_RECORD_STREAM_TYPE; // 回放录像枚举
		IntByReference steamType = new IntByReference(m_streamType);  // 0-主辅码流,1-主码流,2-辅码流
		return LoginModule.netsdk.CLIENT_SetDeviceMode(LoginModule.m_hLoginHandle, emType, steamType.getPointer());
	}

	/**
     * 下载进度回调函数
     * 回调写成单例模式, 如果回调里需要处理数据，请另开线程
     */
    public static class DownloadPosCallback implements NetSDKLib.fDownLoadPosCallBack {

        private DownloadPosCallback() {
        }

        private static class CallBackHolder {
            private static final DownloadPosCallback callback = new DownloadPosCallback();
        }

        public static DownloadPosCallback getInstance() {
            return CallBackHolder.callback;
        }

        @Override
        public void invoke(LLong lPlayHandle, int dwTotalSize, int dwDownLoadSize, Pointer dwUser) {

            System.out.println(String.format("dwDownLoadSize: %d || dwTotalSize: %d ", dwDownLoadSize, dwTotalSize));
            if (dwDownLoadSize == -1) {         // 下载结束
                System.out.println("Downloading Complete. ");
                new StopDownloadTask(lPlayHandle).start();   // 注意这里需要另起线程
            }
        }

        private static class StopDownloadTask extends Thread {
            private final LLong lDownloadHandle;

            public StopDownloadTask(LLong lDownloadHandle) {
                this.lDownloadHandle = lDownloadHandle;
            }

            public void run() {
                stopDownLoadRecordFile(lDownloadHandle);
            }
        }
    }

	public int invoke(LLong lRealHandle, int dwDataType, Pointer pBuffer, int dwBufSize, Pointer dwUser) {

		// byte[] data = pBuffer.getByteArray(0, dwBufSize);   // 这是二进制码流数据, 如果有其他用途可以从这里取出来

		// 不同的封装类型，回调里返回的 dwDataType 是不同的，它们遵循下面的逻辑
		if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_PRIVATE)) {
			System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
		} else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_GBPS)) {
			System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
		} else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_TS)) {
			System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
		} else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_MP4)) {
			System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
		} else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_H264)) {
			System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
		} else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_FLV_STREAM)) {
			System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
		}
		return 0;
	}

	/**
     * 下载数据回调，这里可以拿到原始的二进制码流数据
     * 回调写成单例模式, 如果回调里需要处理数据，请另开线程
     */
    public static class DownLoadDataCallBack implements NetSDKLib.fDataCallBack {

        private DownLoadDataCallBack() {
        }

        private static class DownloadDataCallBackHolder {
            private static final DownLoadDataCallBack dataCB = new DownLoadDataCallBack();
        }

        public static DownLoadDataCallBack getInstance() {
            return DownloadDataCallBackHolder.dataCB;
        }

        public int invoke(LLong lRealHandle, int dwDataType, Pointer pBuffer, int dwBufSize, Pointer dwUser) {

            // byte[] data = pBuffer.getByteArray(0, dwBufSize);   // 这是二进制码流数据, 如果有其他用途可以从这里取出来

            // 不同的封装类型，回调里返回的 dwDataType 是不同的，它们遵循下面的逻辑
            if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_PRIVATE)) {
                System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
            } else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_GBPS)) {
                System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
            } else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_TS)) {
                System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
            } else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_MP4)) {
                System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
            } else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_H264)) {
                System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
            } else if (dwDataType == (NetSDKLib.NET_DATA_CALL_BACK_VALUE + NetSDKLib.EM_REAL_DATA_TYPE.EM_REAL_DATA_TYPE_FLV_STREAM)) {
                System.out.println("DownLoad DataCallBack [ " + dwDataType + " ]");
            }
            return 0;
        }
    }

//    //录像查询===============================================================
//	public static String encode;
//
//    static {
//        String osPrefix = getOsPrefix();
//        if (osPrefix.toLowerCase().startsWith("win32-amd64")) {
//            encode = "GBK";
//        } else if (osPrefix.toLowerCase().startsWith("linux-amd64")
//        || osPrefix.toLowerCase().startsWith("mac-64")) {
//            encode = "UTF-8";
//        }
//    }
//	public int FindID = 0;     // 查询句柄
//    public int total = 0;      // 总数
//
//    // 这个结构体很大，new的特别慢，所以我写成静态
//    private NET_OUT_QUERY_COURSEMEDIA_FILE stuQueryOut = new NET_OUT_QUERY_COURSEMEDIA_FILE();
//
//    /**
//     * 这里的录像查询条件为：
//     * 1) 不区分是否锁定，查询全部
//     * 2）不区分导播/互动，查询全部
//     * 3）模糊查询关键字 "" 即全部
//     * 4）时间 2020/10/10 0:0:0 - 2020/10/20 23:59:59
//     */
//    public void OpenQueryCourseMediaFileTest() {
//
////        if (FindID != 0) {
////            CloseQueryCourseMediaFileTest();     // 如果上一次查询没有关闭，那先关闭它
////            return;
////        }
//
//        NET_IN_QUERY_COURSEMEDIA_FILEOPEN stuIn = new NET_IN_QUERY_COURSEMEDIA_FILEOPEN();
//        stuIn.emCourseLockType = EM_COURSE_LOCK_TYPE.EM_COURSE_LOCK_TYPE_ALL.getValue();            // 所有类型，不区分是否锁定
//        stuIn.emCourseRecordType = EM_COURSE_RECORD_TYPE.EM_COURSE_RECORD_TYPE_ALL.getValue();      // 所有类型，不区分是导播还是互动
//        byte[] keyWords = new byte[0];
//        try {
//            keyWords = "".getBytes(encode);      // 模糊关键字
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.arraycopy(keyWords, 0, stuIn.szKeyWord, 0, keyWords.length);
//
//        // 起止时间
//        stuIn.stuStartTime = new NET_TIME(2023,04,12,14,30,00);
//        stuIn.stuEndTime = new NET_TIME(2023,04,12,19,30,00);
//
//        NET_OUT_QUERY_COURSEMEDIA_FILEOPEN stuOut = new NET_OUT_QUERY_COURSEMEDIA_FILEOPEN();
//		boolean ret = LoginModule.netsdk.CLIENT_OpenQueryCourseMediaFile(LoginModule.m_hLoginHandle, stuIn.getPointer()
//				, stuOut.getPointer(), 3000);
////        boolean ret = LoginModule.netsdk.OpenQueryCourseMediaFile(LoginModule.m_hLoginHandle, stuIn, stuOut, 3000);
//
//        if (!ret) {
//            System.err.println("查询记录失败");
//            return;
//        }
//        FindID = stuOut.nfindID;
//        total = stuOut.ntotalNum;
//        System.out.printf("开始查询成功,FindID:%d, 共查询到记录数:%d\n", FindID, total);
//    }
//
//
//    /**
//     * 从设备获取查询数据
//     */
//    public void DoQueryCourseMediaFileTest() {
//
//        if (FindID == 0) {
//            System.err.println("请先开启查询");
//            return;
//        }
//
//        NET_IN_QUERY_COURSEMEDIA_FILE stuIn = new NET_IN_QUERY_COURSEMEDIA_FILE();
//        int maxCount = 10;          // 一次性获取记录的最大数量，这个参数要根据带宽状态和超时时间自行调整
//
//        int offset = 0;
//        for (int i = 0; i < (total / maxCount) + 1; i++) {
//            stuIn.nfindID = FindID;     // 填写查询句柄
//            stuIn.nOffset = offset;     // 查询偏移量
//            stuIn.nCount = maxCount;    // 最大获取个数
//            stuIn.write();
//            stuQueryOut.writeField("dwSize");
//            boolean ret = LoginModule.netsdk.CLIENT_DoQueryCourseMediaFile(LoginModule.m_hLoginHandle, stuIn.getPointer(), stuQueryOut.getPointer(), 3000);
//            if (!ret) {
//                System.err.println("Query Course Media File failed!" + ToolKits.getErrorCode());
//                System.err.println("获取记录失败！");
//                return;
//            }
//            GetPointerDataToCourseMediaInfo(stuQueryOut);
//
//            int retCount = stuQueryOut.nCountResult;     // 实际获取到的数量
//            for (int j = 0; j < retCount; j++) {
//
//                NET_COURSEMEDIA_FILE_INFO fileInfo = stuQueryOut.stuCourseMediaFile[j];
//                int nID = fileInfo.nID;
//                NET_COURSE_INFO courseInfo = fileInfo.stuCourseInfo;
//
//                StringBuilder mediaInfo = new StringBuilder();
//                try {
//                    mediaInfo.append(String.format("\n————————————视频记录[%s]————————————\n", i * maxCount + j + 1))
//                            .append("ID: ").append(nID).append("\n")
//                            .append("CourseInfo->szCourseName: ").append(new String(courseInfo.szCourseName, encode).trim()).append("\n")
//                            .append("CourseInfo->szTeacherName").append(new String(courseInfo.szTeacherName, encode).trim()).append("\n")
//                            .append("CourseInfo->szIntroduction").append(new String(courseInfo.szIntroduction, encode).trim()).append("\n");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                int nChannelNum = fileInfo.nChannelNum;
//                int[] nRecordNum = fileInfo.nRecordNum;
//                mediaInfo.append("///--->共有通道数: ").append(nChannelNum).append("\n");
//
//                for (int k = 0; k < nChannelNum; k++) {
//
//                    int recordNum = nRecordNum[k]; // 通道下录像分段数(1-16)
//                    mediaInfo.append(String.format("//-->第[%2d]个通道, 有[%2d]段录像\n", k + 1, recordNum));
//
//                    for (int m = 0; m < recordNum; m++) {
//                        NET_RECORD_INFO recordInfo = fileInfo.stuRecordInfo_1[k].stuRecordInfo_2[m];
//                        mediaInfo.append(String.format("/->第[%2d]段录像详情:\n", m + 1))
//                                .append("recordInfo->nRealChannel 真实通道: ").append(recordInfo.nRealChannel).append("\n")
//                                .append("recordInfo->stuStartTime 开始时间: ").append(recordInfo.stuStartTime.toStringTimeEx()).append("\n")
//                                .append("recordInfo->stuEndTime 结束时间: ").append(recordInfo.stuEndTime.toStringTimeEx()).append("\n")
//                                .append("recordInfo->nFileLen 文件长度: ").append(combineInt2Long(recordInfo.nFileLen, recordInfo.nFileLenEx)).append("\n")
//                                .append("recordInfo->nTime 录像时常: ").append(recordInfo.nTime).append("\n")
//                                .append("recordInfo->nFileType 文件类型: ").append(recordInfo.nFileType == 0 ? "裁剪文件" : "原始文件").append("\n")
//                                .append("recordInfo->emCompression 课程录像压缩类型").append(EM_COURSE_RECORD_COMPRESSION_TYPE.getEnum(recordInfo.emCompression).getNote()).append("\n");
//                    }
//                }
//                System.out.println(mediaInfo.toString());
//            }
//            offset = offset + stuQueryOut.nCountResult;
//        }
//    }
//
//    // 合并高低int成long
//    private static long combineInt2Long(int low, int high) {
//        return ((long) low & 0xFFFFFFFFl) | (((long) high << 32) & 0xFFFFFFFF00000000l);
//    }
//
//    /**
//     * 从指针地址获取结构体数据
//     * 不要使用 modules.CourseRecordModule 内的二次封装方法，由于 NET_OUT_QUERY_COURSEMEDIA_FILE 特别大
//     * 直接使用 read() 和 write() 会极其耗时。请使用本方法，只拷贝必须的数据
//     */
//    public static void GetPointerDataToCourseMediaInfo(NET_OUT_QUERY_COURSEMEDIA_FILE stuQueryOut) {
//
//        stuQueryOut.readField("nCountResult");
//
//        long offset = stuQueryOut.fieldOffset("stuCourseMediaFile");
//        Pointer pQueryOut = stuQueryOut.getPointer();
//
//        int sizeOfMediaFile = stuQueryOut.stuCourseMediaFile[0].size();
//        for (int i = 0; i < stuQueryOut.nCountResult; i++) {
//            Pointer pMediaFile = stuQueryOut.stuCourseMediaFile[i].getPointer();
//            pMediaFile.write(0, pQueryOut.getByteArray(offset, sizeOfMediaFile), 0, sizeOfMediaFile);
//            GetPointerDataToStructMediaFile(stuQueryOut.stuCourseMediaFile[i]);
//            offset += sizeOfMediaFile;
//        }
//    }
//
//    /**
//     * 从指针地址获取结构体数据
//     */
//    public static void GetPointerDataToStructMediaFile(NET_COURSEMEDIA_FILE_INFO courseMediaFile) {
//        courseMediaFile.readField("nID");
//        courseMediaFile.readField("stuCourseInfo");
//        courseMediaFile.readField("nChannelNum");
//        courseMediaFile.readField("nRecordNum");
//
//        long offset = courseMediaFile.fieldOffset("stuRecordInfo_1");
//        Pointer pMediaFile = courseMediaFile.getPointer();
//
//        int sizeOfRecordInfo_1 = courseMediaFile.stuRecordInfo_1[0].size();
//        int nChannelNum = courseMediaFile.nChannelNum;  // 通道数量 (1-64)
//        for (int i = 0; i < nChannelNum; i++) {
//            Pointer pRecordRecordInfo_1 = courseMediaFile.stuRecordInfo_1[i].getPointer();
//            pRecordRecordInfo_1.write(0, pMediaFile.getByteArray(offset, sizeOfRecordInfo_1), 0, sizeOfRecordInfo_1);
//            int recordNum = courseMediaFile.nRecordNum[i]; // 通道下录像分段数(1-16)
//            GetPointerDataToStructRecordInfoArray(courseMediaFile.stuRecordInfo_1[i], recordNum);
//            offset += sizeOfRecordInfo_1;
//        }
//    }
//
//    /**
//     * 从指针地址获取结构体数据
//     */
//    public static void GetPointerDataToStructRecordInfoArray(NET_RECORD_INFO_ARRAY recordInfoArray, int recordNum) {
//
//        long offset = 0;
//        Pointer pRecordInfo_1 = recordInfoArray.getPointer();
//
//        int sizeOfRecordInfo_2 = recordInfoArray.stuRecordInfo_2[0].size();
//        for (int i = 0; i < recordNum; i++) {
//            Pointer pRecordInfo_2 = recordInfoArray.stuRecordInfo_2[i].getPointer();
//            pRecordInfo_2.write(0, pRecordInfo_1.getByteArray(offset, sizeOfRecordInfo_2), 0, sizeOfRecordInfo_2);
//            recordInfoArray.stuRecordInfo_2[i].read();
//            offset += sizeOfRecordInfo_2;
//        }
//    }
//
//    /**
//     * 关闭查询
//     */
//    public void CloseQueryCourseMediaFileTest() {
//        if (FindID == 0) {
//            System.err.println("请不要重复关闭");
//            return;
//        }
//
//        NET_IN_QUERY_COURSEMEDIA_FILECLOSE stuIn = new NET_IN_QUERY_COURSEMEDIA_FILECLOSE();
//        stuIn.nFindID = FindID;     // 填写查询句柄
//        NET_OUT_QUERY_COURSEMEDIA_FILECLOSE stuOut = new NET_OUT_QUERY_COURSEMEDIA_FILECLOSE();
//        boolean ret = CloseQueryCourseMediaFile(LoginModule.m_hLoginHandle, stuIn, stuOut, 3000);
//        if (!ret) {
//            System.err.println("关闭查询记录失败!");
//            return;
//        }
//        System.out.println("关闭查询记录成功!");
//        FindID = 0;
//        total = 0;
//    }

}
