package com.sky.utils;

import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * MinIO 文件上传工具类
 */
@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    private MinioClient client;

    public MinioUtil(String endpoint, String accessKey, String secretKey, String bucketName) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        initBucket();
    }

    /**
     * 初始化桶：不存在则创建，并设置为公开读
     */
    private void initBucket() {
        try {
            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO 桶创建成功：{}", bucketName);
            }
            // 设置桶策略为公开读
            String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
            client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
            log.info("MinIO 桶公开读策略设置成功：{}", bucketName);
        } catch (Exception e) {
            log.error("MinIO 桶初始化失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param bytes      文件内容
     * @param objectName 对象名（含路径）
     * @return 可访问的完整 URL
     */
    public String upload(byte[] bytes, String objectName) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                    .build());
        } catch (Exception e) {
            log.error("MinIO 文件上传失败：{}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败", e);
        }

        // 访问路径规则：endpoint/bucketName/objectName
        String url = endpoint + "/" + bucketName + "/" + objectName;
        log.info("文件上传到:{}", url);
        return url;
    }
}
