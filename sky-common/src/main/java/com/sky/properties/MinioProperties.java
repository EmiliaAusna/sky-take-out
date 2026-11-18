package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MinIO 文件存储配置属性
 */
@Component
@ConfigurationProperties(prefix = "sky.minio")
@Data
public class MinioProperties {

    /** MinIO 服务地址，如 http://localhost:9000 */
    private String endpoint;

    /** 访问密钥 AccessKey */
    private String accessKey;

    /** 访问密钥 SecretKey */
    private String secretKey;

    /** 存储桶名称 */
    private String bucketName;

}
