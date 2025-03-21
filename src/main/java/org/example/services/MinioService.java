package org.example.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioService(@Value("${minio.url}") String url,
                        @Value("${minio.bucket.name}") String bucketName) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .build();
        this.bucketName = bucketName;
    }
    @Transactional
    public String uploadFile(InputStream fileStream, String fileName) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(fileStream, -1, PutObjectArgs.MIN_MULTIPART_SIZE)
                        .contentType("image/png")
                        .build()
        );
        return "http://77.246.158.84:9000/" + bucketName + "/" + fileName;
    }
}
