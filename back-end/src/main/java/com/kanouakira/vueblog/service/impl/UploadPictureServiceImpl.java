package com.kanouakira.vueblog.service.impl;

import com.google.gson.Gson;
import com.kanouakira.vueblog.service.UploadPictureService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

@Service
public class UploadPictureServiceImpl implements UploadPictureService {

    @Override
    public String uploadPicture(MultipartFile file) throws IOException {
        String qiniuUrl = "cdn.kanouakira.cn";
        Configuration configuration = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(configuration);
        String accessKey = "clKLmeU_yLlztFrj4gCqiwuMPcfwZV-yxOVybgeo";
        String secretKey = "W8dHQOUaw-j9cvG25EN3kFa6PLsMp7mx8OMGEFsE";
        String bucket = "kanouakira-blog";
        String key = getRandomCharacterAndNumber(10) + ".png";//生成随机文件名
        Auth auth = Auth.create(accessKey,secretKey);
        String uptoken = auth.uploadToken(bucket);
        String responseUrl = "http://";
        try{
            byte[] localFile = file.getBytes();
            Response response = uploadManager.put(localFile,key,uptoken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            responseUrl = responseUrl + qiniuUrl + '/' +putRet.key;
        }catch (QiniuException e){
            Response r = e.response;
        }
        return responseUrl;
    }

    public static String getRandomCharacterAndNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

            if ("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
                // int choice = 97; // 指定字符串为小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
