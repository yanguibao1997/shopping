package com.study.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.study.controller.ShoppingUploadControl;
import com.study.service.ShoppingUploadService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

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

//          上传到FastDFS 返回路径
//            文件后缀名
            String suffix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);


//            下面进行上传  返回URL
            String url="http://image.shopping.com/"+storePath.getFullPath();
            System.out.println(url);
            return url;
        }catch (Exception e){
            return null;
        }
    }
}
