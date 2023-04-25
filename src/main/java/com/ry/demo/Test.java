package com.ry.demo;

import com.ry.demo.module.CommonWithCallBack;
import com.ry.demo.module.LoginModule;

public class Test {
    public static void main(String[] args){
        LoginModule.init(null, null);
        LoginModule.login("sunkoda.us.to", 37771, "admin", "admin123");
        CommonWithCallBack ccb = new CommonWithCallBack(LoginModule.m_hLoginHandle);

        ccb.PlayBackByDataType();
    }

}
