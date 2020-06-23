package com.kanouakira.vueblog.controller;

import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.service.UploadPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    private UploadPictureService uploadPictureService;

    @PostMapping("/picture")
    @CrossOrigin
    public Result uploadPicture(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String pictureUrl = uploadPictureService.uploadPicture(multipartFile);
        return Result.succ(pictureUrl);
    }
}
