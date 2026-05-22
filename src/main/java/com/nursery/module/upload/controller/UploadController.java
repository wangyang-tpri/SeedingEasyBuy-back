package com.nursery.module.upload.controller;

import com.nursery.common.MinioService;
import com.nursery.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Resource
    private MinioService minioService;

    @Value("${upload.path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.fail("上传文件为空");
        }
        try {
            String url = minioService.upload(file);
            log.info("File uploaded to MinIO: {}", url);
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("File upload failed", e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/{objectName}")
    public void download(@PathVariable String objectName, HttpServletResponse response) {
        // Try MinIO first, fall back to local file system
        try (InputStream in = getStream(objectName);
             OutputStream out = response.getOutputStream()) {
            String contentType = "image/jpeg";
            if (objectName.endsWith(".png")) contentType = "image/png";
            else if (objectName.endsWith(".gif")) contentType = "image/gif";
            else if (objectName.endsWith(".webp")) contentType = "image/webp";
            else if (objectName.endsWith(".mp4")) contentType = "video/mp4";
            response.setContentType(contentType);
            response.setHeader("Cache-Control", "public, max-age=86400");
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("File download failed: {}", objectName, e);
            response.setStatus(404);
        }
    }

        /**
         * 获取文件输入流，优先从MinIO对象存储下载，失败时降级到本地文件系统
         *
         * @param objectName 文件对象名称（路径）
         * @return 文件输入流
         * @throws IOException 当MinIO和本地文件系统都找不到文件时抛出异常
         */
        private InputStream getStream(String objectName) throws IOException {
            try {
                return minioService.download(objectName);
            } catch (Exception e) {
                log.info("MinIO miss, trying local: {}", objectName);

                // 解析本地文件路径，确保使用绝对路径
                Path path = Paths.get(uploadPath);
                if (!path.isAbsolute()) {
                    path = Paths.get(System.getProperty("user.dir")).resolve(path);
                }

                // 检查本地文件是否存在并返回输入流
                File file = new File(path.toFile(), objectName);
                if (file.exists()) {
                    return Files.newInputStream(file.toPath());
                }
                throw new FileNotFoundException("File not found: " + objectName);
            }
        }
}
