package com.ry.controller;


import com.ry.service.IInstructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class InstructController {
    @Autowired
    private IInstructService service;

    public String VideoPath;
    public String getVideoPath() {
        return VideoPath;
    }
    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    @GetMapping("/ss")
    public Map<String,Object> SendMessage(PreInfo info){
        service.SendMessage(info);
        InstructController instr = new InstructController();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("videopath", instr.getVideoPath());
        return result;
    }


}

