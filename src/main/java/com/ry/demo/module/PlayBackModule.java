package com.ry.demo.module;

import com.netsdk.lib.LastError;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.NetSDKLib.LLong;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * 录像回放实现
 */
public class PlayBackModule {
	// 下载句柄
//	public static LLong m_hDownLoadHandle = new LLong(0);
	// 回放句柄
	public static LLong m_hPlayHandle = new LLong(0);

	public static void stopDownLoadRecordFile(LLong m_hDownLoadHandle) {
		if (m_hDownLoadHandle.longValue() == 0) {
			return;
		}
		LoginModule.netsdk.CLIENT_StopDownload(m_hDownLoadHandle);
	}

//	/**
//	 * 录像回放
//	 */
//	public void playBack(int nStreamType, int nChannel,
//											NetSDKLib.NET_TIME stTimeStart, NetSDKLib.NET_TIME stTimeEnd,
//											String savedFileName) {
//
//		if (LoginModule.m_hLoginHandle.longValue() == 0){
//			System.err.printf("Please Login First");
//			return;
//		}
//
//		// 设置回放时的码流类型
//		IntByReference steamType = new IntByReference(0);// 0-主辅码流,1-主码流,2-辅码流
//		int emType = NetSDKLib.EM_USEDEV_MODE.NET_RECORD_STREAM_TYPE;
//		boolean bret = LoginModule.netsdk.CLIENT_SetDeviceMode(LoginModule.m_hLoginHandle, emType,steamType.getPointer());
//		if (!bret) {
//			System.err.printf("Set Stream Type Failed, Get last error [0x%x]\n", LoginModule.netsdk.CLIENT_GetLastError());
//		}
//
//		// 设置回放时的录像文件类型
//		IntByReference emFileType = new IntByReference(nStreamType);// 所有录像NET RECORD TYPE
//		emType = NetSDKLib.EM_USEDEV_MODE.NET_RECORD_TYPE;
//		bret = LoginModule.netsdk.CLIENT_SetDeviceMode(LoginModule.m_hLoginHandle, emType, emFileType.getPointer());
//
//		if (!bret){
//		System.err.printf("Set Record Type Failed, Get last error [0x%x]\n", LoginModule.netsdk.CLIENT_GetLastError());
//		}
//
//		m_hPlayHandle = LoginModule.netsdk.CLIENT_PlayBackByTimeEx(LoginModule.m_hLoginHandle, nChannel, stTimeStart, stTimeEnd, null, null);
//		if (m_hPlayHandle.longValue() == 0){
//			int error = LoginModule.netsdk.CLIENT_GetLastError();
//			System.err.printf("PlayBackByTimeEx Failed, Get last error [0x%x]\n", error);
//			switch (error){
//				case LastError.NET_ERROR_NO_RECORD:
//					System.out.println("查不到录像");
//					break;
//			}
//		}else {
//			System.out.println("PlayBackByTimeEx Successed.");
////			m_playFlag = true;// 打开播放标志位
////			playButton.setText("停止回放");
////			panelPlayBack.repaint();
////			panelPlayBack.setVisible(true);
//		}
//
//
////		if (!setStreamType(nStreamType)) {
////			System.err.println("Set Stream Type Failed!." + ToolKits.getErrorCode());
////			return;
////		}
////
////		NetSDKLib.NET_IN_DOWNLOAD_BY_DATA_TYPE stIn = new NetSDKLib.NET_IN_DOWNLOAD_BY_DATA_TYPE();
////
////		stIn.emDataType = nType;            // 封装类型
////		stIn.emRecordType = NetSDKLib.EM_QUERY_RECORD_TYPE.EM_RECORD_TYPE_ALL; // 所有录像
////		stIn.nChannelID = nChannel;
////		stIn.stStartTime = stTimeStart;     // 开始时间
////		stIn.stStopTime = stTimeEnd;        // 结束时间
////		stIn.cbDownLoadPos = DownLoadRecordModule.DownloadPosCallback.getInstance();           // 下载监控回调函数
////		stIn.dwPosUser = null;
////		stIn.fDownLoadDataCallBack = DownLoadRecordModule.DownLoadDataCallBack.getInstance();  // 下载数据回调函数
////		stIn.dwDataUser = null;
////		stIn.szSavedFileName = savedFileName;
////
////		NetSDKLib.NET_OUT_DOWNLOAD_BY_DATA_TYPE stOut = new NetSDKLib.NET_OUT_DOWNLOAD_BY_DATA_TYPE();
////		stIn.write();
////		stOut.write();
////		m_hDownLoadHandle = LoginModule.netsdk.CLIENT_DownloadByDataType(LoginModule.m_hLoginHandle, stIn.getPointer(), stOut.getPointer(), 5000);
////		if (m_hDownLoadHandle.longValue() != 0) {
////			System.out.println("DownloadByDataType Succeed!");
////		} else {
////			System.err.println("DownloadByDataType Failed! " + ToolKits.getErrorCode());
////		}
//	}

	/**
	 * 录像回放
	 */
	public void playBack(int nStreamType, int nChannel,
											NetSDKLib.NET_TIME stTimeStart, NetSDKLib.NET_TIME stTimeEnd,
											String savedFileName, int nType) {

		if (!setStreamType(nStreamType)) {
			System.err.println("Set Stream Type Failed!." + ToolKits.getErrorCode());
			return;
		}

		NetSDKLib.NET_IN_PLAYBACK_BY_DATA_TYPE stIn = new NetSDKLib.NET_IN_PLAYBACK_BY_DATA_TYPE();

		stIn.nChannelID = nChannel;// 通道编号
		stIn.stStartTime = stTimeStart;// 开始时间
		stIn.stStopTime = stTimeEnd;// 结束时间
		stIn.hWnd = null;//播放窗格
		stIn.cbDownLoadPos = PlayBackModule.PlayBackPosCallback.getInstance();//进度回调
		stIn.dwPosUser = null;//进度回调用户信息
		stIn.fDownLoadDataCallBack = PlayBackModule.DownLoadDataCallBack.getInstance(); //数据回调
		stIn.emDataType = nType;//回调的数据类型
		stIn.dwDataUser = null;//数据回调用户信息
		stIn.nPlayDirection = 0;//播放方向 0：正放 1：倒放

		NetSDKLib.NET_OUT_PLAYBACK_BY_DATA_TYPE stOut = new NetSDKLib.NET_OUT_PLAYBACK_BY_DATA_TYPE();
		stIn.write();
		stOut.write();
		m_hPlayHandle = LoginModule.netsdk.CLIENT_PlayBackByDataType(LoginModule.m_hLoginHandle, stIn, stOut, 5000);
		if (m_hPlayHandle.longValue() != 0) {
			System.out.println("PlayBackByDataType Succeed!");
		} else {
			System.err.println("PlayBackByDataType Failed! " + ToolKits.getErrorCode());
		}
	}

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
	public static class PlayBackPosCallback implements NetSDKLib.fDownLoadPosCallBack {

		private PlayBackPosCallback() {
		}

		private static class CallBackHolder {
			private static final PlayBackPosCallback callback = new PlayBackPosCallback();
		}

		public static PlayBackPosCallback getInstance() {
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

}
