package com.nursery.module.upload.controller;

import cn.hutool.core.util.IdUtil;
import com.nursery.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class UploadController {

    @Value("${upload.path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
        String fileName = IdUtil.fastSimpleUUID() + ext;

        File dest = new File(dir, fileName);
        file.transferTo(dest);

        Map<String, String> result = new HashMap<>();
        result.put("url", "/api/file/" + fileName);
        return Result.ok(result);
    }
}
