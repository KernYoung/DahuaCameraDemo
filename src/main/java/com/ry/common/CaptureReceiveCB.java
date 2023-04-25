package com.ry.common;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class CaptureReceiveCB implements NetSDKLib.fSnapRev{

    Logger logger = LoggerFactory.getLogger(CaptureReceiveCB.class);

    String ip;

    private String filename;

    public CaptureReceiveCB(String ip, String filename) {
        this.ip = ip;
        this.filename = filename;
    }

    @Override
    public void invoke(NetSDKLib.LLong lLoginID, Pointer pBuf, int RevLen, int EncodeType, int CmdSerial, Pointer dwUser) {
        if(pBuf != null && RevLen > 0) {
            try {
                byte[] buf = pBuf.getByteArray(0, RevLen);
                ByteArrayInputStream byteArrInput = new ByteArrayInputStream(buf);
                BufferedImage bufferedImage = ImageIO.read(byteArrInput);
                if(bufferedImage == null) {
                    return;
                }
                String path = this.getClass().getResource("/").getPath() + "/static/" + filename;
                ImageIO.write(bufferedImage, "jpg", new File(path));
            } catch (IOException e) {
                logger.error("写文件失败:" + e.getMessage(), e);
            }finally {
                String key = ip + lLoginID.longValue();
                Semaphore semaphore = Cache.getSemaphore(key);
                semaphore.release();
            }
        }
    }
}

