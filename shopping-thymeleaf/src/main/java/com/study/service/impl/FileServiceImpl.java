package com.study.service.impl;

import com.study.service.FileService;
import com.study.service.ThymeleafService;
import com.study.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${shopping.thymeleaf.destPath}")
    private String destPath; //D:\softForInstall\nginx-1.12.2\html\item

    /**
     * 创建html页面
     * @param spuId
     * @throws Exception
     */
    public void createHtml(Long spuId) throws Exception {
        // 创建上下文，
        Context context = new Context();
        // 把数据加入上下文
        context.setVariables(thymeleafService.loadThymeleaf(spuId));

        // 创建输出流，关联到一个临时文件
        File temp = new File(spuId + ".html");
        // 目标页面文件
        File dest = createPath(spuId);
        // 备份原页面文件
        File bak = new File(spuId + "_bak.html");
        try (PrintWriter writer = new PrintWriter(temp, "UTF-8")) {
            // 利用thymeleaf模板引擎生成 静态页面
            templateEngine.process("item", context, writer);

            if (dest.exists()) {
                // 如果目标文件已经存在，先备份
                dest.renameTo(bak);
            }
            // 将新页面覆盖旧页面
            FileCopyUtils.copy(temp,dest);
            // 成功后将备份页面删除
            bak.delete();
        } catch (IOException e) {
            // 失败后，将备份页面恢复
            bak.renameTo(dest);
            // 重新抛出异常，声明页面生成失败
            throw new Exception(e);
        } finally {
            // 删除临时页面
            if (temp.exists()) {
                temp.delete();
            }
        }
    }

    @Override
    public void deleteHtml(Long spuId) {
        File file = new File(this.destPath, spuId + ".html");
        file.deleteOnExit();
    }

    private File createPath(Long spuId) {
        if (spuId == null) {
            return null;
        }
//        创建静态文件的目录
        File dest = new File(this.destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return new File(dest, spuId + ".html");
    }

    /**
     * 判断某个商品的页面是否存在
     * @param spuId
     * @return
     */
    public boolean exists(Long spuId){
        return this.createPath(spuId).exists();
    }

    /**
     * 异步创建html页面
     * @param spuId
     */
    public void syncCreateHtml(Long spuId){
        ThreadUtils.execute(() -> {
            try {
                createHtml(spuId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}