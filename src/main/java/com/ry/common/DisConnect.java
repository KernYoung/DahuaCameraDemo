package com.ry.common;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisConnect implements NetSDKLib.fDisConnect{

    Logger logger = LoggerFactory.getLogger(DisConnect.class);

    @Override
    public void invoke(NetSDKLib.LLong lLong, String s, int i, Pointer pointer) {
        logger.error("客户端失去连接");
    }
}
