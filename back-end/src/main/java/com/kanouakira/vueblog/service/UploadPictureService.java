package com.kanouakira.vueblog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadPictureService {
    String uploadPicture(MultipartFile file) throws IOException;
}
