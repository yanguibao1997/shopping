package com.study.service.impl;

import com.study.controller.ShoppingUploadControl;
import com.study.service.ShoppingUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class ShoppingUploadServiceImpl implements ShoppingUploadService {

    private  static  final Logger logger= LoggerFactory.getLogger(ShoppingUploadControl.class);

    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Override
    public String uploadImage(MultipartFile file) {
        try{
            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
//          校验图片   首先看图片格式    在读图片
            if(!suffixes.contains(contentType)){
                logger.info("上传失败，文件类型不符合：{}",contentType);
                return null;
            }
            BufferedImage read = ImageIO.read(file.getInputStream());
            if(read == null){
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }

//            测试存放目录
            File f=new File("E:\\test");
            if(!f.exists()){
                f.mkdirs();
            }

            file.transferTo(new File(f,filename));

//            下面进行上传  返回URL
            String url="";
            return url;
        }catch (Exception e){
            return null;
        }
    }
}
