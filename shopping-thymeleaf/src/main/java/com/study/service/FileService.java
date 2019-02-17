package com.study.service;

import java.io.File;

public interface FileService {

    void createHtml(Long id) throws Exception;

    boolean exists(Long spuId);

    void syncCreateHtml(Long spuId);
}
