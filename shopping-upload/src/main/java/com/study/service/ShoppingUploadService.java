package com.study.service;

import org.springframework.web.multipart.MultipartFile;

public interface ShoppingUploadService {

    public String uploadImage(MultipartFile file);
}
