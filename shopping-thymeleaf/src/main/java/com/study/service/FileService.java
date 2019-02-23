package com.study.service;

import java.io.File;

public interface FileService {

    void createHtml(Long spuId) throws Exception;

    void deleteHtml(Long spuId);

    boolean exists(Long spuId);

    void syncCreateHtml(Long spuId);
}
