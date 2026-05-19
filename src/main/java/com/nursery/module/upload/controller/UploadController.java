package com.nursery.module.upload.controller;

import cn.hutool.core.util.IdUtil;
import com.nursery.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Value("${upload.path:./uploads/}")
    private String uploadPath;

    private File getUploadDir() {
        Path path = Paths.get(uploadPath);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(path);
        }
        File dir = path.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.fail("上传文件为空");
        }
        try {
            File dir = getUploadDir();
            log.info("Upload directory: {}", dir.getAbsolutePath());

            String originalName = file.getOriginalFilename();
            String ext = ".jpg";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = IdUtil.fastSimpleUUID() + ext;

            File dest = new File(dir, fileName);
            file.transferTo(dest);
            log.info("File uploaded to: {}", dest.getAbsolutePath());

            Map<String, String> result = new HashMap<>();
            result.put("url", "/api/file/" + fileName);
            return Result.ok(result);
        } catch (IOException e) {
            log.error("File upload failed", e);
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }
}
