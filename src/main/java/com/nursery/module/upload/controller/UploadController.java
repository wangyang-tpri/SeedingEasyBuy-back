package com.nursery.module.upload.controller;

import com.nursery.common.MinioService;
import com.nursery.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Resource
    private MinioService minioService;

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
        try (InputStream in = minioService.download(objectName);
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
}
