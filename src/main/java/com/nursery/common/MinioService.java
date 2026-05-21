package com.nursery.common;

import cn.hutool.core.util.IdUtil;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

@Service
public class MinioService {

    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String upload(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String ext = ".jpg";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String objectName = IdUtil.fastSimpleUUID() + ext;

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return "/api/file/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("MinIO上传失败: " + e.getMessage(), e);
        }
    }

    public InputStream download(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO下载失败: " + e.getMessage(), e);
        }
    }

    public void delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO删除失败: " + e.getMessage(), e);
        }
    }
}
