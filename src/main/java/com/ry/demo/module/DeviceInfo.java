package com.ry.demo.module;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.NetSDKLib.LLong;


public class DeviceInfo {


    private String m_strIp;
    private int m_nPort;
    private String m_strUser;
    private String m_strPassword;
    private String m_srtId;


    public DeviceInfo(String m_strIp, int m_nPort, String m_strUser, String m_strPassword) {
        this.m_strIp = m_strIp; //设备ip
        this.m_nPort = m_nPort; //设备端口号
        this.m_strUser = m_strUser;  //设备登录用户名
        this.m_strPassword = m_strPassword;  //设备登陆密码
    }

    public DeviceInfo() {
    }

    //获取设备ID
    public String getM_srtId() {
        return m_srtId;
    }
    //设置设备ID
    public void setM_srtId(String m_srtId) {
        this.m_srtId = m_srtId;
    }
    //获取设备用户名
    public String getM_strUser() {
        return m_strUser;
    }
    //设置设备用户名
    public void setM_strUser(String m_strUser) {
        this.m_strUser = m_strUser;
    }
    //获取设备登录密码
    public String getM_strPassword() {
        return m_strPassword;
    }
    //设置设备登录密码
    public void setM_strPassword(String m_strPassword) {
        this.m_strPassword = m_strPassword;
    }
    //获取设备IP
    public String getM_strIp() {
        return m_strIp;
    }
    //设置设备IP
    public void setM_strIp(String m_strIp) {
        this.m_strIp = m_strIp;
    }
    //获取设备端口号
    public int getM_nPort() {
        return m_nPort;
    }
    //设置设备端口号
    public void setM_nPort(int m_nPort) {
        this.m_nPort = m_nPort;
    }

    public static final NetSDKLib netSdk = NetSDKLib.NETSDK_INSTANCE;
    //获取登录句柄
    public LLong getLoginHandle() {
        return loginHandle;
    }

    // 登陆句柄
    private LLong loginHandle = new LLong(0);

}

