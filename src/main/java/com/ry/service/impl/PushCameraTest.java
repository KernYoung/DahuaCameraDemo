package com.ry.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

@Slf4j
public class PushCameraTest {
    @Setter
    private String src = "rtsp://admin:admin123@192.168.10.243/:554/cam/realmonitor?channel=1&subtype=0";

    @Setter
    private String target = "rtmp://118.178.191.50:1935/myapp/room";

    @Setter
    private String transportType;

    // 帧抓取器(FrameGrabbe)
    private FFmpegFrameGrabber grabber;

    // 帧录制器/推流器(FrameRecorder)
    private FFmpegFrameRecorder recorder;

    // 是否启动
    @Setter
    private boolean isStart = false;

    /**
     * 开启获取rtsp流
     */
    public void live() {
        log.info("连接rtsp：" + src + ",开始创建grabber");
        boolean isSuccess = createGrabber(src);
        if (isSuccess) {
            log.info("live:创建grabber成功");
        } else {
            log.info("live:创建grabber失败");
        }
        startPushStream();
    }

    /**
     * 构造视频抓取器
     *
     * @param src 拉流地址
     * @return 创建成功与否
     */
    private boolean createGrabber(String src) {
        // 获取视频源
        try {
            grabber = FFmpegFrameGrabber.createDefault(src);
            grabber.setOption("rtsp_transport", transportType);
            grabber.start();
            isStart = true;
            recorder = new FFmpegFrameRecorder(target, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
            //avcodec.AV_CODEC_ID_H264
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            //转码格式
            recorder.setFormat("flv");
            //帧率
            recorder.setFrameRate(grabber.getFrameRate());
            //采样率
            recorder.setSampleRate(grabber.getSampleRate());
            //声道
            recorder.setAudioChannels(grabber.getAudioChannels());
            return true;
        } catch (FrameGrabber.Exception e) {
            log.error("创建解析FFmpegFrameGrabber失败:", e);
            this.stop();
            this.reset();
            return false;
        }
    }

    /**
     * 推送流
     */
    private void startPushStream() {
        if (grabber == null) {
            log.info("重试连接src：" + src + ",开始创建grabber");
            boolean isSuccess = createGrabber(src);
            if (isSuccess) {
                log.info("push:创建grabber成功");
            } else {
                log.info("push:创建grabber失败");
            }
        }
        try {
            recorder.start();
            log.info("start recoder");
            Frame frame;
            while (isStart && (frame = grabber.grabFrame()) != null) {
                recorder.setTimestamp(grabber.getTimestamp());
                recorder.record(frame);
            }
            stop();
            reset();
            log.info("stop recoder");
        } catch (FrameGrabber.Exception | RuntimeException | FrameRecorder.Exception e) {
            log.error("exception", e);
            stop();
            reset();
        }
    }

    //停止
    private void stop() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            if (grabber != null) {
                grabber.stop();
            }
        } catch (Exception e) {
            log.error("stop exception:", e);
        }
    }
    //重置
    private void reset() {
        recorder = null;
        grabber = null;
        isStart = false;
    }

    public static void main(String[] args){
        PushCameraTest pt = new PushCameraTest();
        pt.live();
    }
}

